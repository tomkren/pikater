package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Listek;
import cz.tomkren.typewars.PolyTree;
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


    public static TMap<PolyTree> generateAll(Query dadQuery, List<Query> sonQueries) {
        return generateAll(dadQuery, Listek.fromList(sonQueries), new Sub(), Listek.mkSingleton(null) );
    }

    private static TMap<PolyTree> generateAll(Query dadQuery, Listek<Query> sonQueries, Sub sub, Listek<Listek<PolyTree>> acc) {

        if (sonQueries == null) {

            TMap<PolyTree> ret = new TMap<>();

            SmartSym sym = dadQuery.getSym();
            Type originalType = dadQuery.getType();

            for (Listek<PolyTree> sons : Listek.toList(acc)) {
                Type rootType = sub.apply(originalType);
                PolyTree newTree = sym.mkTree(rootType, Listek.toReverseList(sons) );
                ret.add(rootType, newTree);
            }

            return ret;
        }

        Query sonQuery = new Query(sub, sonQueries.getHead());
        TMap<PolyTree> sonResult = dadQuery.getSolver().generateAll(sonQuery);

        TMap<PolyTree> ret = new TMap<>();

        if (sonResult.isEmpty()) {return ret;}

        for (Map.Entry<Type,List<PolyTree>> e : sonResult.entrySet()) {

            Type moreSpecificType = e.getKey();
            List<PolyTree> sonTrees = e.getValue();

            Sub sonSpecificSub = Sub.mgu( moreSpecificType, sonQuery.getType() );
            Sub newSub = Sub.dot(sonSpecificSub,sub);

            // newAcc vznikne obohacenim acc o sonTrees
            Listek<Listek<PolyTree>> newAcc = null;
            for (Listek<PolyTree> preArgs : acc.toList()) {
                for (PolyTree sonTree : sonTrees) {
                    newAcc = Listek.mk( Listek.mk(sonTree,preArgs) , newAcc);
                }
            }

            TMap<PolyTree> subRet = generateAll(dadQuery, sonQueries.getTail(), newSub, newAcc);
            ret.add(subRet);
        }

        return ret;
    }

    private static BigInteger howMany(Query dadQuery, Listek<Query> sonQueries, Sub sub, BigInteger num) {

        if (sonQueries == null) {return num;}

        QueryResult sonResult = dadQuery.query(new Query(sub, sonQueries.getHead()));

        BigInteger sum = BigInteger.ZERO;

        if (F.isZero(sonResult.getNum())) {return sum;}

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

    public static BigInteger howMany(Query dadQuery, List<Query> sonQueries) {
        return howMany(dadQuery, Listek.fromList(sonQueries), new Sub(), BigInteger.ONE);
    }


}
