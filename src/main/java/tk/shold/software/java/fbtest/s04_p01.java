package tk.shold.software.java.fbtest;

// Workshop 4 part 1

import sdljava.*;
import sdljava.video.*;
import sdljava.event.*;

// This is a general purpose vector class
class c_vector
{

    // Constructor that takes another vector as input
    public c_vector( c_vector value )
    {
        x = value.x;
        y = value.y;
    }

    // Constructor that takes two floats as input
    public c_vector( float a, float b )
    {
        x = a;
        y = b;
    }

    // Constructor that takes one float as input
    public c_vector( float value )
    {
        x = y = value;
    }

    // Constructor that takes no input
    public c_vector()
    {
        x = y = 0.0f;
    }

    // Normalises the vector
    public void normalise()
    {
        /***************************************
         ***************************************/

        // Retrieve the magnitude
        float len = get_length();
        if( len > 0.0f )
        {
            // Normalise the vector by dividing the components by this magnitude
            x /= len;
            y /= len;
        }
        else	// This should stop divide by zero errors
            x = y = 0.0f;

        /***************************************
         ***************************************/
    }

    // Returns the length of the vector
    public float get_length()
    {
        /***************************************
         ***************************************/

        // Return the magnitude (length) using Pythagoras' theorem
        return (float) Math.sqrt( (x*x) + (y*y) );

        /***************************************
         ***************************************/
    }

    // Our vector's components
    public float x, y;
}

// A ball class
class ball
{
    // Stores the ball image
    protected static SDLSurface image;

    // Initialises the ball class
    public static void init() throws SDLException
    {
        /***************************************
         ***************************************/

        // Load the ball image
        image = SDLVideo.loadBMP("/home/zabbixadmin/presentation/ball.bmp");

        /***************************************
         ***************************************/
    }

    // The ball constructor, places the ball at the given coordinates, moving in the given direction, and of the given radius
    public ball( c_vector pos, float direction_angle, float input_radius )
    {
        // Set initial values
        max_speed = 500.0f;
        position = new c_vector(pos);
        radius = input_radius;


        // In this class we store the speed in a vector as its x and y speed components


        /***************************************
         ***************************************/

        // Need to set the speed of the ball using the direction angle and the max_speed:

        float x_component = max_speed * (float) Math.sin(direction_angle);
        float y_component = -max_speed * (float) Math.cos(direction_angle);

        speed = new c_vector(x_component,y_component);

        /***************************************
         ***************************************/

    }

    // Makes the ball bounce in the horizontal (i.e. off the left or right)
    public void bounce_horizontal()
    {
        // Need to reverse the horizontal direction
        speed.x = -speed.x;
    }

    // Makes the ball bounce in the vertical (i.e. off the floor / ceiling)
    public void bounce_vertical()
    {
        // Need to reverse the vertical direction
        speed.y = -speed.y;
    }

    // Updates the ball's position
    public void update( float deltaT_s )
    {
        // Update our position
        position.x +=  deltaT_s * speed.x;
        position.y +=  deltaT_s * speed.y;


        // Make it bounce off the walls:

        if( position.x  < 0.0f+radius )
        {	// Left wall
            bounce_horizontal();
            position.x = 0.0f+radius;
        }
        else if( position.x > 640.0f-radius )
        {	// Right wall
            bounce_horizontal();
            position.x = 640.0f-radius;
        }

        if( position.y  < 0.0f+radius )
        {	// Top wall
            bounce_vertical();
            position.y = 0.0f+radius;
        }
        else if( position.y > 480.0f-radius )
        {	// Bottom wall
            bounce_vertical();
            position.y = 480.0f-radius;
        }
    }

    // Draws the ball
    public void draw(SDLSurface screen) throws SDLException
    {
        SDLRect rectangle = new SDLRect((int)position.x-(int)radius,(int)position.y-(int)radius,(int)(2*radius),(int)(2*radius));
        //screen.fillRect(rectangle,screen.mapRGB(255,0,0));
        // To draw the ball's image, we need to use this function:
        // It does a blit operation from the image surface to the screen surface, using the rectangle's position
        image.blitSurface(screen,rectangle);
    }

    // For retrieving the ball's position
    public c_vector get_position()
    {
        // We don't want anyone else to be able to modify the position, we we will make a copy to send
        return new c_vector(position);
    }

    // Retrieves the radius of the ball
    public float get_radius()
    {
        return radius;
    }

    // The maximum speed at which it can travel
    protected float max_speed;
    // The current speed of the ball
    protected c_vector speed;

    // The current position
    protected c_vector position;

    // The radius of our ball
    protected float radius;
}

// A paddle class (a moveable rectangle that the ball can bounce off of)
class paddle
{
    // Need to set the 'playing field' dimensions of where we want this paddle to be allowed to move.
    // This could be useful if we want two players, to keep them to their own court.
    public paddle( c_vector pos, int window_width, int window_height )
    {
        aim = new c_vector(pos);
        position = new c_vector(pos);
        direction = new c_vector(0.0f);
        max_speed = 500.0f;
        width = 30;
        height = 10;
        field_width = window_width;
        field_height = window_height;

        // To demonstrate an alternative method of storing speed,
        // in this class we store the speed as a scalar speed value (max_speed)
        // and the direction in a normalised vector.
        // By storing it this way, we have separated the actual speed from the direction.

    }

