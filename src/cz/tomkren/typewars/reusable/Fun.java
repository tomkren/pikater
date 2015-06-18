package cz.tomkren.typewars.reusable;

import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Listek;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.Types;

import java.util.*;

/** Created by tom on 13. 6. 2015. */

public class Fun {

    public static TMap<PolyTree> generateAll(SmartLib lib, String goalType, int treeSize) {
        return generateAll(lib, new Query(Types.parse(goalType),treeSize));
    }

    /*public static TMap<PolyTree> generateAll(SmartLib lib, Query query) {
        query.setLib(lib);
        return generateAll(query);
    }*/

    public static TMap<PolyTree> generateAll(SmartLib lib, Query query) {
        //return F.list(query.getLib().getSyms()).map(sym -> generateAll(query,sym)).reduce(TMap::merge);

        TMap<PolyTree> ret = new TMap<>();

        for (SmartSym sym : lib.getSyms()) {

            Type symOutType = sym.getOutputType();
            Type goalType   = query.getType();

            Sub sub = Sub.mgu(goalType, symOutType);

            if (!sub.isFail()) {

                TMap<PolyTree> subRet = generateAll(lib, query, sym, sub);
                ret.add(subRet);
            }
        }

        return ret;
    }

    public static TMap<PolyTree> generateAll(SmartLib lib, Query query, SmartSym sym, Sub sub) {

        //if (sym)

        List<List<Integer>> allSimpleProfiles = possibleSimpleProfiles(query.getTreeSize(), sym.getArity());

        TMap<PolyTree> ret = new TMap<>();

        if (allSimpleProfiles.isEmpty()) {return ret;}

        List<Type> argTypes = sym.getArgTypes();

        for (List<Integer> simpleProfile : allSimpleProfiles) {

            List<Query> sonQueries = F.zipWith(argTypes, simpleProfile, Query::new);

            TMap<PolyTree> subRet = generateAll(lib, query, sym, sonQueries, sub);
            ret.add(subRet);
        }

        return ret;
    }


    public static TMap<PolyTree> generateAll(SmartLib lib, Query dadQuery, SmartSym sym, List<Query> sonQueries, Sub sub) {
        return generateAll(lib, dadQuery, sym, Listek.fromList(sonQueries), sub, Listek.mkSingleton(null) );
    }

    private static TMap<PolyTree> generateAll(SmartLib lib, Query dadQuery, SmartSym sym, Listek<Query> sonQueries, Sub sub, Listek<Listek<PolyTree>> acc) {

        if (sonQueries == null) {

            TMap<PolyTree> ret = new TMap<>();

            Type originalType = dadQuery.getType();

            for (Listek<PolyTree> sons : Listek.toList(acc)) {
                Type rootType = sub.apply(originalType);
                PolyTree newTree = sym.mkTree(rootType, Listek.toReverseList(sons) );
                ret.add(rootType, newTree);
            }

            return ret;
        }

        Query sonQuery = new Query(sub, sonQueries.getHead()); // TODO tady možná potøeba nìjaká normalizace
        TMap<PolyTree> sonResult = generateAll(lib, sonQuery);

        TMap<PolyTree> ret = new TMap<>();

        if (sonResult.isEmpty()) {return ret;}

        for (Map.Entry<Type,List<PolyTree>> e : sonResult.entrySet()) {

            Type moreSpecificType = e.getKey();
            List<PolyTree> sonTrees = e.getValue();

            Sub sonSpecificSub = Sub.mgu( moreSpecificType, sonQuery.getType() );
            Sub newSub = Sub.dot(sonSpecificSub,sub);

            // newAcc vznikne obohacenÃ­m acc o sonTrees
            Listek<Listek<PolyTree>> newAcc = null;
            for (Listek<PolyTree> preArgs : acc.toList()) {
                for (PolyTree sonTree : sonTrees) {
                    newAcc = Listek.mk( Listek.mk(sonTree,preArgs) , newAcc);
                }
            }

            TMap<PolyTree> subRet = generateAll(lib, dadQuery, sym, sonQueries.getTail(), newSub, newAcc);
            ret.add(subRet);
        }

        return ret;
    }



    public static List<List<Integer>> possibleSimpleProfiles(int fatherSize, int numArgs) {

        int size = fatherSize - 1;
        List<List<Integer>> ret = new ArrayList<>();

        if (size < numArgs) {
            return ret;
        }

        // todo tady sem to dal novÄ›
        if (numArgs == 0) {
            if (size == 0) {
                ret.add(Collections.emptyList());
            }
            return ret;
        }

        if (numArgs == 1) {
            ret.add(F.singleton(size));
        } else {

            int n = size - (numArgs - 1);
            for (int i = 1; i <= n; i++) {

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
