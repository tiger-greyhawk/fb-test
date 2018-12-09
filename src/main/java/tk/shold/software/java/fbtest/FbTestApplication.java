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
	private static int screenWidth = 1024;
	private static int screenHeight = 768;

	public static void main(String[] args) {
		SpringApplication.run(FbTestApplication.class, args);
		//System.setProperty("java.library.path", "c:\\_src\\_java\\fb-test\\src\\main\\sdljava\\lib");
		System.setProperty("java.library.path", "/home/kovalchuk/presentation/sdljava_linux/lib");
		DisplayableSurface displayableSurface = new DisplayableSurface(screenWidth, screenHeight);


		// -Djava.library.path=c:\_src\_java\fb-test\src\main\sdljava\lib
		//System.loadLibrary("SDL");
		//System.loadLibrary("SDL_image");
		//System.loadLibrary("SDLJava");
		//System.loadLibrary("SDLJava_image");
        //System.loadLibrary("libsdljava");
        //System.loadLibrary("libsdljava_image");
		//s04_p01 p = new s04_p01();



		//SDLMain sdlMain = new SDLMain();

		try {
			//SDLMain.getSDLVersion();
			//System.loadLibrary("SDLJava_image");
			//int height = SDLImage.load("c:\\delete\\destDir\\Fest\\4\\4-0.png").getHeight();
			//for (int i = 0; i<100; i++) System.out.println("test param");
			SDLMain.init(SDLMain.SDL_INIT_VIDEO);
            log.info("init video done");
			//SDLMain.initSubSystem(SDLMain.SDL_INIT_VIDEO);
			//sdlMain.init(SDLMain.SDL_INIT_VIDEO);
			//SDLSurface screen = SDLVideo.setVideoMode(640, 480, 0, 0 );
			/*try {
				log.warning("before getVideoSurface");
				SDLSurface mode = SDLVideo.getVideoSurface();
				log.warning("after getVideoSurface");
				System.out.println(mode.getHeight());
				System.out.println(mode.getWidth());
			}
			catch (Exception e) {
				e.getMessage();
			}*/


			//SDLSurface screen = SDLVideo.setVideoMode(screenWidth,screenHeight, 24, SDLVideo.SDL_SWSURFACE | SDLVideo.SDL_ANYFORMAT);
            SDLSurface screen = SDLVideo.setVideoMode(screenWidth,screenHeight, 24, SDLVideo.SDL_SWSURFACE);

			//SDLSurface screen = SDLVideo.getVideoSurface();
			//SDLSurface screen = SDLVideo.setVideoMode(screenWidth, screenHeight, 24, SDLVideo.SDL_HWSURFACE | SDLVideo.SDL_DOUBLEBUF);
			//SDLSurface screen = SDLVideo.getVideoSurface();





			//SDLSurface image2 = SDLVideo.loadBMP("/home/kovalchuk/presentation/ball.bmp");
			//SDLSurface image = SDLImage.load("/home/kovalchuk/presentation/5.png");
			//double zoomWidth = (float)screenWidth/(float)image.getWidth();
			//double zoomHeight = (float)screenHeight/(float)image.getHeight();
			//double zoom;
			//if (zoomWidth < zoomHeight) zoom = zoomWidth; else zoom = zoomHeight;
			//System.out.println("zoom =  " + zoom);
			//SDLSurface image4 = SDLGfx.rotozoomSurface(image, 0, zoom, true);
			//System.out.println(image4.getHeight());
			//System.out.println(image4.getWidth());


/*
			if (image.blitSurface(new SDLRect(0,0, image.getHeight(), image.getWidth()),
					screen,
					new SDLRect(0,0, screen.getHeight(), screen.getWidth())) == -1)
				log.warning("Blit did now work");
*/
            log.info("init screen done");
			for (int i = 0; i<30; i++)
			{
				screen.fillRect(3655L);
                log.info("clear screen done");
				SDLSurface original2 = SDLImage.load("/home/kovalchuk/presentation/ball.bmp");
                log.info("image load done");
				SDLSurface original3 = SDLImage.load("/home/kovalchuk/presentation/5.png");
				SDLSurface image2 = displayableSurface.getPreparedImage(original2);
                log.info("zoom done");
				SDLSurface image3 = displayableSurface.getPreparedImage(original3);
				//int[] coor = getStartCoordinates(image2);
				if (image2.blitSurface(screen, displayableSurface.getStartCoordinates(image2)) == -1)
					log.warning("Blit did now work");
				//log.info("" + coor[0] + " - " + coor[1]);
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
				//if (image3.blitSurface(screen, new SDLRect(10, 10, 500, 400)) == -1)
                if (image3.blitSurface(screen, displayableSurface.getStartCoordinates(image3)) == -1)
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
			//log.warning(""+image.displayFormat().getHeight());
			//image.freeSurface();
			screen.freeSurface();

			//image4.freeSurface();
			//SDLMain.quitSubSystem(SDLMain.SDL_INIT_VIDEO);


			//log.info(""+height);
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
