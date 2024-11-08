package musics;

import java.awt.*;

public class Key {
    public static int[]
            sG = {0,3,-1,2,5,1,4},
            fG = {4,1,5,2,6,3,7},
            sF = {2,5,1,4,7,3,6},
            fF = {6,3,7,4,8,5,9};
    public static void drawOnStaff(Graphics g, int n, int[] lines, int x, Glyph glyph, Staff staff){
        int gap = gapForGlyph(glyph,staff);
        for (int i = 0; i< n; i++){
            glyph.showAt(g,staff.fmt.H,x+i*gap,staff.yOfLine(lines[i]));
        }
    }
    public static int gapForGlyph(Glyph glyph, Staff staff){
        int h = staff.fmt.H;
        if (glyph == Glyph.SHARP){return 22 * 8 /h;}
        if (glyph == Glyph.FLAT){return 18 * 8 /h;}
        return 16 * 8 /h;
    }
}
