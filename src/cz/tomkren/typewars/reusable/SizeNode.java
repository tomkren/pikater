package cz.tomkren.typewars.reusable;


import cz.tomkren.typewars.Type;

import java.util.List;
import java.util.Map;

public class SizeNode {

    private final TreeTree parent;

    private final int n;
    private Map<String, TypeNode> types;
    private int num;

    public SizeNode(int n, TreeTree parent) {
        this.n = n;
        this.parent = parent;


        // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!

    }

    public TypeNode getTypeNode(Type type) {
        return types.get(type.toString());
    }


    public TreeTree getTreeTree() {
        return parent;
    }

    public int getTreeSize() {
        return n;
    }
}
