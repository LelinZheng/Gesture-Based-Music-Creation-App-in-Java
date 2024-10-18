package reaction;

import musics.I;
import musics.UC;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Reaction implements I.React{
    private static Map byShape = new Map();
    public static List initialReaction = new List(); //used by undo to restart everything

    public Shape shape;

    public Reaction(String shapeName){
        shape = Shape.DB.get(shapeName);
        if(shape==null){System.out.println("WTF?-shape.db does not know " + shapeName);}
    }

    // Enable or disable add or remove reaction from the byShape map
    public void enable(){
        List list = byShape.getList(shape);
        if (!list.contains(this)) {
            list.add(this);
        }
    }
    public void disable(){
        List list = byShape.getList(shape);
        list.remove(this);
    }
    public static void nuke(){
        byShape = new Map();
        initialReaction.enable();
    }

    public static Reaction best(Gesture g){
        return byShape.getList(g.shape).loBid(g);
    }

    //----------------List------------------------------
    public static class List extends ArrayList<Reaction> {
        public void addReaction(Reaction r){add(r); r.enable();}
        public void removeReacton(Reaction r){remove(r); r.disable();}
        public void clearAll(){
            for(Reaction r: this){
                r.disable();
            }
            this.clear();
        }
        public Reaction loBid(Gesture g){ //can return null
            Reaction res = null;
            int bestSoFar = UC.noBid;
            for(Reaction r: this){
                int b = r.bid(g);
                if(b<bestSoFar){
                    bestSoFar=b;
                    res=r;
                }
            }
            return res;
        }
        public void enable(){
            for (Reaction r: this){r.enable();}
        }
    }
    //------------------Map-----------------------------
    public static class Map extends HashMap<Shape,List>{
        public List getList(Shape s){ //Always succeeds!!
            List res = get(s);
            if(res==null){
                res= new List();
                put(s,res);
            }
            return res;
        }
    }

}