package cz.tomkren.typewars.reusable;


import cz.tomkren.typewars.CodeNode;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.Type;

import java.util.ArrayList;
import java.util.List;

public class SmartSym {

    private final CodeNode codeNode;
    private final List<List<SmartSym>> applicableSons;

    public SmartSym(CodeNode codeNode) {
        this.codeNode = codeNode;
        applicableSons = new ArrayList<>(codeNode.getIns().size());
    }

    public PolyTree mkTree(Type rootType, List<PolyTree> sons) {
        return new PolyTree(codeNode.getName(), rootType, sons, codeNode.getCode());
    }

    public List<Type> getArgTypes() {
        return codeNode.getIns();
    }

    public int getArity() {
        return codeNode.getArity();
    }



}
