package musics;

import reaction.Mass;

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
    }
    public void addStem(Stem s){
        if (s.beam == null){
            stems.add(s);
            s.beam = this;
            s.nFlag = 1;
            stems.sort();
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
}
