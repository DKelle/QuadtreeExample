import java.util.concurrent.ThreadLocalRandom;
import java.awt.Color;

public class Square
{
    int x;
    int y;

    private int maxx;
    private int maxy;

    private int velx;
    private int vely;

    private Color c;

    int WIDTH = 10;
    int HEIGHT = 10;

    public Square(int screenWidth, int screenHeight)
    {
        maxx = screenWidth;
        maxy = screenHeight;

        x = ThreadLocalRandom.current().nextInt(0, screenWidth + 1);
        y = ThreadLocalRandom.current().nextInt(0, screenHeight + 1);

        velx = ThreadLocalRandom.current().nextInt(1, 10);
        vely = ThreadLocalRandom.current().nextInt(1, 10);

        int r = ThreadLocalRandom.current().nextInt(1, 256);
        int g = ThreadLocalRandom.current().nextInt(1, 256);
        int b = ThreadLocalRandom.current().nextInt(1, 256);
        this.c = new Color(r, g, b);
    }

    public void update()
    {
        if((x + velx > maxx) || (x + velx < 0))
            this.reflect(true, false);

        if((y + vely > maxy)||(y + vely < 0))
            this.reflect(false, true);

        x += velx;
        y += vely;
    }

    public boolean intersect(Square other)
    {
        boolean intersectX = false;
        boolean intersectY = false;

        //Check to see if they intersect in X axis

        if(this.x > other.x && this.x < (other.x+other.WIDTH))
            intersectX = true;
        if(other.x > this.x && other.x < (this.x+this.WIDTH))
            intersectX = true;

        if(this.y > other.y && this.y < (other.y+other.HEIGHT))
            intersectY = true;
        if(other.y > this.y && other.y < (this.y+this.HEIGHT))
            intersectY = true;


        if(intersectX && intersectY)
        {
            //If we are colliding, just reverse in both directions.
            //No need for real physics
            this.reflect(true, true);
            other.reflect(true, true);
            this.manipulateVelocities();
        }

        return true;
    }

    public void manipulateVelocities()
    {
        int deltax = ThreadLocalRandom.current().nextInt(-4, 5);
        int deltay = ThreadLocalRandom.current().nextInt(-4, 5);

        this.velx += deltax;
        this.vely += deltay;
    }

    public void reflect(boolean x, boolean y)
    {
        //Should we reverse in the X direction?
        if(x)
        {
            this.velx = -this.velx;
        }

        //Should we reverse in the Y direction?
        if(y)
        {
            this.vely = -this.vely;
        }
    }

    public Color getColor()
    {
        return this.c;
    }

    public void setColor(Color c)
    {
        this.c = c;
    }

    public String toString()
    {
        return "(x,y) : (" + this.x + "," + this.y + ")";
    }
}
