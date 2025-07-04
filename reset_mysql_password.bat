@echo off
chcp 65001
echo ========================================
echo MySQL密码重置工具
echo ========================================
echo.

echo 正在检查MySQL服务状态...
sc query mysql >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ MySQL服务未安装或未找到
    echo 请确保MySQL已正确安装
    pause
    exit /b 1
)

echo ✅ MySQL服务已安装
echo.

echo 请选择操作：
echo 1. 停止MySQL服务
echo 2. 启动MySQL服务
echo 3. 重置root密码
echo 4. 测试连接
echo 5. 退出
echo.

set /p choice=请输入选择 (1-5): 

if "%choice%"=="1" goto stop_mysql
if "%choice%"=="2" goto start_mysql
if "%choice%"=="3" goto reset_password
if "%choice%"=="4" goto test_connection
if "%choice%"=="5" goto exit
goto invalid_choice

:stop_mysql
echo.
echo 正在停止MySQL服务...
net stop mysql
if %errorlevel% equ 0 (
    echo ✅ MySQL服务已停止
) else (
    echo ❌ 停止MySQL服务失败
)
pause
goto menu

:start_mysql
echo.
echo 正在启动MySQL服务...
net start mysql
if %errorlevel% equ 0 (
    echo ✅ MySQL服务已启动
) else (
    echo ❌ 启动MySQL服务失败
)
pause
goto menu

:reset_password
echo.
echo ========================================
echo 重置MySQL root密码
echo ========================================
echo.
echo 注意：此操作将重置root用户密码为 'fht041029'
echo.
set /p confirm=确认继续吗？(y/n): 
if /i not "%confirm%"=="y" goto menu

echo.
echo 步骤1: 停止MySQL服务
net stop mysql
if %errorlevel% neq 0 (
    echo ❌ 无法停止MySQL服务，请手动停止
    pause
    goto menu
)

echo.
echo 步骤2: 以安全模式启动MySQL
echo 请手动执行以下命令：
echo mysqld --skip-grant-tables --user=mysql
echo.
echo 然后在新的命令行窗口中执行：
echo mysql -u root
echo.
echo 在MySQL命令行中执行：
echo USE mysql;
echo ALTER USER 'root'@'localhost' IDENTIFIED BY 'fht041029';
echo FLUSH PRIVILEGES;
echo EXIT;
echo.
echo 完成后按任意键继续...
pause

echo.
echo 步骤3: 重启MySQL服务
net start mysql
if %errorlevel% equ 0 (
    echo ✅ MySQL服务已重启
) else (
    echo ❌ 重启MySQL服务失败
)

echo.
echo 步骤4: 测试新密码
echo 正在测试连接...
mysql -u root -pfht041029 -e "SELECT 'Connection successful' as status;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ 密码重置成功！
    echo 新密码: fht041029
) else (
    echo ❌ 密码重置失败，请检查错误信息
)

pause
goto menu

:test_connection
echo.
echo 测试MySQL连接...
echo 尝试使用默认密码连接...
mysql -u root -p -e "SELECT 'Connection successful' as status;" 2>nul
if %errorlevel% equ 0 (
    echo ✅ 连接成功！
) else (
    echo ❌ 连接失败
    echo.
    echo 尝试使用重置后的密码连接...
    mysql -u root -pfht041029 -e "SELECT 'Connection successful' as status;" 2>nul
    if %errorlevel% equ 0 (
        echo ✅ 使用新密码连接成功！
    ) else (
        echo ❌ 连接仍然失败
        echo 请检查MySQL服务状态和密码设置
    )
)
pause
goto menu

:invalid_choice
echo.
echo ❌ 无效选择，请重新输入
pause
goto menu

:menu
cls
echo ========================================
echo MySQL密码重置工具
echo ========================================
echo.
echo 请选择操作：
echo 1. 停止MySQL服务
echo 2. 启动MySQL服务
echo 3. 重置root密码
echo 4. 测试连接
echo 5. 退出
echo.
set /p choice=请输入选择 (1-5): 

if "%choice%"=="1" goto stop_mysql
if "%choice%"=="2" goto start_mysql
if "%choice%"=="3" goto reset_password
if "%choice%"=="4" goto test_connection
if "%choice%"=="5" goto exit
goto invalid_choice

:exit
echo.
echo 感谢使用！
pause 