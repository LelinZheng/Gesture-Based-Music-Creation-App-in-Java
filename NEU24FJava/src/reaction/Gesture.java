package reaction;

import graphics.G;
import musics.I;

import java.util.ArrayList;

public class Gesture {

    public static String recognized = "NULL";
    private static List UNDO = new List();
    public static I.Area AREA = new I.Area() {
        public boolean hit(int x, int y) {return true;}

        public void dn(int x, int y) {Ink.BUFFER.dn(x, y);}

        public void drag(int x, int y) {Ink.BUFFER.drag(x, y);}

        public void up(int x, int y) {
            Ink.BUFFER.up(x, y);
            Ink ink = new Ink();
            Gesture gest = Gesture.getNew(ink);
            Ink.BUFFER.clear();
            recognized = gest == null ? "NULL" : gest.shape.name;
            System.out.println(recognized);
            if (gest != null) {
                if (gest.shape.name.equals("N-N")){
                    undo();
                }else{
                    gest.doGesture();
                }
            }
        }
    };
    public Shape shape;
    public G.VS vs;

    private Gesture(Shape shape, G.VS vs) {
        this.shape = shape;
        this.vs = vs;
    }

    // Gesture cannot return null
    private void reDoGesture() {
        Reaction r = Reaction.best(this);
        if (r != null) {
            r.act(this);
        }
    }

    private void doGesture() {
        Reaction r = Reaction.best(this);
        if (r != null) {
            UNDO.add(this);
            r.act(this);
        } else {
            recognized += " NO BIDS";
        }
    }

    public static void undo() {
        if (UNDO.size() > 0) {
            UNDO.remove(UNDO.size() - 1);
            Layer.nuke(); // eliminates all the masses
            Reaction.nuke(); // clears out byshapes and reloads initial reactions
            UNDO.redo();
        }
    }

    //getNew does not have to succeed
    public static Gesture getNew(Ink ink) {
        Shape s = Shape.recognize(ink);
        return s == null ? null : new Gesture(s, ink.vs);
    }

    //-----------------List-------------------
    public static class List extends ArrayList<Gesture> {
        private void redo() {
            for (Gesture gest : this) {gest.reDoGesture();}
        }
    }
}
