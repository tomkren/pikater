package cz.tomkren.typewars.reusable;

import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;

/** Created by tom on 7. 6. 2015 */

public class Query {

    private final Type type;
    private final int treeSize;

    public Query(Sub sub, Query preQuery) {
        this(sub.apply(preQuery.type),preQuery.treeSize);
    }

    public Query(Type type, int treeSize) {
        this.type = type;
        this.treeSize = treeSize;
    }

    @Override
    public String toString() {
        return type.toString() + " ; " + treeSize ;
    }

    public Type getType() {return type;}
    public int getTreeSize() {return treeSize;}

}
