import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

import java.util.ArrayList;

public class Fast extends JComponent
{
    private int WIDTH, HEIGHT;
    private int squares = 10000;
    private int x = 0;
    private int timesteps = 0;
    private int numQuads = 4;

    private boolean useQuadColors = true;

    private long starttime;
    private long currenttime;
    private long timeInSeconds;
    private long fps;

    public static ArrayList<Square>[][] boundingBoxes = new ArrayList[4][4];
    private Color[][] quadColors = new Color[numQuads][numQuads];
    private Square[] squareArr = new Square[squares];


    public static void main(String[] args)
    {
        System.out.println("Hello world");
        new Fast();
    }

    public Fast()
    {
        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = (int)d.getWidth();
        HEIGHT = (int)d.getHeight();
        quadColors = new Color[numQuads][numQuads];
        initSquare();
        initBoundingBox();
        initJFrame();
        if(useQuadColors)
            initQuadColors();
        starttime = System.currentTimeMillis();

    }


    /**
     *      Initializes the JFrame
     *      Includes setting up the mouseMotionListener, and a timer that will run repaint on a 60 m
     *
     */
    public void initJFrame(){
        //Set up the JFrame
        JFrame j = new JFrame();
        j.setSize(WIDTH, HEIGHT);
        j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Make sure we recognize mouse movements
        this.addMouseMotionListener(new MouseMotionListener(){
             public void mouseMoved(MouseEvent e){

             }
             public void mouseDragged(MouseEvent e){

             }
        });

        //Repaint happens every 60 milliseconds. This may not be neccessary
        //We maybe want repaint to run only after new pixel colors have been determined
        Timer t = new Timer(60, new ActionListener(){
                public void actionPerformed(ActionEvent e){
                        repaint();
                }
        });

        t.start();


        j.add(this);

        j.setVisible(true);


    }

    public void initSquare(){
        for(int i = 0; i < squares; i ++)
        {
            Square s = new Square(WIDTH, HEIGHT);
            squareArr[i] = s;
        }

    }

    public void initBoundingBox()
    {

        for(int i = 0; i < boundingBoxes.length; i++) {
            for(int j = 0; j < boundingBoxes[i].length; j++) {
                 boundingBoxes[i][j] = new ArrayList<Square>();
            }
        }
    }

    public void initQuadColors()
    {
        for(int i = 0; i < quadColors.length; i++) {
            for(int j = 0; j < quadColors[i].length; j++) {
                int r = ThreadLocalRandom.current().nextInt(1, 256);
                int g = ThreadLocalRandom.current().nextInt(1, 256);
                int b = ThreadLocalRandom.current().nextInt(1, 256);
                quadColors[i][j] = new Color(r,g,b);
            }
        }

    }
   public void paintComponent(Graphics g){
       super.paintComponent(g);

        update();

       for(Square s : squareArr)
       {
            g.setColor(s.getColor());
            g.fillRect(s.x, s.y, s.WIDTH, s.HEIGHT);
       }


        //Paint the FPS in the corner
        g.drawString("FPS: " + Long.toString(fps), WIDTH - 100, 100);
        g.drawString("TOTAL TIME " + Long.toString(timeInSeconds), WIDTH - 100, 130);
        g.drawString("TIMESTEPS " + Long.toString(timesteps), WIDTH - 100, 160);

        timesteps++;
   }

    public void update()
    {

        currenttime = System.currentTimeMillis();
        timeInSeconds = (currenttime - starttime) / 1000;
        fps = timesteps / timeInSeconds;

        initBoundingBox();

        for(Square s : squareArr)
            s.update();


        //begin detecting collisions
        //first break the square up into one of 16 quadrants

        for(Square s : squareArr)
        {
            //How long is each section of our 4X4 screen?
            int width = WIDTH / numQuads;
            int height = HEIGHT / numQuads;
            int xquad = s.x/width;
            int yquad = s.y/height;

            //restrict quad to values 0-3
            xquad = (xquad == numQuads) ? 3 : xquad;
            yquad = (yquad == numQuads) ? 3 : yquad;

            if(useQuadColors)
            {
                //Change this squares color to be the quadrant color
                s.setColor(quadColors[xquad][yquad]);
            }

            boundingBoxes[xquad][yquad].add(s);


        }

        //Check for collisions of square that are in the same quadrant
        for(int a = 0; a < boundingBoxes.length; a++)
        {
            for(int b = 0; b< boundingBoxes[a].length; b++)
            {
                ArrayList<Square> squareList = boundingBoxes[a][b];

                //Now that we have all the squares that are close together, check for collisisons within them
                for(int i = 0; i < squareList.size(); i++)
                {
                    for(int j = i+1; j < squareList.size(); j++)
                    {
                        //intersect squares i and j
                        Square sqr = squareList.get(i);
                        Square sqr1 = squareList.get(j);
                        sqr.intersect(sqr1);
                    }
                }

            }
        }

    }

}
