cd ..
set PATH=%CD%\lib;%PATH%
java -cp .\lib\sdljava.jar -Djava.library.path=.\lib sdljava.event.SDLEventTest
cd bin