cd ..
set CLASSPATH=%CD%\lib;%CD%\lib\sdljava.jar;%CLASSPATH%
set PATH=%CD%\lib;%PATH%
java -cp %CLASSPATH% -Djava.library.path=%CD%\lib org.gljava.opengl.ftgl.FTGLTest
cd bin
pause