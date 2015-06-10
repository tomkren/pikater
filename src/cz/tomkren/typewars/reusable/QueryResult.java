package cz.tomkren.typewars.reusable;

import cz.tomkren.helpers.F;
import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by tom on 7. 6. 2015. */

public class QueryResult {

    private final Map<Type,SubResult> result;
    private BigInteger num;

    public QueryResult(Query query) {


        result = new HashMap<>();
        num = BigInteger.ZERO;

        for (SmartSym sym : query.getAllSyms()) { // TODO tady pak nahradit lokalnì pro parentSym v query
            RootNode rn = new RootNode(sym, query);
            if (!F.isZero(rn.getNum())) {

                Type rootType = rn.getRootType();
                SubResult subResult = result.get(rootType);

                if (subResult == null) {
                    subResult = new SubResult(query, rootType);
                    result.put(rootType, subResult);
                }

                subResult.addRoot(rn);
                num = num.add(rn.getNum());
            }
        }
    }


    public Map<Type, SubResult> getSubResultMap() {return result;}

    public BigInteger getNum() {return num;}


}
