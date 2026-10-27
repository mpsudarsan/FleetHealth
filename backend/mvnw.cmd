@echo off
@setlocal

set ERROR_CODE=0

if not "%JAVA_HOME%"=="" goto OkJHome
echo Error: JAVA_HOME not found in your environment. >&2
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto init
echo Error: JAVA_HOME is set to an invalid directory: "%JAVA_HOME%" >&2
goto error

:init
set MAVEN_PROJECTBASEDIR=%~dp0
set WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar

if not exist "%WRAPPER_JAR%" (
    echo Downloading Maven Wrapper JAR...
    powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; (New-Object Net.WebClient).DownloadFile('https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar', '%WRAPPER_JAR%')"
)

"%JAVA_HOME%\bin\java.exe" %MAVEN_OPTS% -classpath "%WRAPPER_JAR%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*

if ERRORLEVEL 1 goto error
goto end

:error
set ERROR_CODE=1

:end
@endlocal & set ERROR_CODE=%ERROR_CODE%
cmd /C exit /B %ERROR_CODE%
