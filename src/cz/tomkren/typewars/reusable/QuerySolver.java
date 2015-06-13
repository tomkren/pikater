package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.TODO;
import cz.tomkren.typewars.PolyTree;

import java.util.HashMap;
import java.util.Map;

public class QuerySolver {

    private SmartLib lib;
    private Map<String,QueryResult> queries;

    public QuerySolver(SmartLib lib) {
        this.lib = lib;
        queries = new HashMap<>();
    }

    public QueryResult query(Query q) {
        q.setSolver(this);
        String qKey = q.toString();
        QueryResult qNode = queries.get(qKey);
        if (qNode == null) {
            qNode = new QueryResult(q);
        }
        return qNode;
    }


    public SmartLib getLib() {
        return lib;
    }

}


    /*
    private List<SizeNode> sizes;

    public TreeTree(SmartLib lib) {
        this.lib = lib;
        //sizes = new ArrayList<>();
        queries = new HashMap<>();
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
    }*/

