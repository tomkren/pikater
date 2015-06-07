package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.AB;
import cz.tomkren.helpers.F;
import cz.tomkren.typewars.Type;

import java.util.List;

public class RootNode {

    private final SizeNode grandpa;

    private final SmartSym sym;
    private List<ProfileNode> profiles;
    private int num;

    public static RootNode mk(SmartSym sym, SizeNode grandpa) {
        List<List<Integer>> allSimpleProfiles = possibleSimpleProfiles(sym, grandpa);

        if (allSimpleProfiles.size() == 0) {return null;}

        List<List<AB<Integer,Type>>> allSignatures = F.map(allSimpleProfiles, sp -> F.zip(sp, sym.getArgTypes()));

        return new RootNode(sym, grandpa, allSignatures);
    }

    private RootNode(SmartSym sym, SizeNode grandpa, List<List<AB<Integer,Type>>> allSignatures) {
        this.sym = sym;
        this.grandpa = grandpa;
        this.profiles = F.map(allSignatures, signatures -> new ProfileNode(this, signatures) );
    }


    /*private RootNode(SmartSym sym, TypeNode parent, List<List<Integer>> allSimpleProfiles) {
        this.sym = sym;
        this.parent = parent;
        this.profiles = F.map(allSimpleProfiles, this::mkProfile);
    }

    private ProfileNode mkProfile(List<Integer> simpleProfile) {
        assert simpleProfile.size() == sym.getArgTypes().size();

        List<AB<Integer,Type>> signatures = F.zip(simpleProfile, sym.getArgTypes());
        return new ProfileNode(this, signatures);
    }*/


    private static List<List<Integer>> possibleSimpleProfiles(SmartSym sym, SizeNode grandpa) {
        return ProfileNode.possibleSimpleProfiles(grandpa.getTreeSize(), sym.getArity());
    }

    public TreeTree getTreeTree() {return grandpa.getTreeTree();}

    public int getArity() {return sym.getArity();}
    //public int getTreeSize() {return grandpa.getTreeSize();}
}
