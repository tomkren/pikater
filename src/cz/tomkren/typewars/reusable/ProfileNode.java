package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.AB;
import cz.tomkren.helpers.F;
import cz.tomkren.typewars.Type;

import java.util.ArrayList;
import java.util.List;

public class ProfileNode {

    private final RootNode parent;
    private final List<AB<Integer,Type>> signatures;
    private int num;

    // private List<PolyTree> trees;

    public static ProfileNode mk(RootNode parent, List<AB<Integer,Type>> signatures) {

        boolean fail = false;
        for (AB<Integer,Type> signature :signatures) {
            if (F.isZero(parent.getTreeTree().getTypeNode(signature).getNum())) {
                fail = true;
                break;
            }
        }

        // pokud existuje pro kazdou ze signatur aspon jeden strom tak ok, jinak jsme dali null
        if (fail) {return null;}
        return new ProfileNode(parent, signatures);
    }



    public ProfileNode(RootNode parent, List<AB<Integer,Type>> signatures) {
        this.parent = parent;
        this.signatures = signatures;
        assert signatures.size() == parent.getArity();
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



    public TreeTree getTreeTree() {return parent.getTreeTree();}


}
