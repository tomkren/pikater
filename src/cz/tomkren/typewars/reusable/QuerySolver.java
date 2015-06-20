package cz.tomkren.typewars.reusable;


import cz.tomkren.typewars.Types;

import java.util.HashMap;
import java.util.Map;

public class QuerySolver {

    private SmartLib lib;
    private Map<String,QueryResult> queries;

    private int nextVarId;

    private int numQueryCalls;
    private int numQueryResults;

    public QuerySolver(SmartLib lib) {
        this.lib = lib;

        nextVarId = 0;
        queries = new HashMap<>();

        numQueryCalls = 0;
        numQueryResults = 0;
    }

    public int getNextVarId() {return nextVarId;}
    public void setNextVarId(int nextVarId) {this.nextVarId = nextVarId;}

    public QueryResult query(String type, int treeSize) {
        return query(new Query(Types.parse(type),treeSize));
    }

    public QueryResult query(Query q) {
        q.setSolver(this);
        String qKey = q.toString();
        QueryResult qResult = queries.get(qKey);
        if (qResult == null) {
            qResult = new QueryResult(q);
            queries.put(qKey, qResult);
            numQueryResults ++;
        }
        numQueryCalls ++;
        return qResult;
    }


    public SmartLib getLib() {return lib;}

    public String getReuseRatio() {
        return numQueryResults+"/"+numQueryCalls;
    }

}

    /*public QueryResult_old query_old(Query q) {
        q.setSolver(this);
        String qKey = q.toString();
        QueryResult_old qNode = queries_old.get(qKey);
        if (qNode == null) {
            qNode = new QueryResult_old(q);
            queries_old.put(qKey,qNode);
        }
        return qNode;
    }*/


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

