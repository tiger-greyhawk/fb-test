package tk.shold.software.java.fbtest;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sdljava.SDLException;
import sdljava.SDLMain;
import sdljava.image.SDLImage;

@SpringBootApplication
@Log
public class FbTestApplication {

	public static void main(String[] args) {
		System.setProperty("java.library.path", "c:\\_src\\_java\\fb-test\\src\\main\\sdljava\\lib");
		// -Djava.library.path=c:\_src\_java\fb-test\src\main\sdljava\lib
		System.loadLibrary("SDL");
		System.loadLibrary("SDL_image");
		System.loadLibrary("SDLJava");
		System.loadLibrary("SDLJava_image");

		try {
			//SDLMain.getSDLVersion();
			//System.loadLibrary("SDLJava_image");
			int height = SDLImage.load("c:\\delete\\destDir\\Fest\\4\\4-0.png").getHeight();

			log.info(""+height);
		}
		catch (SDLException e)
		{
			log.warning(e.getMessage());
		}
		SpringApplication.run(FbTestApplication.class, args);


	}
}
