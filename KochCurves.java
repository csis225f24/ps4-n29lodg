import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
class KochCurves extends MouseAdapter implements Runnable, ActionListener{
    //Start and clear buttons
    JButton start, clear;

    //Adjusts length of time between redraws
    JSlider timeBuffer;

    //Frame to display, panel to draw koch curves on
    JPanel panel;

    //List to store line segments drawn on the panel
    public static ArrayList<KochLine> listOfCurrentLines = new ArrayList<KochLine>();

    //Line tracker used to keep track of most recently drawn line.
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
                    panel.repaint();
                }

                for(KochLine l : listOfCurrentLines){
                    l.paint(g);
                }
            }
        };
        start = new JButton("Start");
        start.addActionListener(this);
        panel.add(start);
        clear = new JButton("Clear");
        clear.addActionListener(this);
        panel.add(clear);
        frame.add(panel);
        panel.addMouseListener(this);
        panel.addMouseMotionListener(this);

        //display frame and panel
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == start){
            KochLine.advanceKochLine(lineTracker);
            System.out.println(lineTracker);
        }else if(e.getSource() == clear){
            lineTracker = -1;
            listOfCurrentLines.clear();
            panel.repaint();
        }
    }

    //draw a Koch line segment 
    @Override
    public void mousePressed(MouseEvent e){
        
        if(clickNum == 0){
            clickNum = 1;
            click = e.getPoint();
        }else if(clickNum == 1){
            clickNum = 0;
            listOfCurrentLines.add(new KochLine(click, e.getPoint()));
            lineTracker++;
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currPos = e.getPoint();
        panel.repaint();
    }
    
    public static void main(String args[]) {

        javax.swing.SwingUtilities.invokeLater(new KochCurves());
    }

    public static ArrayList<KochLine> getList(){
        return listOfCurrentLines;
    }

    public static void setList(ArrayList<KochLine> list){
        listOfCurrentLines = list;
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
    public Point startPoint, endPoint;

    public KochLine(Point startPoint, Point endPoint){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public void paint(Graphics g){
        g.setColor(Color.BLUE);
        g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }

    
    public static Point getMiddle1(KochLine line){
        // int newCoords = (int)line.startPoint.distance(line.endPoint)/3;
        // return new Point(newCoords, newCoords);
        int newX = ((line.endPoint.x - line.startPoint.x)/3) + line.startPoint.x;
        int newY = ((line.endPoint.y - line.startPoint.y)/3) + line.startPoint.y;
        return new Point(newX, newY);
    }

    public static Point getMiddle2(KochLine line){
        // int x = (getMiddle1(line).x) + (getMiddle3(line).x);
        // x = x + (int)(Math.sqrt(getMiddle1(line).y - getMiddle3(line).y)/2);
    
        // int y = (getMiddle1(line).y) + (getMiddle3(line).y);
        // y = y + (int)(Math.sqrt(getMiddle1(line).x - getMiddle3(line).x)/2);
        int newX = (int)((getMiddle3(line).x + getMiddle1(line).x) + (Math.sqrt(3)*(getMiddle3(line).y - getMiddle1(line).y)))/2;
        int newY = (int)((getMiddle3(line).y + getMiddle1(line).y) - (Math.sqrt(3)*(getMiddle3(line).x - getMiddle1(line).x)))/2;
        return new Point(newX,newY);
    } 

    public static Point getMiddle3(KochLine line){
        // int newCoords = (int)(line.startPoint.distance(line.endPoint)/3)*2;
        // return new Point(newCoords, newCoords);
        int newX = (((line.endPoint.x - line.startPoint.x)/3)*2) + line.startPoint.x;
        int newY = (((line.endPoint.y - line.startPoint.y)/3)*2) + line.startPoint.y;
        return new Point(newX, newY);
    }

    public static void advanceKochLine(int startFrom){
        ArrayList<KochLine> listOfNextLines = new ArrayList<KochLine>();
        //start at most recently drawn line
        //for each line segment, get the 5 points needed
        ArrayList<KochLine> list = KochCurves.getList();
        for(int i = startFrom; i < list.size(); i++){
            Point a = new Point(list.get(i).startPoint);
            Point b = new Point(getMiddle1(list.get(i)));
            Point c = new Point(getMiddle2(list.get(i)));
            Point d = new Point(getMiddle3(list.get(i)));
            Point e = new Point(list.get(i).endPoint);

            //Make 4 new line segments using the 5 points we got from the previous segment
            listOfNextLines.add(new KochLine(a, b));
            listOfNextLines.add(new KochLine(b, c));
            listOfNextLines.add(new KochLine(c, d));
            listOfNextLines.add(new KochLine(d, e));
        }
        KochCurves.setList(listOfNextLines);
    }
}