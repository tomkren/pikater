package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.AB;
import cz.tomkren.typewars.Type;

import java.util.ArrayList;
import java.util.List;

public class TreeTree {

    private SmartLib lib;
    private List<SizeNode> sizes;

    public TreeTree(SmartLib lib) {
        this.lib = lib;
        sizes = new ArrayList<>();
    }

    public int addSize() {
        int n = getMaxSize() + 1;
        sizes.add(new SizeNode(n, this));
        return n;
    }

    public SizeNode getSizeNode(int treeSize) {
        if (treeSize > getMaxSize()) {return null;}
        return sizes.get(treeSize-1);
    }

    public int getMaxSize() {
        return sizes.size();
    }

    public TypeNode getTypeNode(AB<Integer,Type> signature) {
        int treeSize = signature._1();
        Type type    = signature._2();

        SizeNode sizeNode = getSizeNode(treeSize);
        if (sizeNode == null) {return null;}
        return sizeNode.getTypeNode(type);
    }

    public SmartLib getLib() {
        return lib;
    }
}
