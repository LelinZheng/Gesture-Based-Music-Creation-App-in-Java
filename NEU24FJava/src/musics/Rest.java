package musics;

import reaction.Gesture;
import reaction.Reaction;

import java.awt.*;

public class Rest extends Duration{
    public Staff staff;
    public Time time;
    public int line = 4;
    public Rest(Staff staff, Time time){
        this.staff = staff;
        this.time = time;

        addReaction(new Reaction("E-E") { // add flag
            public int bid(Gesture g) {
                int y = g.vs.yM(), x1 = g.vs.xL(), x2 = g.vs.xH(), x = Rest.this.time.x;
                if (x1 > x || x2< x){return UC.noBid;}
                return Math.abs(y - Rest.this.staff.yOfLine(4));
            }
            public void act(Gesture g) {
                Rest.this.incFlag();
            }
        });
        addReaction(new Reaction("W-W") { // add flag
            public int bid(Gesture g) {
                int y = g.vs.yM(), x1 = g.vs.xL(), x2 = g.vs.xH(), x = Rest.this.time.x;
                if (x1 > x || x2< x){return UC.noBid;}
                return Math.abs(y - Rest.this.staff.yOfLine(4));
            }
            public void act(Gesture g) {
                Rest.this.decFlag();
            }
        });

    }
    public int y(){return staff.yOfLine(line);}

    private static Glyph[] glyphs =
            {Glyph.REST_W, Glyph.REST_H, Glyph.REST_Q,Glyph.REST_1F,
                    Glyph.REST_2F, Glyph.REST_3F, Glyph.REST_4F};

    public void show(Graphics g){
        int H = staff.fmt.H, y = y();
        Glyph glyph = glyphs[nFlag+2];
        glyph.showAt(g,H, time.x, y);
    }
}
