package cz.tomkren.typewars.phalanx;

import com.google.common.base.Joiner;
import cz.tomkren.helpers.AA;
import cz.tomkren.helpers.AB;
import cz.tomkren.helpers.ABC;
import cz.tomkren.helpers.F;
import cz.tomkren.typewars.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Created by tom on 6.7.2015.*/

public class IslandLeaf {

    private final Type goal;
    private final List<SmartSym> possibleSymbols;

    public IslandLeaf(Type goalType, SmartLib lib) {

        this.goal = goalType;

        AB<Type,Integer> fresheningResult = goalType.freshenVars(0, new Sub());
        goalType = fresheningResult._1();
        int nextVarId = fresheningResult._2();

        possibleSymbols = new ArrayList<>();

        // odfiltrovat ty nesmyslný
        for (SmartSym smartSym : lib.getSyms()) {
            ProtoNode protoNode = smartSym.getCodeNode();
            ABC<ProtoNode,Sub,Integer> matchRes = protoNode.matchOut(goalType, nextVarId); // TODO opičárna po basic káře, pak nějak líp
            if (matchRes != null) {
                possibleSymbols.add(smartSym); //new Car(this, matchRes._1(), matchRes._2(), matchRes._3())
            }
        }

    }

    public IslandLeaf(Type goal, List<SmartSym> possibleSymbols) {
        this.goal = goal;
        this.possibleSymbols = possibleSymbols;
    }
    
    public int numPossibleSymbols() {
        return possibleSymbols.size();
    }

    public SmartSym getFirstSym() {
        return possibleSymbols.get(0);
    }

    public AA<IslandLeaf> split(Random rand) {

        List<SmartSym> xs1 = new ArrayList<>();
        List<SmartSym> xs2 = new ArrayList<>();

        for (SmartSym sym : possibleSymbols) {
            (rand.nextBoolean() ? xs1 : xs2).add(sym);
        }

        if (xs1.isEmpty()) {xs1.add(xs2.remove(xs2.size()-1));}
        if (xs2.isEmpty()) {xs2.add(xs1.remove(xs1.size()-1));}

        return new AA<>(new IslandLeaf(goal, xs1), new IslandLeaf(goal, xs2));
    }

    @Override
    public String toString() {
        return "{"  + Joiner.on(",").join(F.map(possibleSymbols, SmartSym::getName)) + "}";
    }
}
