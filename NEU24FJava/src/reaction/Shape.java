package reaction;

import games.Tetris;
import graphics.G;

import musics.I;
import musics.UC;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

public class Shape {
    public static Shape.Database DB = Shape.Database.load();
    public static Shape DOT = DB.get("DOT");
    public static Collection<Shape> LIST = DB.values();
    public Prototype.List prototypes = new Prototype.List();
    public String name;
    public Shape(String name){this.name = name;}
    public static Shape recognize(Ink ink){ // can return null
        if (ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold){return DOT;}
        Shape bestMatch = null;
        int bestSoFar = UC.noMatchDist;
        for (Shape s: LIST){
            int d = s.prototypes.bestDist(ink.norm);
            if (d < bestSoFar){
                bestMatch = s;
                bestSoFar = d;
            }
        }
        return bestMatch;
    }

    //-----------------Database-------------------
    public static class Database extends TreeMap<String,Shape>{
        public static Database load(){
            Database res = new Database();
            res.put("DOT", new Shape("DOT"));
            return res;
        }
        public static void save(){

        }
        public boolean isKnown(String name){return containsKey(name);}
        public boolean isUnknown(String name){return !containsKey(name);}
        public boolean isLegal(String name){return !name.equals("")&& !name.equals("DOT");}

    }
    //map: a look-up function, going from integer, key-value pair, key or value can be any object
    //a tree map requires you to take a key and have them in sort of order
    //tree map: traditional way to look up words in dictionary, list of keys
    //tree and hash are two different ways, hash in random order
    //default: use hash map b/c it's marginally faster

    //-----------------Prototype------------------
    public static class Prototype extends Ink.Norm {
        public int nBlend = 0;//naming: n--counting, i--- in some array, p---pointer
        public void blend(Ink.Norm norm){blend(norm,nBlend);nBlend++;}
        //----------------- List --------------------
        public static class List extends ArrayList<Prototype> implements I.Show{
            //Python allows you to return two functions, Java allows you to return only one
            public static Prototype bestMatch; //set by side effect of bestDist
            public int bestDist(Ink.Norm norm){
                bestMatch = null;
                int bestSoFar = UC.noMatchDist;
                for (Prototype p: this){
                    int d = p.dist(norm);
                    if (d < bestSoFar){
                        bestMatch = p;
                        bestSoFar = d;
                    }
                }
                return bestSoFar;
            }

            private static int m = 10, w = 60;
            private G.VS showBox = new G.VS(m,m,w,w);
            public void show(Graphics g){
                g.setColor(Color.ORANGE);
                for(int i = 0; i< size(); i++){
                    Prototype p = get(i);
                    int x = m + i * (m + w);
                    showBox.loc.set(x, m);
                    p.drawAt(g,showBox);
                    g.drawString("" + p.nBlend, x, 20);
                }

            }
        }

    }
}