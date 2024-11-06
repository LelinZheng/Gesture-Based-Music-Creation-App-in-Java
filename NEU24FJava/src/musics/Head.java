package musics;

import reaction.Gesture;
import reaction.Mass;
import reaction.Reaction;

import java.awt.*;
import java.util.ArrayList;

public class Head extends Mass implements Comparable<Head>{
    public Staff staff;
    public int line;
    public Time time;
    public Glyph forcedGlyph = null;
    public Stem stem = null;
    public boolean wrongSide;

    public Head(Staff staff, int x, int y){
        super("NOTE");
        this.staff = staff;
        time = staff.sys.getTime(x);
        line = staff.lineOfY(y);
        time.heads.add(this);

        addReaction(new Reaction("S-S") { //stem or unstem heads
            @Override
            public int bid(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                int W = Head.this.w(), hY = Head.this.y();
                if (y1 > y || y2 < y){return UC.noBid;}
                int hL = Head.this.time.x, hR = hL + W;
                if (x < hL - W || x > hR +W){return UC.noBid;}
                if (x < hL + W/2){return hL-x;}
                if (x > hR - W/2){return x -hR;}
                return UC.noBid;
            }

            @Override
            public void act(Gesture g) {
                int x = g.vs.xM(), y1 = g.vs.yL(), y2 = g.vs.yH();
                Staff staff = Head.this.staff;
                Time t = Head.this.time;
                int W = Head.this.w();
                boolean up = x > t.x + W/2;
                if (Head.this.stem == null){
                    //t.stemHeads(staff,up,y1,y2);
                    Stem.getStem(staff, t, y1, y2, up);
                }else{
                    t.unStemHeads(y1,y2);
                }
            }
        });
        addReaction(new Reaction("DOT") {
            @Override
            public int bid(Gesture g) {
                int xH = x(), yH = y(), H = staff.fmt.H, W = w();
                int x = g.vs.xM(), y = g.vs.yM();
                if (Head.this.stem == null){return UC.noBid;}
                if (x < xH || x > xH + 2 * W || y < yH - H || y > yH + H){
                    return UC.noBid;
                }
                return Math.abs(xH + W - x) + Math.abs(yH - y);
            }

            @Override
            public void act(Gesture g) {
                Head.this.stem.cycleDot();
            }
        });
    }
    public int w(){return 24 * staff.fmt.H/10;} // Width of note head
    public int y(){return staff.yOfLine(line);}
    public int x(){
        int res = time.x;
        if (wrongSide){
            res +=(stem != null && stem.isUp)? w(): -w();
        }
        return res;
    }
    public Glyph normalGlyph(){
        if (stem == null){return Glyph.HEAD_Q;}
        if (stem.nFlag == -1){return Glyph.HEAD_HALF;}
        if (stem.nFlag == -2){return Glyph.HEAD_W;}
        return Glyph.HEAD_Q;
    }
    public void delete(){time.heads.remove(this);} //STUB
    public void show(Graphics g){
        g.setColor(wrongSide? Color.GREEN: Color.BLUE);
        if (stem != null && stem.heads.size() != 0 && this == stem.firstHead()){g.setColor(Color.RED);}
        int H = staff.fmt.H;
        (forcedGlyph != null? forcedGlyph:normalGlyph()).showAt(g, H, x(), y());
        if (stem != null){
            int off = UC.AugDotOffset, sp = UC.AugDotSpacing;
            for (int i = 0; i < stem.nDot; i++){
                g.fillOval(x() + off + i * sp, y() -3 * H/2, H*2/3,H*2/3);
            }
        }

    }

    public void unStem() {
        if (stem != null){
            stem.heads.remove(this);
            if (stem.heads.size() == 0){
                stem.deleteStem();
            }
            stem = null;
            wrongSide = false;
        }
    }

    public void joinStem(Stem s) {
        if (stem != null){unStem();}
        s.heads.add(this);
        stem = s;
    }

    @Override
    public int compareTo(Head h) {
        return (staff.iStaff != h.staff.iStaff? staff.iStaff - h.staff.iStaff : line - h.line);
    }

    //---------------------------List---------------------------
    public static class List extends ArrayList<Head>{}

}
