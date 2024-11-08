package musics;

import reaction.Mass;

import java.awt.*;

public class Beam extends Mass {
    public Stem.List stems = new Stem.List();
    public Beam(Stem first, Stem last){
        super("NOTE");
        addStem(first); addStem(last);

    }


    public Stem first(){return stems.get(0);}
    public Stem last(){return stems.get(stems.size() - 1);}
    public void deleteBeam(){
        for (Stem s: stems){s.beam = null;}
        deleteMass();
    }
    public void addStem(Stem s){
        if (s.beam == null){
            stems.add(s);
            s.beam = this;
            s.nFlag = 1;
            stems.sort();
        }
    }
    public void show(Graphics g){
        g.setColor(Color.BLACK);
        drawBeamGroup(g);
    }
    private void drawBeamGroup(Graphics g){
        setMasterBeam();
        Stem firstStem = first();
        int H = firstStem.staff.fmt.H; int sH = firstStem.isUp? H: -H;
        int nPrev = 0, nCur = firstStem.nFlag, nNext = stems.get(1).nFlag;
        int pX; int cX = firstStem.x(); int bX = cX + 3 * H; // bX is end of beamlet
        if (nCur > nNext){drawBeamStack(g, nNext, nCur, cX, bX, sH);} //WHat???
        for (int cur = 1; cur < stems.size(); cur++){
            Stem sCur = stems.get(cur);
            pX = cX; cX = sCur.x();
            nPrev = nCur; nCur = nNext; nNext = (cur < stems.size() - 1 )? stems.get(cur + 1).nFlag:0;
            int nBack = Math.min(nPrev, nCur);
            drawBeamStack(g, 0, nBack, pX, cX, sH);
            if (nCur > nPrev && nCur > nNext){ //Beamlets required on cur stem
                if (nPrev< nNext){
                    bX = cX + 3 * H;
                    drawBeamStack(g, nNext, nCur, cX, bX, sH); //beamlets towards to the next stem
                }else{
                    bX = cX - 3 * H;
                    drawBeamStack(g, nPrev, nCur, bX, cX, sH); // beamlets towards to the prev stem

                }
            }
        }


    }
    public void setMasterBeam(){
        mX1 = first().x();
        mY1 = first().yBeamEnd();
        mX2 = last().x();
        mY2 = last().yBeamEnd();
    }
    public static int yOfX(int x, int x1, int y1, int x2, int y2){ //this is find the beam and stem intersecting point y
        int dy = y2 - y1 , dx = x2 - x1;
        return (x - x1) * dy/dx + y1;
    }
    public static int mX1, mY1, mX2, mY2; //this is the coordinates for the master beam
    public static void setMasterBeam(int x1, int y1, int x2, int y2){
        mX1 = x1;
        mX2 = x2;
        mY1 = y1;
        mY2 = y2;
    }
    public static int yOfX(int x){
       int dy = mY2 - mY1, dx = mX2 - mX1;
       return (x - mX1) * dy/dx + mY1;
    }
    public static boolean verticalLineCrossesSegment(int x, int y1, int y2, int bX, int bY, int eX, int eY) {
        if (x < bX || x > eX){return false;}
        int y = yOfX(x,bX,bY,eX,eY);
        if (y1 < y2){return y1<y && y<y2;}else{return y2<y && y<y1;}
    }

    static int[] points = {0,0,0,0};
    public static Polygon poly = new Polygon(points, points, 4);
    public static void setPoly(int x1, int y1, int x2, int y2, int h){
        int[] a = poly.xpoints; a[0] = x1; a[1] = x2; a[2] = x2; a[3] = x1;
        a = poly.ypoints; a[0] = y1; a[1] = y2; a[2] = y2 + h; a[3] = y1 + h; // replacing a??????

    }
    public static void drawBeamStack(Graphics g, int n1, int n2, int x1, int x2, int h){
        int y1 = yOfX(x1), y2 = yOfX(x2);
        for (int i = n1; i < n2; i++){
            setPoly(x1,y1+i*2*h, x2, y2+i*2*h, h);
            g.fillPolygon(poly);
        }
    }

    public void removeStem(Stem stem) {
        if (stem == first() || stem == last()){
            deleteBeam();
        }else{
            stems.remove(stem);
            stems.sort();
        }
    }
}
