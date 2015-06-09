package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.F;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ProfileNode {

    private final RootNode parent;
    private final List<Query> signatures;
    private BigInteger num;

    // private Map<?,PolyTree> trees;


    public ProfileNode(RootNode parent, List<Query> signatures) {
        this.parent = parent;
        this.signatures = signatures;
        assert signatures.size() == parent.getArity();

        num = BigInteger.ZERO;

        for (Query query : signatures) {

            // TODO před dotazem musíme na query aplikovat substituci

            QueryResult qResult = parent.getSolver().query(query);

            if (F.isZero(qResult.getNum())) {
                num = BigInteger.ZERO;
                break;
            }

            List<BigInteger> qNums = qResult.getNums();

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



    public QuerySolver getTreeTree() {return parent.getSolver();}


}
