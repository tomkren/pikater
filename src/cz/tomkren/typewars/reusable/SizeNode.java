package cz.tomkren.typewars.reusable;


import java.util.List;

public class SizeNode {

    private final TreeTree parent;

    private final int n;
    private List<TypeNode> types;
    private int num;

    public SizeNode(int n, TreeTree parent) {
        this.n = n;
        this.parent = parent;




    }




    public TreeTree getTreeTree() {
        return parent;
    }

    public int getTreeSize() {
        return n;
    }
}
