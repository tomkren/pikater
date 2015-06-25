package cz.tomkren.typewars;

import com.google.common.base.Joiner;
import cz.tomkren.helpers.Comb0;
import cz.tomkren.helpers.F;

import java.util.ArrayList;
import java.util.List;

public class PolyTree {

    private final String name;
    private Type type;
    private final List<PolyTree> sons;
    private final Comb0 code;

    public PolyTree(String name, Type type, List<PolyTree> sons, Comb0 code) {
        this.name = name;
        this.type = type;
        this.sons = sons;
        this.code = code;
    }

    public PolyTree(String name, Type type, List<PolyTree> sons) {
        this(name, type, sons, null);
    }

    public Object computeValue() {
        if (code == null) {throw new Error("Null-code in computeValue().");}

        if (isTerminal()) {
            List<Object> haxTypeInput = new ArrayList<>(1);
            haxTypeInput.add(type);
            return code.compute(haxTypeInput);
        }
        else {
            return code.compute(F.map(sons, PolyTree::computeValue));
        }
    }

    public String getName() {return name;}
    public Type getType() {return type;}
    public List<PolyTree> getSons() {return sons;}
    public Comb0 getCode() {return code;}

    public boolean isTerminal() {
        return sons.isEmpty();
    }

    // TODO otazka zda to stojí za porušení immutability, ale slouží to k dopøesnìní typù pøi reusable generování
    public void applySub(Sub sub) {
        type = sub.apply(type);
        sons.forEach(s->s.applySub(sub));
    }

    @Override
    public String toString() {
        return isTerminal() ? name : "("+ name +" "+ Joiner.on(' ').join( sons ) +")";
    }

    private String showHead() {
        return "<"+name+":"+type+">";
    }

    public String showWithTypes() {
        return isTerminal() ? showHead() : "("+ showHead() +" "+ Joiner.on(' ').join( F.map(sons, PolyTree::showWithTypes) ) +")";
    }

    public int getSize() {
        int sum = 0;
        for (PolyTree son : sons) {sum += son.getSize();}
        return 1 + sum;
        // return 1 + F.list(sons).map(PolyTree::getSize).foldr(0,(x,y)->x+y);
    }
}
