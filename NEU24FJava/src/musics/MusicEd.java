package musics;

import graphics.G;
import graphics.WinApp;
import reaction.*;
import reaction.Shape;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class MusicEd extends WinApp {

    public static Page PAGE;
    // public Layer BACK = new Layer("BACK"), FORE = new Layer("FORE");
    static {new Layer("BACK"); new Layer("NOTE"); new Layer("FORE");}
    // static block is for initializing, it runs at load time
    public static boolean training = false;
    public static I.Area curArea = Gesture.AREA;
    public MusicEd(){
      super("Music Editor", UC.screenWidth, UC.screenHeight);
      Reaction.initialReaction.addReaction(new Reaction("W-W") { //adding a new staff to the first system
          public int bid(Gesture g) {return 0;}
          public void act(Gesture g) {
              int y = g.vs.yM();
              PAGE = new Page(y);
              this.disable();
          }
      });
    }
    public void testPoly(Graphics g){
//        Beam.setPoly(100,100+G.rnd(100), 200, 100+G.rnd(100), 8);
//        g.fillPolygon(Beam.poly);
        int H = -8, x1 = 100, x2 = 200;
        Beam.setMasterBeam(x1, 100+G.rnd(100), x2, 100+G.rnd(100));
        g.drawLine(0, Beam.mY1, x1, Beam.mY1);
        Beam.drawBeamStack(g,0,1,x1,x2,H);
        g.setColor(Color.ORANGE);
        Beam.drawBeamStack(g,1,3, x1+10, x2-10, H);
    }
    public void paintComponent(Graphics g){
        G.fillBack(g);
        if (training){Shape.TRAINER.show(g);return;}
        g.setColor(Color.BLACK);
        Ink.BUFFER.show(g);
        Layer.ALL.show(g);
        g.drawString(Gesture.recognized, 900, 30);
        if (PAGE != null){
//            Staff staff = PAGE.sysList.get(0).staffs.get(0);
//            Key.drawOnStaff(g,7,Key.sF,110,Glyph.SHARP,staff);
            // 8 is the size/scale of the symbol
            //Glyph.HEAD_HALF.showAt(g, 8, 200,PAGE.margins.top + 4 * 8);
            //Glyph.HEAD_Q.showAt(g, 8, 200,PAGE.margins.top + 4 * 8);
            //Glyph.HEAD_W.showAt(g, 8, 200,PAGE.margins.top + 4 * 8);
//            int h = 32;
//            Glyph.HEAD_Q.showAt(g,h,200, PAGE.margins.top + 4 * h);
//            g.setColor(Color.RED);
//            g.drawRect(200,PAGE.margins.top+3*h, 24*h/10,2*h);
        }
//        g.fillPolygon(poly);
//        poly.ypoints[3]++;  // change y value everytime we repaint
        //testPoly(g);

    }
    public void trainButton(MouseEvent me){
        if (me.getX() > UC.screenWidth - 40 && me.getY() < 40){
            training = !training;
            curArea = training? Shape.TRAINER:Gesture.AREA;
    }}
    public void mousePressed(MouseEvent me){curArea.dn(me.getX(),me.getY());repaint();}
    public void mouseDragged(MouseEvent me){curArea.drag(me.getX(),me.getY());repaint();}
    public void mouseReleased(MouseEvent me){
        curArea.up(me.getX(),me.getY());
        trainButton(me);
        repaint();
    }

    public void keyTyped(KeyEvent ke){if (training){Shape.TRAINER.keyTyped(ke);repaint();}}

    public static void main(String[] args){PANEL = new MusicEd(); WinApp.launch();}

}
