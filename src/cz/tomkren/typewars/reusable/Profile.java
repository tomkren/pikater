package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Listek;
import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class Profile {

    private BigInteger num;

    // private Map<?,PolyTree> trees;


    public Profile(Query dadQuery, List<Query> sonQueries) {
        num = howMany(dadQuery, sonQueries);
    }

    public static BigInteger howMany(Query dadQuery, List<Query> sonQueries) {
        return howMany(dadQuery, Listek.fromList(sonQueries), new Sub(), BigInteger.ONE);
    }

    public static BigInteger howMany(Query dadQuery, Listek<Query> sonQueries, Sub sub, BigInteger num) {
        if (sonQueries == null) {return num;}

        QueryResult sonResult = dadQuery.query(new Query(sub, sonQueries.getHead()));

        if (F.isZero(sonResult.getNum())) {return BigInteger.ZERO;}

        BigInteger sum = BigInteger.ZERO;

        for (Map.Entry<Type,SubResult> e : sonResult.getSubResultMap().entrySet()) {

            SubResult subResult = e.getValue();
            Sub subResultSub = subResult.getSub();
            BigInteger subResultNum = subResult.getNum();

            Sub newSub = Sub.dot(subResultSub,sub);
            BigInteger newNum = subResultNum.multiply(num);

            sum = sum.add(howMany(dadQuery, sonQueries.getTail(), newSub, newNum));
        }

        return sum;
    }


}
