package cz.tomkren.typewars.reusable;


import cz.tomkren.helpers.F;
import cz.tomkren.helpers.TODO;
import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class RootNode {


    private final SmartSym sym;
    private List<Profile> profiles;
    private BigInteger num;

    public BigInteger getNum() {return num;}

    public Type getRootType() {
        throw new TODO();
    }

    public RootNode(SmartSym sym, Query query) {

        this.sym = sym;
        this.num = BigInteger.ZERO;
        this.profiles = new ArrayList<>();

        List<List<Integer>> allSimpleProfiles = possibleSimpleProfiles(sym, query);

        if (allSimpleProfiles.size() == 0) {return;}

        List<Type> argTypes = sym.getArgTypes();

        for (List<Integer> simpleProfile : allSimpleProfiles) {
            List<Query> queryList = F.zipWith(argTypes, simpleProfile, Query::new);
            profiles.add(new Profile(query, queryList));
        }


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


    private static List<List<Integer>> possibleSimpleProfiles(SmartSym sym, Query query) {
        return Profile.possibleSimpleProfiles(query.getTreeSize(), sym.getArity());
    }


    public int getArity() {return sym.getArity();}
    //public int getTreeSize() {return grandpa.getTreeSize();}
}
