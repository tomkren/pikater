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


    public ProfileNode(RootNode parent) {
        this.parent = parent;

        signatures = new ArrayList<>(parent.getArity());

        // TODO

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
