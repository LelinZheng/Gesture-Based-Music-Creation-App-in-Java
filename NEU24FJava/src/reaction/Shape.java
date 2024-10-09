package reaction;

import games.Tetris;
import graphics.G;

import musics.I;
import musics.UC;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.TreeMap;

public class Shape implements Serializable{
    public static Shape.Database DB = Shape.Database.load();
    public static Shape DOT = DB.get("DOT");
    public static Trainer TRAINER = new Trainer();
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
    public static class Database extends TreeMap<String,Shape> implements Serializable {
        private static String fileName = UC.shapeDatabaseFileName;
        private Database(){
            super();
            put("DOT", new Shape("DOT"));
        }
        private Shape forceGet(String name){
            if(!DB.containsKey(name)){
                DB.put(name, new Shape(name));
            }
            return DB.get(name);
        }
        public void train(String name, Ink.Norm norm){
            if (isLegal(name)){
                forceGet(name).prototypes.train(norm);
            }
        }
        public static Database load() { // we could have "throws Exception" this is showing there could be Exception
            Database res;
            try{
                System.out.println("Attempting to load " + fileName);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
                res = (Shape.Database)ois.readObject();
                System.out.println("Successful load found: " + res.keySet());
                ois.close();
            }catch(Exception e) {
                System.out.println("Load failed");
                System.out.println(e);
                res = new Database(); //initialization
            }
            return res;
        }
        public static void save(){
            try{
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
                oos.writeObject(DB);
                System.out.println("Saved: " + fileName);
                oos.close(); // stop the file in use and save buffer space
            }catch(Exception e){
                System.out.println("Failed saving: " + fileName);
                System.out.println(e);
            }
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
    public static class Prototype extends Ink.Norm implements Serializable{
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
            public void train(Ink.Norm norm){
                if (bestDist(norm) < UC.noMatchDist) {
                    bestMatch.blend(norm);
                }else{
                    add(new Shape.Prototype());
                }
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
    // ------------------------------Trainer----------------------------------
    public static class Trainer implements I.Show, I.Area{

        public static final String UNKNOWN = " <- this name unknown";
        public static final String ILLEGAL = " <- this name is NOT legal";
        public static final String KNOWN = " <- this name is known";
        public static Shape.Prototype.List pList = new Shape.Prototype.List();

        public static String curName = "";
        public static String curState = ILLEGAL;
        private Trainer(){}; // Singeton

        public void setState(){
            curState = (!Shape.DB.isLegal(curName))? ILLEGAL:UNKNOWN;
            if (curState == UNKNOWN){
                if (Shape.DB.isKnown(curName)){
                    curState = KNOWN;
                    pList = Shape.DB.get(curName).prototypes;
                }else{
                    pList = null;
                }
            }

        }

        public void show(Graphics g){
            G.fillBack(g);
            g.setColor(Color.BLACK);
            g.drawString(curName, 600, 30);
            g.drawString(curState, 700, 30);
            g.setColor(Color.RED);
            Ink.BUFFER.show(g);
            if (pList != null){pList.show(g);}
        }

        public boolean hit(int x, int y){return true;}
        public void dn(int x, int y){Ink.BUFFER.dn(x,y);}
        public void drag(int x, int y){Ink.BUFFER.drag(x,y);}
        public void up(int x, int y){
            Ink.BUFFER.up(x,y);
            Ink ink = new Ink();
            Shape.DB.train(curName, ink.norm);
            setState();
        }

        public void keyTyped(KeyEvent ke){
            char c = ke.getKeyChar();
            System.out.println("Typed: " + c);
            curName = (c == ' ' || c == 0x0D || c == 0x0A)?"": curName + c;
            if (c == 0x0D || c == 0x0A) {
                Shape.Database.save();
            }
            setState();
        }

    }

}