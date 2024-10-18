package reaction;

import musics.I;

import java.awt.*;

public abstract class Mass extends Reaction.List implements I.Show {
    public Layer layer;

    public Mass(String layerName){
        layer = Layer.byName.get(layerName);
        if(layer!=null){
            layer.add(this);
        }else{
            System.out.println("Bad layer name " + layerName);
        }
    }

    public void deleteMass(){
        clearAll();
        layer.remove(this);
    }
    public void show(Graphics g){}

    //Fix a bug that shows up removing masses as I.Shows from layers
    private static int M = 1;
    private int hash = M++;

    @Override
    public int hashCode(){return hash;}
    @Override
    public boolean equals(Object o){return this==o;}
}
