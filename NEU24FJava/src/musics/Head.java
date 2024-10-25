package musics;

import reaction.Mass;

import java.awt.*;

public class Head extends Mass{
    public Staff staff;
    public int line;
    public Time time;
    public Head(Staff staff, int x, int y){
        super("NOTE");
        this.staff = staff;
        time = staff.sys.getTime(x);
        line = staff.lineOfY(y);
    }
    public void show(Graphics g){
        int H = staff.fmt.H;
        Glyph.HEAD_Q.showAt(g, H, time.x, staff.yTop() + line * H);

    }

}
