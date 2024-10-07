package reaction;

import graphics.G;
import graphics.WinApp;
import musics.UC;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ShapeTrainer extends WinApp {
    public static final String UNKNOWN = " <- this name unknown";
    public static final String ILLEGAL = " <- this name is NOT legal";
    public static final String KNOWN = " <- this name is known";
    public static Shape.Prototype.List pList = new Shape.Prototype.List();

    public static String curName = "";
    public static String curState = ILLEGAL;

    public ShapeTrainer(){
        super("ShapeTrainer", UC.screenWidth, UC.screenHeight);
    }
    public void setState(){
        curState = (!Shape.DB.isLegal(curName))? ILLEGAL:UNKNOWN;
        if (curState == UNKNOWN){
            if (Shape.DB.isKnown(curName)){
                curState = KNOWN;
                pList = Shape.DB.get(curName).prototypes;
            }else{
                pList = null;
            }
        }

    }
    public void paintComponent(Graphics g){
        G.fillBack(g);
        g.setColor(Color.BLACK);
        g.drawString(curName, 600, 30);
        g.drawString(curState, 700, 30);
        g.setColor(Color.RED);
        Ink.BUFFER.show(g);
        if (pList != null){pList.show(g);}

    }

    public void mousePressed(MouseEvent me){
        Ink.BUFFER.dn(me.getX(), me.getY());
        repaint();
    }
    public void mouseDragged(MouseEvent me){
        Ink.BUFFER.drag(me.getX(), me.getY());
        repaint();
    }
    public void mouseReleased(MouseEvent me){
        Ink.BUFFER.up(me.getX(), me.getY());
        if (curState != ILLEGAL){
            Ink ink = new Ink();
            Shape.Prototype proto;
            if(pList == null){
                Shape s = new Shape(curName);
                Shape.DB.put(curName,s);
                pList = s.prototypes;
            }
            if(pList.bestDist(ink.norm) < UC.noMatchDist){
                proto = Shape.Prototype.List.bestMatch;
                proto.blend(ink.norm);
            }else{
                proto = new Shape.Prototype();
                pList.add(proto);
            }
            setState();
        }
        repaint();
    }

    public void keyTyped(KeyEvent ke){
        char c = ke.getKeyChar();
        System.out.println("Typed: " + c);
        curName = (c == ' ' || c == 0x0D || c == 0x0A)?"":curName + c;
        setState();
        repaint();
    }

    public static void main(String[] args) {
        PANEL = new ShapeTrainer();
        WinApp.launch();
    }

}