    // Called when we need to generate the ball
    public ball release( float direction_angle, float radius )
    {
        // Create a new ball.
        // As position, we give it our position with an offset due to our height and the radius of the ball
        ball our_ball = new ball(new c_vector(position.x,position.y-(height/2.0f)-radius),direction_angle, radius);
        return our_ball;
    }

    // Gives the paddle a coordinate to move towards
    public void set_position_aim( c_vector aim_input_pos )
    {
        // Our input vector becomes our new aim vector
        aim = aim_input_pos;

        // Work out the new direction:
        // This will be the direction from our current position to the input position
        direction.x = aim_input_pos.x - position.x;
        direction.y = aim_input_pos.y - position.y;

        // We are storing the speed separately (as max_speed)
        // so normalise the direction vector
        direction.normalise();

    }

    // This function checks whether a ball collides with the paddle, and makes it bounce off
    public void check_ball_collision( ball input_ball )
    {
        // Retrieve information about the ball
        c_vector ball_pos = input_ball.get_position();
        float ball_radius = input_ball.get_radius();

        // We are going to perform a rectangle - rectangle collision test.
        // This is not actually correct, as the ball is really a circle, but it is quick and easy, and good enough for our uses.
        // Plus, because the ball is moving quite quickly, the user will probably never even notice!
        // To do this, we will perform four tests.

        // First, test the left edge of the paddle against the right edge of the ball
        // Then test the right edge of the paddle against the left edge of the ball
        if( position.x - (width/2) < ball_pos.x + ball_radius   &&   position.x + (width/2) > ball_pos.x - ball_radius )
        {
            // If these tests pass, it means that the ball is at least partially vertically aligned with the paddle
            // We now need to see if it is also horizontally aligned.
            // If it is, we will have a collision

            // First, test the top edge of the paddle against the bottom edge of the ball
            // Then test the bottom edge of the paddle against the top edge of the ball
            if( position.y - (height/2) < ball_pos.y + ball_radius   &&   position.y + (height/2) > ball_pos.y - ball_radius )
            {
                // The ball is partially aligned with the paddle both horizontally and vertically.
                // This means that the two rectangles actually intersect - i.e. there has been a collision.
                // Now we need to "resolve" it (decide what to do).
                // For the purposes of this demo, we're not going to do anything fancy or even correct.
                // We know that the paddle is wider than it is long, so statistically the ball is more likely to be hitting the long side.
                // Therefore, we'll just assume that it is hitting the paddle in this way and tell the ball to perform an vertical bounce:
                input_ball.bounce_vertical();
            }
        }
    }

    // Updates the paddle's position
    public void update( float deltaT_s )
    {

        // Update our position
        if( position.x < aim.x - 2 || position.x > aim.x + 2)
            position.x += deltaT_s * direction.x * max_speed;
        if( position.y < aim.y - 2 || position.y > aim.y + 2 )
            position.y += deltaT_s * direction.y * max_speed;

        // Stop the paddle if it tries to go off the playing field
        if( position.x - (width/2) < 0.0f )
            position.x = width/2;
        else if( position.x + (width/2) > field_width )
            position.x = field_width - (width/2);

        if( position.y - (height/2) < 0.0f )
            position.y = height/2;
        else if( position.y + (height/2) > field_height )
            position.y = field_height - (height/2);
    }

    // Draws the paddle
    public void draw(SDLSurface screen) throws SDLException
    {
        // Draw the paddle
        SDLRect rectangle = new SDLRect((int)position.x-(width/2),(int)position.y-(height/2),30,10);
        screen.fillRect(rectangle,screen.mapRGB(255,255,255));

    }

    // The maximum speed at which it can travel
    protected float max_speed;

    // The normalised direction vector that the paddle is travelling in
    protected c_vector direction;

    // The current position
    protected c_vector position;

    // The position that the paddle is heading towards
    protected c_vector aim;

    // Dimensions of the paddle
    protected int width, height;

    // Dimensions of our 'playing field' (i.e. the window size)
    protected int field_width, field_height;
}

class s04_p01
{
    // Our SDL surface for drawing on
    static SDLSurface screen;

    // The speed of our object
    static ball our_ball;
    static paddle our_paddle;



    // We will call this function whenever we want to exit the program.
    // It uninitialises SDL and forces a program exit.
    public static void exit(int value)
    {
        SDLMain.quit();
        System.out.println("Exiting..");
        System.exit(value);
    }

