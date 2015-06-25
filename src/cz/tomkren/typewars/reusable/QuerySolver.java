package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.Log;
import cz.tomkren.helpers.TODO;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.Types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuerySolver {

    private SmartLib lib;
    private Random rand;

    private Map<String,QueryResult> queries;

    private int nextVarId;

    private int numQueryCalls;
    private int numQueryResults;

    public QuerySolver(SmartLib lib, Random rand) {
        this.lib = lib;
        this.rand = rand;

        nextVarId = 0;
        queries = new HashMap<>();

        numQueryCalls = 0;
        numQueryResults = 0;
    }

    public int getNextVarId() {return nextVarId;}
    public void setNextVarId(int nextVarId) {this.nextVarId = nextVarId;}
    public Random getRand() {return rand;}

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

    public List<PolyTree> generate(String goalType, int treeSize, int numToGenerate) {
        throw new TODO();
    }

    public PolyTree generateOne(String goalType, int treeSize) {
        return query(goalType, treeSize).generateOne();
    }

    public TMap<PolyTree> generateAllUpTo(String goalType, int upToTreeSize) {
        Log.it("generateAllUpTo("+goalType+", "+upToTreeSize+")");
        TMap<PolyTree> ret = new TMap<>();
        for (int treeSize = 1; treeSize <= upToTreeSize; treeSize++) {
            QueryResult qRes = query(goalType, treeSize);
            Log.it("  treeSize = " + treeSize + ", num = " + qRes.getNum() + "\t\t" + "reuse-ratio: " + getReuseRatio());
            ret.add(qRes.generateAll());
        }
        return ret;
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

