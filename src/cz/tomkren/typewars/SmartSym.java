package cz.tomkren.typewars;


import cz.tomkren.helpers.AB;
import cz.tomkren.helpers.ABC;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SmartSym {

    private final CodeNode codeNode;
    //private final List<List<SmartSym>> applicableSons; // TODO ......................

    public SmartSym(CodeNode codeNode) {
        this.codeNode = codeNode;
        //applicableSons = new ArrayList<>(codeNode.getIns().size());
    }


    public ABC<Type,List<Type>,Integer> freshenTypeVars(int startVarId) {

        List<Type> typeList = new ArrayList<>(getArity()+1);
        typeList.add(getOutputType());
        typeList.addAll(getArgTypes());

        TypeTerm helperTerm = new TypeTerm(typeList);
        AB<Type,Integer> p = helperTerm.freshenVars(startVarId, new Sub());

        TypeTerm freshHelperTerm = (TypeTerm) p._1();
        int nextVarId = p._2();

        List<Type> freshTypeList = freshHelperTerm.getArgs();

        Type       freshOutType = freshTypeList.get(0);
        List<Type> freshInTypes = freshTypeList.subList(1,freshTypeList.size());

        return new ABC<>(freshOutType, freshInTypes, nextVarId);
    }



    public PolyTree mkTree(Type rootType, List<PolyTree> sons) {
        return new PolyTree(codeNode, rootType, sons);
    }

    public String getName() {
        return codeNode.getName();
    }

    public Type getOutputType() {return codeNode.getOut();}

    public List<Type> getArgTypes() {
        return codeNode.getIns();
    }

    public int getArity() {
        return codeNode.getArity();
    }

    @Override
    public String toString() {
        return codeNode.toString();
    }
}