    // In this function, we retrieve and handle events.
    // The function returns true if the user wishes to exit the program.
    public static boolean handle_events()
    {
        try
        {
            // SDL stores events in a queue.
            // To look at events we have two options:
            //  1. We can wait for events, in which case we call a function that will not return until there is an event.
            //  2. We can have a quick look at the event queue, using a function that will retrieve an event if there is one, returning null if there isn't.
            //
            // In an interactive game, it's not much use if we get stuck waiting for events, as we won't be able to draw or update the scene.
            // Therefore, we want to use the 'quick look' option.
            // This is called polling for events.
            // The waiting technique is known as blocking.
            // Both methods remove the oldest event from the queue and give it to us to decide what to do with it.

            // Poll for events
            SDLEvent event = SDLEvent.pollEvent();

            // If there was an event..
            if( event != null )
                // What we do depends on the type
                switch( event.getType() )
                {
                    case SDLEvent.SDL_QUIT:
                        // This event type is generated when the user clicks on the 'x' to close the window.
                        // We want to exit our handle_events function, returning true to indicate that the user wishes to quit.
                        return true;

                    case SDLEvent.SDL_KEYDOWN: // The user has pressed a key
                        break;

                    case SDLEvent.SDL_KEYUP: // The user has released a key
                        break;

                    case SDLEvent.SDL_MOUSEBUTTONDOWN: // The user has pressed a mouse button
                        /***************************************
                         ***************************************/

                        // Set release angle to be random - don't forget it's in radians
                        our_ball = our_paddle.release((float)(Math.random()*2.0f - 1.0f)*(3.141592653589793238462643f),5.0f);

                        /***************************************
                         ***************************************/
                        break;

                    case SDLEvent.SDL_MOUSEBUTTONUP: // The user has released a mouse button
                        break;

                    case SDLEvent.SDL_MOUSEMOTION:	// The mouse has moved
                        // This event is generated whenever the mouse moves in the window
                        // (it gets called a lot!)
                        // Retrieve the position of the mouse:
                        float x = (float) ((SDLMouseMotionEvent) event).getX();
                        float y = (float) ((SDLMouseMotionEvent) event).getY();
                        // We want the paddle to move towards this position:
                        our_paddle.set_position_aim(new c_vector(x,y));
                        break;
                }

        }
        catch(SDLException e)
        {
            System.err.println("Error while polling event : " + SDLMain.getError());
            exit(1);
        }

        return false;

    }

    // In this function, we update our scene
    public static void update( float deltaT_s )
    {
        // Update our paddle
        our_paddle.update(deltaT_s);


        // If we have a ball..
        if( our_ball != null )
        {
            // Update it
            our_ball.update(deltaT_s);

            // And check to see if it collides with our paddle
            our_paddle.check_ball_collision(our_ball);
        }
    }

    // In this function, we draw our scene
    // Upon encountering an error, this function throws an SDLException, which is passed on from SDL function calls
    public static void draw() throws sdljava.SDLException
    {
        // First, we clear the screen
        screen.fillRect(screen.mapRGB(0,0,0));

        our_paddle.draw(screen);

        if( our_ball != null )
            our_ball.draw(screen);

        screen.flip();
    }


    public s04_p01()
    {
        // Create a paddle:
        //public ball_example() {
        our_paddle = new paddle(new c_vector(320.0f, 240.0f), 640, 480);


        // Create an SDL object
        SDLMain sdl = new SDLMain();

        // SDL uses exceptions when it encounters problems
        try {
            // Initialise SDL to use its video (graphics) capabilities
            sdl.init(SDLMain.SDL_INIT_VIDEO);
            // SDL uses what it calls a 'surface' as a window.
            screen = SDLVideo.setVideoMode(640, 480, 0, 0);

            // Initialise our objects
            ball.init();


            // Timing is one of the most crucial aspects of an interactive game-like program.
            // Since the graphics on screen can take a variable amount of time to draw,
            // we cannot rely on a constant frame rate. Besides, if we run our program on a
            // faster or slower machine, it will be different again.
            // We must therefore calculate the amount of time that has elapsed between each
            // iteration of our game loop, and use this when we update things.
            long previous_time = 0, current_time = 0, deltaT = 0;
            float deltaT_s = 0.0f;

            current_time = System.currentTimeMillis();
            previous_time = current_time;

            // This will store whether or not the user has asked to quit
            boolean quit = false;

            // This is our main "game loop", and we will run it
            // while we've not been asked to quit...
            while (quit == false) {

                // When the user presses a key, or uses the mouse, that input is stored in SDL as an 'event'.
                // As an interactive program, we need to look at these events and decide what to do about them.
                // Our function here also returns true if we want to quit:
                quit = handle_events();

                // In an interactive program, be it a game or whatever, we usually have a variety of things to update each frame.
                // These could be character animations, physics simulations, explosions, whatever we want...
                update(deltaT_s);

                // Finally, we need to draw our scene
                draw();

                // Update our current time
                current_time = System.currentTimeMillis();
                // Work out the frame rate for that frame
                deltaT_s = (float) (current_time - previous_time) / 1000.0f;
                // For the next frame, our current time will the the previous one
                previous_time = current_time;

            }

            exit(0);

        } catch (SDLException e) // Catch SDL problems
        {
            // We shall handle any problems by printing a stack trace:
            e.printStackTrace();
            // and an explanation if that wasn't enough
            System.err.println("SDL encountered a problem");
            exit(1);
        }


    }
}
