package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.F;
import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Profile {

    private final List<Query> signatures;
    private BigInteger num;

    // private Map<?,PolyTree> trees;


    public Profile(Query oldQuery, List<Query> signatures) {
        this.signatures = signatures;

        //assert signatures.size() == parent.getArity();

        num = BigInteger.ZERO;

        Sub sub = new Sub();

        for (Query preQuery : signatures) {

            // TODO před dotazem musíme na query aplikovat substituci

            Query newQuery = new Query(sub, preQuery);

            QueryResult qResult = oldQuery.getSolver().query(newQuery);



            if (F.isZero(qResult.getNum())) {
                num = BigInteger.ZERO;
                break;
            }


            for (Map.Entry<Type,SubResult> e : qResult.getSubResultMap().entrySet()) {
                SubResult subResult = e.getValue();

                Sub subResultSub = subResult.getSub();

                subResultSub.dot(sub);
                sub = subResultSub;

                // TODO

            }


            // TODO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            // todo: hlavně to nepude timdle forcyklem, protože se to větví

        }



    }

    // TODO projít a znovu pochopit, pokud jde tak zjednodušit (možná by stačil jen ten else, ale nevim zatim)
    public static List<List<Integer>> possibleSimpleProfiles(int fatherSize, int numArgs) {

        int size = fatherSize - 1;
        List<List<Integer>> ret = new ArrayList<>();

        if (numArgs == 0 || size < numArgs) {return ret;}

        if (numArgs == 1) {
            ret.add( F.singleton(size) );
        } else {

            int n = size - (numArgs-1);
            for (int i = 1; i <= n ; i++) {

                List<List<Integer>> subResults = possibleSimpleProfiles(size - i + 1, numArgs - 1);

                for (List<Integer> subResult : subResults) {
                    List<Integer> newResult = new ArrayList<>();
                    newResult.add(i);
                    newResult.addAll(subResult);
                    ret.add(newResult);
                }
            }

        }

        return ret;
    }


}
