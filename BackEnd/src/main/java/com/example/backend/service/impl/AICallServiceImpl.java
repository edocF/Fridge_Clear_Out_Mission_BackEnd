package com.example.backend.service.impl;

import com.example.backend.exception.AIException;
import com.example.backend.pojo.Food;
import com.example.backend.promptStrategy.GetFoodNutritionByNameStrategy;
import com.example.backend.promptStrategy.GenerateFreshnessReportStrategy;
import com.example.backend.promptStrategy.PromptStrategy;
import com.example.backend.service.AICallService;
import com.example.backend.utils.AIConfig;
import com.example.backend.utils.VivoAuth;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class AICallServiceImpl implements AICallService {
    private static final String API_URL = "https://api-ai.vivo.com.cn/vivogpt/completions";
    private static final String API_PATH = "/vivogpt/completions";
    private final RestTemplate restTemplate;
    private final AIConfig aiConfig;
    private final GetFoodNutritionByNameStrategy getFoodNutritionByNameStrategy;
    private final GenerateFreshnessReportStrategy generateFreshnessReportStrategy;

    @Autowired
    public AICallServiceImpl(RestTemplate restTemplate, AIConfig aiConfig, GetFoodNutritionByNameStrategy getFoodNutritionByNameStrategy, GenerateFreshnessReportStrategy generateFreshnessReportStrategy) {
        this.restTemplate = restTemplate;
        this.aiConfig = aiConfig;
        this.getFoodNutritionByNameStrategy = getFoodNutritionByNameStrategy;
        this.generateFreshnessReportStrategy = generateFreshnessReportStrategy;
    }

    @Override
    public Food getFoodNutrition(String foodName) throws AIException {
       PromptStrategy promptStrategy = this.getFoodNutritionByNameStrategy;
       Object[] params = new Object[]{foodName};
       return (Food) GetInfoFromAI(promptStrategy,params);
    }

    @Override
    public String generateFreshnessReport(List<Food> foods) {
        PromptStrategy promptStrategy = this.generateFreshnessReportStrategy;
        Object[] params = new Object[]{foods};
        return (String) getReportFromAI(promptStrategy, params);
    }

    private Object GetInfoFromAI(PromptStrategy promptStrategy,Object... params) throws AIException {
        try {
            UUID requestId = UUID.randomUUID();
            UUID sessionId = UUID.randomUUID();
            log.info("requestId: " + requestId);
            //产生查询参数
            Map<String,Object> queryParams = new HashMap<>();
            queryParams.put("requestId", requestId.toString());
            String queryStr = mapToQueryString(queryParams);
            //产生请求体
            Map<String, Object> requestBody = buildRequestBody(promptStrategy,sessionId.toString(),params);
            //产生请求头
            HttpHeaders headers = null;
            try {
                headers = VivoAuth.generateAuthHeaders(
                        aiConfig.getAppId(),
                        aiConfig.getAppKey(),
                        "POST",
                        API_PATH,
                        queryStr
                );
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            headers.setContentType(MediaType.APPLICATION_JSON);
            //拼接完整请求URL
            String url = String.format("http://api-ai.vivo.com.cn%s?%s", API_PATH, queryStr);
            //发送请求
            HttpEntity<Map<String,Object>> requestEntity = new HttpEntity<>(requestBody,headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            log.info("response: " + response.getBody());
            //解析响应
            return parseResponse(promptStrategy,response,params);
        } catch (Exception e) {
            throw new AIException("AI调用失败" + params, e);
        }
    }

    /**
     * 通用AI调用方法，专用于返回文本报告
     * @param promptStrategy 具体的Prompt策略
     * @param params         传递给Prompt的数据
     * @return String AI生成的报告文本
     */
    private Object getReportFromAI(PromptStrategy promptStrategy, Object... params) {
        try {
            UUID requestId = UUID.randomUUID();
            UUID sessionId = UUID.randomUUID();
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("requestId", requestId.toString());
            String queryStr = mapToQueryString(queryParams);
            Map<String, Object> requestBody = buildRequestBody(promptStrategy, sessionId.toString(), params);
            HttpHeaders headers = VivoAuth.generateAuthHeaders(
                    aiConfig.getAppId(),
                    aiConfig.getAppKey(),
                    "POST",
                    API_PATH,
                    queryStr
            );
            headers.setContentType(MediaType.APPLICATION_JSON);
            String url = String.format("http://api-ai.vivo.com.cn%s?%s", API_PATH, queryStr);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return parseReportResponse(promptStrategy, response, params);
        } catch (Exception e) {
            throw new AIException("AI调用失败" + params, e);
        }
    }

    /**
     * 解析AI返回的报告文本
     * @param promptStrategy Prompt策略
     * @param response       AI响应
     * @param params         传递参数
     * @return String AI生成的报告文本
     */
    private String parseReportResponse(PromptStrategy promptStrategy, ResponseEntity<String> response, Object... params) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AIException("AI调用失败,状态码:" + response.getStatusCode() + " .错误信息: " + response.getBody());
        }
        String responseBody = response.getBody();
        if (responseBody == null) {
            throw new AIException("AI调用失败,响应体为空");
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            int code = rootNode.path("code").asInt();
            if (code != 0) {
                String msg = rootNode.path("msg").asText("未知错误");
                throw new AIException("AI调用失败,错误码:" + code + ", 错误信息: " + msg);
            }
            String content = rootNode.path("data").path("content").asText();
            if (content.isEmpty()) {
                throw new AIException("AI返回内容为空");
            }
            // 直接返回AI生成的报告文本
            return content;
        } catch (JsonProcessingException e) {
            throw new AIException("解析AI响应失败", e);
        }
    }

    //构建请求体
    private Map<String, Object> buildRequestBody(PromptStrategy promptStrategy,String sessionId,Object... params) {
        Map<String,Object> requestBody = new HashMap<>();
        requestBody.put("prompt",promptStrategy.buildPrompt(params));
        requestBody.put("model",aiConfig.getModelName());
        requestBody.put("sessionId",sessionId.toString());
        requestBody.put("extra",buildExtraParam());
        return requestBody;
    }
    //构建extra参数
    private Object buildExtraParam() {
        Map<String,Object> extra = new HashMap<>();
        extra.put("temperature",aiConfig.getTemperature());
        extra.put("max_new_tokens",aiConfig.getMaxTokens());
        return extra;
    }

    //解析响应
    private Food parseResponse(PromptStrategy promptStrategy,ResponseEntity<String> response, Object... params) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new AIException("AI调用失败,状态码:" + response.getStatusCode() + " .错误信息: " + response.getBody());
        }

        String responseBody = response.getBody();
        if (responseBody == null) {
            throw new AIException("AI调用失败,响应体为空");
        }

        try {
            // 解析JSON响应，提取content字段
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // 检查code是否为0（成功）
            int code = rootNode.path("code").asInt();
            if (code != 0) {
                String msg = rootNode.path("msg").asText("未知错误");
                throw new AIException("AI调用失败,错误码:" + code + ", 错误信息: " + msg);
            }

            // 提取content字段
            String content = rootNode.path("data").path("content").asText();
            if (content.isEmpty()) {
                throw new AIException("AI返回内容为空");
            }

            return (Food) promptStrategy.parseContent(content,params);
        } catch (JsonProcessingException e) {
            throw new AIException("解析AI响应失败", e);
        }
    }
    //将map转换为查询字符串
    private static String mapToQueryString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }
        return queryString.toString();
    }


}
