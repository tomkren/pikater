package cz.tomkren.typewars.reusable;


import java.util.ArrayList;
import java.util.List;

public class TypeNode {

    private final SizeNode parent;

    private final List<RootNode> roots;
    private int num;

    public TypeNode(SizeNode parent) {
        this.parent = parent;

        roots = new ArrayList<>();

        // TODO
        for (SmartSym sym : getTreeTree().getLib().getSyms()) {

            tryToAddRoot(sym);

        }


    }


    boolean tryToAddRoot(SmartSym sym) {

        if (true) {     // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            RootNode rn = RootNode.mk(sym, this);
            if (rn != null) {
                roots.add(rn);
            }
        }

        return true;
    }




    public TreeTree getTreeTree() {
        return parent.getTreeTree();
    }

    public int getTreeSize() {
        return parent.getTreeSize();
    }
}
