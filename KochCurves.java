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
//Bonus: jslider to change wait time on panel between 1 and 10 seconds
//Bonus: paints new lines as red and all existing lines as blue

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
    static JSlider timeBuffer;
    static int waitTime;

    //Frame to display, panel to draw koch curves on
    JPanel panel;

    //List to store line segments drawn on the panel
    public static ArrayList<KochLine> listOfPreviousLines = new ArrayList<KochLine>();
    public static ArrayList<KochLine> currentLine = new ArrayList<KochLine>();
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

                for(KochLine l : listOfPreviousLines){
                    l.paint(g);
                }

                for(KochLine l : currentLine){
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
        //Initialising jslider for bonus
        timeBuffer = new JSlider(0, 10);
        panel.add(timeBuffer);
        timeBuffer.setPaintTrack(true);
        timeBuffer.setPaintTicks(true);
        timeBuffer.setPaintLabels(true);
        //display frame and panel
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource() == start){
            KochLine.advanceKochLine(currentLine);
            System.out.println(lineTracker);
        }else if(e.getSource() == clear){
            lineTracker = -1;
            listOfPreviousLines.clear();
            currentLine.clear();
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
            //line already exists, we only want to work with
            //most recently drawn line
            if(!currentLine.isEmpty()){
                for(int i = 0; i < currentLine.size(); i++){
                    listOfPreviousLines.add(currentLine.get(i));
                }
                currentLine.clear();
                //listOfPreviousLines.add(new KochLine(click, e.getPoint()));
                currentLine.add(new KochLine(click, e.getPoint(), Color.BLUE));
                clickNum = 0;
                lineTracker++;
            }else{
                clickNum = 0;
                //listOfPreviousLines.add(new KochLine(click, e.getPoint()));
                currentLine.add(new KochLine(click, e.getPoint(), Color.BLUE));
                lineTracker++;
            }
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
        return currentLine;
    }

    public static void setList(ArrayList<KochLine> list){
        currentLine = list;
    }

    public static int getTimeSlider(){
        switch (timeBuffer.getValue()) {
            case 1:
                waitTime = 1000;break;
            case 2:
                waitTime = 2000;break;
            case 3:
                waitTime = 3000;break;
            case 4:
                waitTime = 4000;break;
            case 5:
                waitTime = 5000;break;
            case 6:
                waitTime = 6000;break;
            case 7:
                waitTime = 7000;break;
            case 8:
                waitTime = 8000;break;
            case 9:
                waitTime = 9000;break;
            case 10:
                waitTime = 10000;break;
        }
        return waitTime;
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
class KochLine extends Thread{
    //add comment describing constant
    private static final int SHORTESTLINE = 10;

    //start and end points for initial line draw
    public Point startPoint, endPoint;
    
    //setting color of each line so new lines appear as red,
    //existing lines are blue
    public Color color;

    public KochLine(Point startPoint, Point endPoint, Color color){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.color = color;
    }

    public void paint(Graphics g){
        if(color.equals(Color.BLUE)){
            g.setColor(Color.BLUE);
            g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }else if(color.equals(Color.RED)){
            g.setColor(Color.RED);
            g.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        }
    }
    
    public static Point getMiddle1(KochLine line){
        int newX = ((line.endPoint.x - line.startPoint.x)/3) + line.startPoint.x;
        int newY = ((line.endPoint.y - line.startPoint.y)/3) + line.startPoint.y;
        return new Point(newX, newY);
    }

    public static Point getMiddle2(KochLine line){
        int newX = (int)((getMiddle3(line).x + getMiddle1(line).x) + (Math.sqrt(3)*(getMiddle3(line).y - getMiddle1(line).y)))/2;
        int newY = (int)((getMiddle3(line).y + getMiddle1(line).y) - (Math.sqrt(3)*(getMiddle3(line).x - getMiddle1(line).x)))/2;
        return new Point(newX,newY);
    } 

    public static Point getMiddle3(KochLine line){
        int newX = (((line.endPoint.x - line.startPoint.x)/3)*2) + line.startPoint.x;
        int newY = (((line.endPoint.y - line.startPoint.y)/3)*2) + line.startPoint.y;
        return new Point(newX, newY);
    }

    public static void advanceKochLine(ArrayList<KochLine> lineList){
        ArrayList<KochLine> listOfNextLines = new ArrayList<KochLine>();
        //start at most recently drawn line
        //for each line segment, get the 5 points needed
        //ArrayList<KochLine> list = KochCurves.getList();
        if(lineList.get(0).endPoint.distance(lineList.get(0).startPoint) <= SHORTESTLINE){
            return;
        }else{
            try {
                sleep(KochCurves.getTimeSlider());
            } catch (InterruptedException e) {
            }
            for(KochLine l : lineList){
                if(!l.color.equals(Color.RED)){
                    l.color = Color.BLUE;
                }
            }
            for(KochLine l : lineList){
                Point a = new Point(l.startPoint);
                Point b = new Point(getMiddle1(l));
                Point c = new Point(getMiddle2(l));
                Point d = new Point(getMiddle3(l));
                Point e = new Point(l.endPoint);
    
                //Make 4 new line segments using the 5 points we got from the previous segment
                listOfNextLines.add(new KochLine(a, b, Color.BLUE));
                listOfNextLines.add(new KochLine(b, c, Color.RED));
                listOfNextLines.add(new KochLine(c, d, Color.RED));
                listOfNextLines.add(new KochLine(d, e, Color.BLUE));
            }
            KochCurves.setList(listOfNextLines);
            //advanceKochLine(listOfNextLines);
        }
        
    }
}