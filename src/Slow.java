import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.*;

public class Slow extends JComponent
{
    private int WIDTH, HEIGHT;
    private int squares = 10000;
    private Square[] squareArr = new Square[squares];
    private int x = 0;

    private int timesteps = 0;

    private long starttime;
    private long currenttime;
    private long timeInSeconds;
    private long fps;


    public static void main(String[] args)
    {
        System.out.println("Hello world");
        new Slow();
    }

    public Slow()
    {
        Dimension d = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        WIDTH = (int)d.getWidth();
        HEIGHT = (int)d.getHeight();
        initJFrame();
        initSquare();
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

        for(Square s : squareArr)
            s.update();

        //begin detecting collisions
        for(int i = 0; i < squareArr.length; i++)
        {
            for(int j = i+1; j < squareArr.length; j++)
            {
                //intersect squares i and j
                Square sqr = squareArr[i];
                Square sqr1 = squareArr[j];
                sqr.intersect(sqr1);
            }
        }


    }

}
