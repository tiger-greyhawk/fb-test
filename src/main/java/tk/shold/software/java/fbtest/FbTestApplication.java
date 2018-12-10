package tk.shold.software.java.fbtest;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sdljava.SDLException;
import sdljava.SDLMain;
import sdljava.image.SDLImage;
import sdljava.video.*;
import sdljava.x.swig.SWIG_SDLImage;
import sdljavax.gfx.SDLGfx;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SpringBootApplication
@Log
public class FbTestApplication {

	//protected static SDLSurface screen;
	private static int screenWidth = 640;
	private static int screenHeight = 480;

	public static void main(String[] args) {
		//SpringApplication.run(FbTestApplication.class, args);
		//System.setProperty("java.library.path", "c:\\_src\\_java\\fb-test\\src\\main\\sdljava\\lib");
		System.loadLibrary("SDL");
		System.loadLibrary("SDL_image");
		System.loadLibrary("SDLJava");
		System.loadLibrary("SDLJava_image");
		//System.loadLibrary("SDLJava_gfx");
		//System.setProperty("java.library.path", "/home/kovalchuk/presentation/sdljava_linux/lib");
		DisplayableSurface displayableSurface = new DisplayableSurface(screenWidth, screenHeight);
		// -Djava.library.path=c:\_src\_java\fb-test\src\main\sdljava\lib
		try {
			SDLMain.init(SDLMain.SDL_INIT_VIDEO);
            log.info("init video done");




            SDLSurface screen = SDLVideo.setVideoMode(screenWidth,screenHeight, 24, SDLVideo.SDL_SWSURFACE);

            log.info("init screen done");
			for (int i = 0; i<30; i++)
			{
				screen.fillRect(0L);
                log.info("clear screen done");
				//SDLSurface original2 = SDLImage.load("/home/kovalchuk/presentation/ball.bmp");
				SDLSurface original2 = SDLImage.load("./src/main/resources/ball.bmp");
                log.info("image load done");
				//SDLSurface original3 = SDLImage.load("/home/kovalchuk/presentation/5.png");
				SDLSurface original3 = SDLImage.load("./src/main/resources/ava.png");
				SDLSurface image2 = displayableSurface.getPreparedImage(original2);
                log.info("zoom done");
				SDLSurface image3 = displayableSurface.getPreparedImage(original3);
				if (image2.blitSurface(screen) == -1)
				//if (image2.blitSurface(screen, displayableSurface.getStartCoordinates(image2)) == -1)
					log.warning("Blit did now work");
				screen.flip();
				//screen.saveBMP("/home/kovalchuk/presentation/6.bmp");

				try
				{
					Thread.sleep(1000L);
				} catch (InterruptedException e)
				{
					System.out.println(e);
				}
				screen.fillRect(0);
				if (image3.blitSurface(screen) == -1)
                //if (image3.blitSurface(screen, displayableSurface.getStartCoordinates(image3)) == -1)
					log.warning("Blit did now work");

				screen.flip();

				try
				{
					Thread.sleep(1000L);
				} catch (InterruptedException e)
				{
					System.out.println(e);
				}
				original2.freeSurface();
				original3.freeSurface();
				image2.freeSurface();
				image3.freeSurface();
			}
			screen.freeSurface();
		}
		catch (SDLException e)
		{
			log.warning(e.getMessage());
		}
		finally {
			SDLMain.quit();
		}
		log.info("complited");

	}


}
