package cz.tomkren.typewars.reusable;

import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by tom on 7. 6. 2015. */

public class QueryResult {

    private final QuerySolver solver;
    private final Query query;

    private final Map<Type,List<RootNode>> result;

    private BigInteger num;
    private List<BigInteger> nums; // sum nums = num

    public QueryResult(Query query, QuerySolver solver) {
        this.solver = solver;
        this.query = query;

        result = new HashMap<>();
        num = BigInteger.ZERO;

        for (SmartSym sym : solver.getLib().getSyms()) { // TODO tady pak nahradit lokalnì pro parentSym v query
            RootNode rn = RootNode.mk(sym, this);
            if (rn != null) {

                Type rootType = rn.getRootType();
                List<RootNode> roots = result.get(rootType);

                if (roots == null) {
                    roots = new ArrayList<>();
                    result.put(rootType, roots);
                }

                roots.add(rn);
                num = num.add(rn.getNum());
            }
        }


    }

    public List<BigInteger> getNums() {
        if (nums != null) {return nums;}

        nums = new ArrayList<>();

        for (Map.Entry<Type,List<RootNode>> e : result.entrySet()) {
            BigInteger num = BigInteger.ZERO;
            for (RootNode rn : e.getValue()) {
                num = num.add(rn.getNum());
            }
            nums.add(num);
        }

        return nums;
    }


    public BigInteger getNum() {return num;}

    public int getTreeSize() {
        return query.getTreeSize();
    }
    public Type getType() {return query.getType();}

    public QuerySolver getSolver() {
        return solver;
    }


}
