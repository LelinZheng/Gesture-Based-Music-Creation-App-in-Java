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
    }

    public int yTop(){return staffTop.v();}
    public int yOfLine(int Line){return yTop() + Line * fmt.H;}
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
