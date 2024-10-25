package musics;

import graphics.G;
import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Staff extends Mass {
    public Sys sys;
    public int iStaff;
    public G.HC staffTop;
    public Staff.Fmt fmt;

    public Staff(Sys sys, int iStaff, G.HC staffTop, Staff.Fmt fmt){
        super("BACK");
        this.sys = sys;
        this.iStaff = iStaff;
        this.staffTop = staffTop;
        this.fmt = fmt;

        addReaction(new Reaction("S-S") { // create new bar
            @Override
            public int bid(Gesture g) {
                Page PAGE = sys.page;
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                if (x < PAGE.margins.left || x > PAGE.margins.right + UC.barToMarginSnap){return UC.noBid;}
                int d = Math.abs(y1 - yTop()) + Math.abs(y2 - yBot());
                // Bias let's cycle bar S-S outbid this
                return d < 30? d + UC.barToMarginSnap: UC.noBid;
            }
            @Override
            public void act(Gesture g) {
                new Bar(Staff.this.sys, g.vs.xM()); // Staff.this refers to the Staff constructor

            }
        });

        addReaction(new Reaction("S-S") { // toggle barContinues
            @Override
            public int bid(Gesture g) {
               if (Staff.this.sys.iSys != 0){return UC.noBid;}
               int y1 = g.vs.yL(), y2 = g.vs.yH();
               if (iStaff == sys.staffs.size() - 1){return UC.noBid;}
               if (Math.abs(y1 - yBot()) > 20){return UC.noBid;}
               Staff nextStaff = sys.staffs.get(iStaff + 1);
               if (Math.abs(y2 - nextStaff.yTop()) > 20){return UC.noBid;}
               return 10;
            }
            @Override
            public void act(Gesture g) {
                fmt.toggleBarContinues();
            }
        });

        addReaction(new Reaction("SW-SW") { //adds note to staff
            @Override
            public int bid(Gesture g){
                Page PAGE = sys.page;
                int x = g.vs.xM(), y = g.vs.yM();
                if (x < PAGE.margins.left || x > PAGE.margins.right){return UC.noBid;}
                int H = Staff.this.fmt.H, top = Staff.this.yTop()-H, bot = Staff.this.yBot()+H;
                if (y < top || y > bot){return UC.noBid;}
                return 10;
            }
            @Override
            public void act(Gesture g) {
                new Head(Staff.this,g.vs.xM(),g.vs.yM());

            }
        });
        addReaction(new Reaction("W-S") { // Add quarter rest
            public int bid(Gesture g) {
                int x = g.vs.xL(), y = g.vs.yM(); // Quesion?????
                if (x < sys.page.margins.left || x > sys.page.margins.right){return UC.noBid;}
                int H = fmt.H, top = yTop() - H, bot = yBot() + H;
                if (y < top || y > bot){return UC.noBid;}
                return 10;
            }
            public void act(Gesture g) {
                Time t = Staff.this.sys.getTime(g.vs.xL());
                new Rest(Staff.this, t);
            }
        });
        addReaction(new Reaction("E-S") { // Add 1/8 rest
            public int bid(Gesture g) {
                int x = g.vs.xL(), y = g.vs.yM();
                if (x < sys.page.margins.left || x > sys.page.margins.right){return UC.noBid;}
                int H = fmt.H, top = yTop() - H, bot = yBot() + H;
                if (y < top || y > bot){return UC.noBid;}
                return 10;
            }
            public void act(Gesture g) {
                Time t = Staff.this.sys.getTime(g.vs.xL());
                (new Rest(Staff.this, t)).nFlag = 1;
            }
        });
    }

    public int yTop(){return staffTop.v();}
    public int yOfLine(int line){return yTop() + line * fmt.H;}
    public int lineOfY(int y){
        int H = fmt.H;
        int bias = 100;
        int top = yTop() - H*bias;
        return (y - top + H/2)/H - bias;  // adding the half H to do rounding instead of floor division
    }
    public int yBot(){return yOfLine(2 * (fmt.nLines - 1));}

    public Staff copy(Sys newSys){
        G.HC hc = new G.HC(newSys.staffs.sysTop, staffTop.dv);
        return new Staff(newSys,iStaff,hc,fmt);
    }
    public void show(Graphics g){
        Page.Margins m = sys.page.margins;
        int x1 = m.left, x2 = m.right, y = yTop(), h = fmt.H *2;
        for (int i = 0; i < fmt.nLines; i++){
            g.drawLine(x1, y+i*h, x2,y+i*h);
        }
    }


    //-----------------------Fmt--------------------------
    public static class Fmt{
        public int nLines;
        public int H; // this is half the line space on ths staff
        public boolean barContinues = false;
        public static Fmt DEFAULT = new Fmt(5, 8);

        public Fmt(int nLines, int H){this.nLines = nLines;this.H = H;}
        public void toggleBarContinues(){barContinues = !barContinues;}
    }
    //----------------------List--------------------------
    public static class List extends ArrayList<Staff>{
        public G.HC sysTop;
        public List(G.HC sysTop){
            this.sysTop = sysTop;
        }
        public int sysTop(){return sysTop.v();}
    }
}
