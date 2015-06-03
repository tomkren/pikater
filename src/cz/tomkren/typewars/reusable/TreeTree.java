package cz.tomkren.typewars.reusable;


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
        int n = sizes.size() + 1;
        sizes.add(new SizeNode(n, this));
        return n;
    }

    public SmartLib getLib() {
        return lib;
    }
}
