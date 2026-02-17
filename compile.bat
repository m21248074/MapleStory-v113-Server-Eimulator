@echo off
title TMS113 核心編譯器

set "JAVA_HOME=C:\Program Files\Java\jdk1.8.0_172"

if exist "%JAVA_HOME%\bin\javac.exe" (
    set "JAVAC=%JAVA_HOME%\bin\javac.exe"
    set "JAR=%JAVA_HOME%\bin\jar.exe"
) else (
    echo [錯誤] 找不到 JDK! 路徑: %JAVA_HOME%
    pause
    exit
)

echo [1/3] 正在清理舊的編譯檔案 (build)...
if exist build rd /s /q build
mkdir build

echo [2/3] 正在搜尋所有原始碼檔案...
:: 關鍵步驟：搜尋 src 內所有 .java 檔並存入 sources.txt
dir /s /b src\*.java > sources.txt

echo 正在編譯原始碼 (遇到錯誤將停止)...
"%JAVAC%" -g -encoding UTF-8 -d build -cp "dist/*" @sources.txt || (echo [編譯失敗] 發現錯誤，停止作業。 & pause & exit /b 1)

:: 編譯完後刪除暫存清單
del sources.txt

:: --- 新增步驟：把 FXML 和其他資源檔案複製到 build 目錄 ---
echo [2.5/3] 正在同步資源檔案 (FXML, CSS, Properties)...
:: /S 包含子目錄, /I 如果目的地不存在則建立, /Y 覆蓋不詢問
xcopy "src\*.fxml" "build\" /S /Y /I >nul
xcopy "src\*.css" "build\" /S /Y /I >nul
xcopy "src\image\*" "build\image\" /S /Y /I >nul
:: ------------------------------------------------------

echo [3/3] 正在打包成新的 JAR 檔...
"%JAR%" cvfe dist/MapleStory.jar server.Start -C build .

echo.
echo ==========================================
echo 編譯成功! 產出檔案: dist/MapleStory.jar
echo ==========================================
pause