package sandbox;

import graphics.G;
import graphics.WinApp;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class RedRect extends WinApp {

    public static int nRepaint = 0;
    public static int clicks = 0;
    public static Path thepath = new Path();
    public static Pic thepic= new Pic();

    public RedRect() {super("Red Rect", 1000, 700);}


    @Override
    public void paintComponent(Graphics g){
        nRepaint ++;
        g.setColor(G.rndColor());
        g.fillOval(100,100,200, 100);
        g.drawLine(100,400,400,100);

        String msg = "Hello there!"+ clicks;
        int x= 400, y = 200;
        g.drawString(msg, x, y); //the y value is the baseline
        g.setColor(Color.RED);
        g.fillOval(x, y,2,2);

        FontMetrics fm = g.getFontMetrics();
        int a = fm.getAscent(), h = fm.getHeight(), w = fm.stringWidth(msg);
        g.drawRect(x, y - a, w, h);
        thepic.draw(g);


    }

    @Override
    public void mousePressed(MouseEvent me){
        thepath = new Path();
        thepath.add(me.getPoint());
        thepic.add(thepath);
        repaint(); //Calling the operating system to repaint it.
    }

    public void mouseDragged(MouseEvent me){
        thepath.add(me.getPoint());
        repaint();
    }

    public static void main(String [] args) {
        PANEL = new RedRect();
        WinApp.launch();

    }

    // ------------------------------------------pass-----------------------------------------------------
    public static class Path extends ArrayList<Point> {
        public void draw(Graphics g){
            for (int i = 1; i < size(); i++){
                Point p = get(i-1), n = get(i);
                g.drawLine(p.x,p.y,n.x,n.y);

            }
        }

    }

    public static class Pic extends ArrayList<Path>{
        public void draw(Graphics g){
            for (Path p: this){
                p.draw(g);
            }
        }
    }
}
