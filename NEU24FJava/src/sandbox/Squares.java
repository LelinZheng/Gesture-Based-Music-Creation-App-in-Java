package sandbox;
import graphics.G;
import graphics.WinApp;
import musics.I;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Squares extends WinApp implements ActionListener {
    public static Timer timer;
    public static I.Area curArea;
    public static G.V pressLoc = new G.V(0,0);
    public static Square.List squares = new Square.List();
    public static Square lastSquare;
    public static boolean dragging = false;
    public static G.V mouseDelta = new G.V(0, 0);

    public static G.VS theVS= new G.VS(100,100,200,300);
    public static Color color = G.rndColor();
    public Squares(){
        super("Squares", 1000, 700);
        timer = new Timer(30,this);
        timer.setInitialDelay(5000);
        timer.start();

    }
    public void paintComponent(Graphics g){
//        theVS.fill(g,color);
        squares.draw(g);
    }
    public void mousePressed(MouseEvent me){
        int x = me.getX(), y = me.getY();
//        if(theVS.hit(x,y)){
//            color = G.rndColor();
//        }
        /*lastSquare = squares.hit(x,y);
        if (lastSquare == null){
            dragging = false;
            lastSquare = new Square(x,y);
            squares.add(lastSquare);
        }else{
            dragging = true;
            lastSquare.dv.set(0,0);
            pressLoc.set(x,y);
            mouseDelta.set(lastSquare.loc.x - x, lastSquare.loc.y - y);
        }*/
        curArea = squares.hit(x, y);
        curArea.dn(x,y);

        repaint();
    }

    public void mouseDragged(MouseEvent me){
        int x = me.getX(), y = me.getY();
        /*if (dragging){
            lastSquare.moveTo(mouseDelta.x + x, mouseDelta.y + y);
        }else{
            lastSquare.resize(me.getX(), me.getY());
        }*/
        curArea.drag(x,y);

        repaint();
    }
    public void mouseReleased(MouseEvent me){
        if (dragging){
            lastSquare.dv.set(me.getX() - pressLoc.x, me.getY() - pressLoc.y);
        }
    }
    public static void main(String [] args){
        PANEL = new Squares();
        WinApp.launch();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    //=----------------------Square--------------------------------------------------------
    public static class Square extends G.VS implements I.Area{
        public static Square BACKGROUND = new Square(){
            public void dn(int x,int y){
                lastSquare = new Square(x, y);
                squares.add(lastSquare);
            }
            public void drag(int x, int y){
                lastSquare.resize(x,y);
            }
        };
        public Color c = G.rndColor();
        public G.V dv = new G.V(G.rnd(20)-10, G.rnd(20)-10);
        public Square(int x, int y){
            super(x, y, 100, 100);
        }
        public Square(){
            super(0,0,5000,5000);
            c = Color.WHITE;
        }
        public void resize(int x, int y){
            if(x > loc.x && y > loc.y){
                size.set(x - loc.x, y - loc.y);
            }
        }
        public void moveTo (int x, int y){loc.set(x, y);}
        public void draw(Graphics g){fill(g, c);/*moveAndBounce();*/}
        public void moveAndBounce() {
            loc.add(dv);
            if (xL() <= 0 && dv.x < 0) {
                dv.x = -dv.x;
            }
            if (xH() >= 1000 && dv.x > 0) {
                dv.x = -dv.x;
            }
            if (yL() <= 0 && dv.y < 0) {
                dv.y = -dv.y;
            }
            if (yH() >= 700 && dv.y > 0) {
                dv.y = -dv.y;
            }
        }

        @Override
        public void dn(int x, int y) {
            mouseDelta.set(loc.x - x, loc.y - y);
        }

        @Override
        public void drag(int x, int y) {
            loc.set(mouseDelta.x + x, mouseDelta.y + y);
        }

        @Override
        public void up(int x, int y) {

        }
        //=----------------------List--------------------------------------------------------

        public static class List extends ArrayList<Square> {
            public List (){
                super();
                add(Square.BACKGROUND);
            }
            public void draw(Graphics g){
                for (Square s: this){s.draw(g);}


            }
            public Square hit(int x, int y){
                Square res = null;
                for (Square s: this){
                    if (s.hit(x, y)){res = s;}
                }
                return res;
            }
        }
    }

}
