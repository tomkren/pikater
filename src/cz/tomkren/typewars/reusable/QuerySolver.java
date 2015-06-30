package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.Log;
import cz.tomkren.helpers.TODO;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.Types;
import cz.tomkren.typewars.eva.IndivGenerator;

import java.util.*;

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

    public QueryResult query(Type type, int treeSize) {
        return query(new Query(type,treeSize));
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


    public static final Comparator<String> compareStrs = (s1, s2) -> {
        int len1 = s1.length();
        int len2 = s2.length();
        if (len1 == len2) {return s1.compareTo(s2);}
        return Integer.compare(len1, len2);
    };

    public static final Comparator<PolyTree> compareTrees = (t1, t2) -> {
        int size1 = t1.getSize();
        int size2 = t2.getSize();
        if (size1 == size2) {return compareStrs.compare(t1.toString(), t2.toString());}
        return Integer.compare(size1, size2);
    };

    public List<PolyTree> uniformGenerate(String goalType, int maxTreeSize, int numToGenerate) {
        Set<PolyTree> codeSet = new TreeSet<>(compareTrees);

        while (codeSet.size() < numToGenerate) {

            int treeSize = rand.nextInt(maxTreeSize + 1);
            PolyTree tree = generateOne(goalType, treeSize);

            if (tree != null) {
                if (!codeSet.contains(tree)) {
                    codeSet.add(tree);
                }
            }
        }

        return new ArrayList<>(codeSet);
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

