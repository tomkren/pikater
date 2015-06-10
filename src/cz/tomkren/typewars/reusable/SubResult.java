package cz.tomkren.typewars.reusable;

import cz.tomkren.helpers.F;
import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/** Created by tom on 9. 6. 2015. */

// To co pøedtim dìlal TypeNode vlastnì..

public class SubResult {

    private final List<RootNode> roots;

    private Sub sub;
    private BigInteger num;


    public SubResult(Query query, Type rootType) {
        roots = new ArrayList<>();
        num = BigInteger.ZERO;
        sub = Sub.mgu(query.getType(), rootType); // TODO: vhodný místo proèek, protože tu sub už sem musel zpoèíst pøi konstrukci rootType
                                                  // todo  ale tady se mi zda že je nìco na tom to takle znovuspoèíst
    }

    public void addRoot(RootNode rn) {
        roots.add(rn);
        num = num.add(rn.getNum());
    }

    public void addRoots(List<RootNode> rns) {

        for (RootNode rn : rns) {
            BigInteger howMany = rn.getNum();
            if (!F.isZero(howMany)) {   // TODO opravdu je to tady nejlíp kontrolovat?
                roots.add(rn);
                num = num.add(howMany);
            }
        }

    }

    public List<RootNode> getRoots() {return roots;}
    public Sub getSub() {return sub;}
    public BigInteger getNum() {return num;}
}
