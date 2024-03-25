import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

/**
 * This program will define a line with
 * 2 endpoint, and recursively redraw them
 * in the pattern of the Koch Curve
 * 
 * @author Nicholas Lodge
 * @version 3/18/24
 */
class KochCurves extends MouseAdapter implements Runnable{
    //Start and clear buttons
    JButton start, clear;

    //Adjusts length of time between redraws
    JSlider timeBuffer;

    //Frame to display, panel to draw koch curves on
    JPanel panel;

    //List to store line segments drawn on the panel
    ArrayList<KochLine> listOfCurrentLines = new ArrayList<KochLine>();
    ArrayList<KochLine> listOfNextLines = new ArrayList<KochLine>();

    //Line tracker used to keep track of mosdt recently drawn line.
    int lineTracker = -1;
    int clickNum = 0;
    Point click, currPos;

    public void run(){
        //Setting up frame
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Koch Curves");
        frame.setPreferredSize(new Dimension(500, 500));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        //Setting up panel with its own paint component
        panel = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                super.paintComponent(g);

                if(clickNum == 1){
                    g.drawLine(click.x, click.y, currPos.x, currPos.y);
                }
            }
        };
        start = new JButton("Start");
        clear = new JButton("Clear");
        panel.add(start);
        panel.add(clear);

        frame.add(panel);
        panel.addMouseListener(this);
        panel.addMouseListener(this);

        //display frame and panel
        frame.pack();
        frame.setVisible(true);
    }

    //advance to next sequence of koch curves,
    //clear panel,
    //or draw a Koch line segment
    @Override
    public void mousePressed(MouseEvent e){
        if(e.getSource() == start){
            //do thing
        }else if(e.getSource() == clear){
            listOfCurrentLines.clear();
            listOfNextLines.clear();
            panel.repaint();
        }else{
            if(clickNum == 0){
                clickNum = 1;
                click = e.getPoint();
            }else if(clickNum == 1){
                clickNum = 0;
                listOfCurrentLines.add(new KochLine(click, e.getPoint()));
            }
        }
        System.out.println(clickNum);
    }

    @Override
    public void mouseDragged(MouseEvent e){
        
    }

    @Override
    public void mouseReleased(MouseEvent e){

    }

    @Override
    public void mouseMoved(MouseEvent e) {

        currPos = e.getPoint();
        panel.repaint();
    }
    public static void main(String args[]) {

        javax.swing.SwingUtilities.invokeLater(new KochCurves());
    }
}

/**
 * KochLine class, used to draw initial line and 
 * stores information about line segment to advance the Koch 
 * curve when the start button is clicked
 * 
 * @author Nicholas Lodge
 * @version 3/24/24
 */
class KochLine{
    //add comment describing constant
    private static final int SHORTESTLINE = 10;

    //start and end points for initial line draw
    //middle points indicate where the new lines will be
    private Point startPoint, endPoint, middle1, middle2, middle3;

    public KochLine(Point startPoint, Point endPoint){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        middle1.x = (int)endPoint.distance(startPoint)/3;
        middle1.y = (int)endPoint.distance(startPoint)/3;
        
    }

    public void paint(Graphics g){
        g.setColor(Color.BLUE);
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }

    protected static void advanceKochLine(){

    }
}