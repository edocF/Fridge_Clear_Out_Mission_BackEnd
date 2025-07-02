package com.example.backend.service;

import com.example.backend.pojo.NutritionReportRequest;
import com.example.backend.pojo.ConsumptionReportRequest;

/**
 * ReportService
 * 负责与报告生成相关的业务逻辑接口定义。
 * 目前主要用于生成食物保鲜报告，后续可扩展更多类型的报告。
 */
public interface ReportService {

    /**
     * 生成当前用户的食物保鲜报告。
     * 该方法会收集用户冰箱中的所有食物信息，
     * 并调用AI服务生成一份详细的保鲜分析报告。
     *
     * @return String 由AI生成的报告文本，内容为中文。
     */
    String generateFreshnessReport();

    /**
     * 生成当前用户的膳食营养报告。
     * 该方法会收集用户的食物消耗记录，
     * 并调用AI服务生成一份详细的营养分析报告。
     *
     * @return String 由AI生成的报告文本，内容为中文。
     */
    String generateNutritionReport(NutritionReportRequest request);

    /**
     * 生成当前用户的食物消耗报告。
     * 该方法会收集用户的食物消耗记录，并调用AI服务生成详细的消耗分析报告。
     *
     * @param request 消耗报告请求参数
     * @return String 由AI生成的报告文本，内容为中文。
     */
    String generateConsumptionReport(ConsumptionReportRequest request);

    // 未来可扩展：
    // String generateConsumptionReport(); // 生成消耗报告
}