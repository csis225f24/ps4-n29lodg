import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    JButton start;
    JButton clear;

    //Adjusts length of time between redraws
    JSlider timeBuffer;

    //Frame to display, panel to draw koch curves on
    JPanel panel;

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

                if(clickNum == 2){
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

        //display frame and panel
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void mousePressed(MouseEvent e){
        // if(e.getSource() == start){
        //     //do thing
        // }else if(e.getSource() == clear){
        //     panel.repaint();
        // }else{
        //     clickNum++;
        //     click = e.getPoint();
        // }
        if(clickNum == 0){
            clickNum=1;
            click = e.getPoint();
        }else{
            clickNum = 0;
        }
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