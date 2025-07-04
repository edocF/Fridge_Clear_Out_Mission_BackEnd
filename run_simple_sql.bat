@echo off
echo 正在执行SQL脚本添加缺失的字段...

mysql -u root -p -e "source add_columns_simple.sql"

echo SQL脚本执行完成！
pause 