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

    private final Map<Type,SubResult> result;

    private BigInteger num;

    public QueryResult(Query query, QuerySolver solver) {
        this.solver = solver;
        this.query = query;

        result = new HashMap<>();
        num = BigInteger.ZERO;

        for (SmartSym sym : solver.getLib().getSyms()) { // TODO tady pak nahradit lokalnì pro parentSym v query
            RootNode rn = RootNode.mk(sym, this);
            if (rn != null) {

                Type rootType = rn.getRootType();
                SubResult subResult = result.get(rootType);

                if (subResult == null) {
                    subResult = new SubResult(query.getType(), rootType);
                    result.put(rootType, subResult);
                }

                subResult.addRoot(rn);
                num = num.add(rn.getNum());
            }
        }
    }


    public Map<Type, SubResult> getSubResultMap() {return result;}

    public BigInteger getNum() {return num;}

    public int getTreeSize() {
        return query.getTreeSize();
    }
    public Type getType() {return query.getType();}

    public QuerySolver getSolver() {
        return solver;
    }


}
