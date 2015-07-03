package cz.tomkren.typewars;

import com.google.common.base.Joiner;
import cz.tomkren.helpers.*;
import cz.tomkren.typewars.eva.FitIndiv;
import cz.tomkren.typewars.eva.FitVal;
import cz.tomkren.typewars.reusable.QuerySolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PolyTree implements FitIndiv {

    private final String name;
    private Type type;
    private final List<PolyTree> sons;
    private final Comb0 code;

    private FitVal fitVal;

    public PolyTree(String name, Type type, List<PolyTree> sons, Comb0 code) {
        this.name = name;
        this.type = type;
        this.sons = sons;
        this.code = code;
        this.fitVal = null;
    }

    public PolyTree(String name, Type type, List<PolyTree> sons) {
        this(name, type, sons, null);
    }

    @Override
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

    public List<SubtreePos> getAllSubtreePoses() {

        List<SubtreePos> ret = new ArrayList<>();

        ret.add( SubtreePos.root(type) );

        int sonIndex = 0;
        for (PolyTree son : sons) {
            List<SubtreePos> sonSubtreePoses = son.getAllSubtreePoses();
            for (SubtreePos subtreePosInSon : sonSubtreePoses) {
                ret.add(SubtreePos.step(sonIndex, subtreePosInSon));
            }
            sonIndex++;
        }

        return ret;
    }

    public TMap<SubtreePos> getAllSubtreePoses_byTypes() {
        return new TMap<>(getAllSubtreePoses(), SubtreePos::getType);
    }

    // TODO asi by bylo slušný udělat efektivnějc
    public SubtreePos getRandomSubtreePos(Random rand) {
        return F.randomElement(getAllSubtreePoses(), rand);
    }

    public PolyTree getSubtree(SubtreePos pos) {
        if (pos.isRoot()) {
            return this;
        } else {
            return sons.get(pos.getSonIndex()).getSubtree(pos.getTail());
        }
    }

    public PolyTree changeSubtree(SubtreePos pos, PolyTree newSubtree) {
        if (pos.isRoot()) {
            return newSubtree;
        } else {
            List<PolyTree> newSons = new ArrayList<>(sons.size());
            int sonIndex = pos.getSonIndex();
            int i = 0;
            for (PolyTree son : sons) {
                newSons.add( i == sonIndex ? son.changeSubtree(pos.getTail(),newSubtree) : son );
                i++;
            }
            return  new PolyTree(name,type,newSons,code);
        }
    }

    public static AA<PolyTree> xover(PolyTree mum, PolyTree dad, SubtreePos mumPos, SubtreePos dadPos) {
        PolyTree daughter = mum.changeSubtree(mumPos, dad.getSubtree(dadPos));
        PolyTree son      = dad.changeSubtree(dadPos, mum.getSubtree(mumPos));
        return new AA<>(daughter,son);
    }

    // TODO otázka zda to stojí za porušení immutability, ale slouží to k do-upřesnění typů při reusable generování
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public FitVal getFitVal() {
        return fitVal;
    }

    @Override
    public void setFitVal(FitVal fitVal) {
        this.fitVal = fitVal;
    }

    @Override
    public double getProbability() {
        if (fitVal == null) {throw new Error("fitVal must be not-null!");}
        return fitVal.getVal();
    }

    public static void main(String[] args) {
        Checker ch = new Checker();

        SmartLib lib = SmartLib.DATA_SCIENTIST_01;
        List<PolyTree> trees = new QuerySolver(lib, ch.getRandom()).uniformGenerate("D => LD", 20, 1000);

        for (PolyTree tree : trees) {
            List<SubtreePos> allPoses = tree.getAllSubtreePoses();
            ch.eq(tree.getSize(), allPoses.size());
            SubtreePos randomPos = F.randomElement(allPoses,ch.getRandom());
            Log.it(randomPos);
            ch.eqStr(tree.toString(), tree.changeSubtree(randomPos, tree.getSubtree(randomPos)));
        }

        ch.results();
    }
}
