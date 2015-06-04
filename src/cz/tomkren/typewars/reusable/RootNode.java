package cz.tomkren.typewars.reusable;


import java.util.List;

public class RootNode {

    private final TypeNode parent;

    private final SmartSym sym;
    private List<ProfileNode> profiles;
    private int num;

    public RootNode(SmartSym sym, TypeNode parent) {
        this.sym = sym;
        this.parent = parent;
    }

    public static RootNode mk(SmartSym sym, TypeNode parent) {

        // todo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        return null;
    }

    public TreeTree getTreeTree() {return parent.getTreeTree();}

    public int getArity() {return sym.getArity();}
}
