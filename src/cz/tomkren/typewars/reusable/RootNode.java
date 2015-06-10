package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.F;
import cz.tomkren.helpers.TODO;
import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.List;

public class RootNode {

    private final QueryResult parent;

    private final SmartSym sym;
    private List<Profile> profiles;
    private BigInteger num;

    public BigInteger getNum() {return num;}

    public Type getRootType() {
        throw new TODO();
    }

    public static RootNode mk(SmartSym sym, QueryResult parent) {
        List<List<Integer>> allSimpleProfiles = possibleSimpleProfiles(sym, parent);

        if (allSimpleProfiles.size() == 0) {return null;}

        List<List<Query>> allSignatures = F.map(allSimpleProfiles, sp -> F.zipWith(sym.getArgTypes(), sp, Query::new));



        RootNode rn = new RootNode(sym, parent, allSignatures);

        if (F.isZero(rn.getNum())) {return null;}

        return rn;
    }

    private RootNode(SmartSym sym, QueryResult parent, List<List<Query>> allSignatures) {
        this.sym = sym;
        this.parent = parent;
        this.profiles = F.map(allSignatures, signatures -> new Profile(this, signatures) );

        num = BigInteger.ZERO;

        // TODO odfiltrovat signatury, ktery maj aspon jeden prazný query
        // TODO pokud žádná nezbyde tak vrátit null

        // TODO aktualizovat num



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


    private static List<List<Integer>> possibleSimpleProfiles(SmartSym sym, QueryResult parent) {
        return Profile.possibleSimpleProfiles(parent.getTreeSize(), sym.getArity());
    }

    public QuerySolver getSolver() {return parent.getSolver();}

    public int getArity() {return sym.getArity();}
    //public int getTreeSize() {return grandpa.getTreeSize();}
}
