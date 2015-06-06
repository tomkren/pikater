package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.AB;
import cz.tomkren.helpers.F;
import cz.tomkren.typewars.Type;

import java.util.ArrayList;
import java.util.List;

public class RootNode {

    private final TypeNode parent;

    private final SmartSym sym;
    private List<ProfileNode> profiles;
    private int num;

    public RootNode(SmartSym sym, TypeNode parent) {
        this.sym = sym;
        this.parent = parent;

        List<List<Integer>> allSimpleProfiles = ProfileNode.possibleSimpleProfiles(getTreeSize(), getArity());
        this.profiles = F.map(allSimpleProfiles, this::mkProfile);
    }

    private ProfileNode mkProfile(List<Integer> simpleProfile) {
        assert simpleProfile.size() == sym.getArgTypes().size();

        List<AB<Integer,Type>> signatures = F.zip(simpleProfile, sym.getArgTypes());
        return new ProfileNode(this, signatures);
    }

    public static RootNode mk(SmartSym sym, TypeNode parent) {

        // todo !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        return null;
    }

    public TreeTree getTreeTree() {return parent.getTreeTree();}

    public int getArity() {return sym.getArity();}
    public int getTreeSize() {
        return parent.getTreeSize();
    }
}
