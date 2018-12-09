package tk.shold.software.java.fbtest;

import sdljava.SDLException;
import sdljava.image.SDLImage;
import sdljava.video.SDLRect;
import sdljava.video.SDLSurface;
import sdljavax.gfx.SDLGfx;

public class DisplayableSurface
{
    private final int screenWidth;
    private final int screenHeight;

    public DisplayableSurface(int screenWidth, int screenHeight)
    {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public SDLSurface getPreparedImage(String pathToImage) throws SDLException
    {
        //SDLSurface image = SDLImage.load(pathToImage);
        //return SDLGfx.rotozoomSurface(image, 0, getZoom(image), false);
        throw  new RuntimeException(" надо освобождать ресурсы: image.freeSurface()");
    }

    public SDLSurface getPreparedImage(SDLSurface image) throws SDLException
    {
        return SDLGfx.rotozoomSurface(image, 0, getZoom(image), false);
    }

    private double getZoom(SDLSurface image)
    {
        double zoomWidth = (float)screenWidth/(float)image.getWidth();
        double zoomHeight = (float)screenHeight/(float)image.getHeight();
        double zoom;
        if (zoomWidth < zoomHeight) zoom = zoomWidth; else zoom = zoomHeight;
        return zoom;
    }

    public SDLRect getStartCoordinates(SDLSurface image)
    {
        int x = 0;
        int y = 0;
        if (image.getHeight() == screenHeight)
            x = (screenWidth - image.getWidth()) / 2;
        else if (image.getWidth() == screenWidth)
            y = (screenHeight - image.getHeight()) / 2;
        return new SDLRect(x,y);
    }

}
