@ECHO OFF

if "%JAVA_HOME%" == "" (
	ECHO JAVA_HOME is not defined. Please set the environment variable JAVA_HOME to a JDK
	GOTO end
)

if NOT EXIST "%JAVA_HOME%\bin\javac.exe" (
	ECHO JAVA_HOME is not a valid JDK. Please install a JDK and set JAVA_HOME to it
	ECHO.
	ECHO JAVA_HOME is currently set to %JAVA_HOME%
	GOTO end
)

"%JAVA_HOME%\bin\java.exe" -version
ECHO.
"%JAVA_HOME%\bin\javac.exe" -version

ECHO.
ECHO Everything seems fine

:end
pause