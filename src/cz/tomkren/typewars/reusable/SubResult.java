package cz.tomkren.typewars.reusable;

import cz.tomkren.typewars.Sub;
import cz.tomkren.typewars.Type;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/** Created by tom on 9. 6. 2015. */

// To co p�edtim d�lal TypeNode vlastn�..

public class SubResult {

    private final List<RootNode> roots;

    private Sub sub;
    private BigInteger num;


    public SubResult(Type queryType, Type rootType) {
        roots = new ArrayList<>();
        num = BigInteger.ZERO;
        sub = Sub.mgu(queryType, rootType); // TODO: vhodn� m�sto pro�ek, proto�e tu sub u� sem musel zpo��st p�i konstrukci rootType
                                            // todo  ale tady se mi zda �e je n�co na tom to takle znovuspo��st
    }

    public void addRoot(RootNode rn) {
        roots.add(rn);
        num = num.add(rn.getNum());
    }

    public List<RootNode> getRoots() {return roots;}
    public Sub getSub() {return sub;}
    public BigInteger getNum() {return num;}
}
