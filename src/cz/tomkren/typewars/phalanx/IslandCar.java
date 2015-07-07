package cz.tomkren.typewars.phalanx;

import com.google.common.base.Joiner;
import cz.tomkren.helpers.*;
import cz.tomkren.typewars.*;
import cz.tomkren.typewars.basicgen.CreationLog;

import java.util.*;

/** Created by tom on 6.7.2015. */

public class IslandCar {

    private final SmartLib lib;
    private CreationLog creationLog;
    private final Deque<IslandLeaf> leafs;



    public IslandCar(Type goalType, SmartLib lib) {

        this.lib = lib;
        creationLog = CreationLog.START;
        leafs = new ArrayDeque<>();

        leafs.offer(new IslandLeaf(goalType, lib));

        tryStep();
    }

    private IslandCar(CreationLog creationLog, Deque<IslandLeaf> leafs, SmartLib lib /*, Sub sub, int nextVarId, int treeSize*/) {
        this.creationLog = creationLog;
        this.leafs = leafs;
        this.lib = lib;

        tryStep();
    }

    private void tryStep() {

        if (leafs.isEmpty()) {
            //throw new TODO();
            return;
        }

        int numPossibleSymbols = leafs.getFirst().numPossibleSymbols();

        if (numPossibleSymbols == 0) {

            throw new TODO();

        } else if (numPossibleSymbols == 1) {

            IslandLeaf singletonLeaf = leafs.removeFirst();

            SmartSym sym = singletonLeaf.getFirstSym();

            creationLog = new CreationLog.Step(creationLog, sym.getCodeNode());

            List<Type> argTypes = sym.getArgTypes();

            int i = 0;
            for (List<SmartSym> possibleSons: sym.getApplicableSons()) {
                leafs.offer(new IslandLeaf(argTypes.get(i),possibleSons));
                i++;
            }

            //Log.it(" >>> "+ toString());

            tryStep();

        }
    }

    public AA<IslandCar> split(Random rand) {

        if (leafs.isEmpty() || leafs.getFirst().numPossibleSymbols() <= 1) {throw new Error("nemělo by nastat protože by mel odchytit tryStep()");}


        Deque<IslandLeaf> leafs1 = new ArrayDeque<>(leafs);
        Deque<IslandLeaf> leafs2 = new ArrayDeque<>(leafs);

        leafs1.removeFirst();
        leafs2.removeFirst();

        AA<IslandLeaf> splitLeaf = leafs.getFirst().split(rand);

        leafs1.addFirst(splitLeaf._1());
        leafs2.addFirst(splitLeaf._2());

        IslandCar car1 = new IslandCar(creationLog, leafs1, lib); //, sub, nextVarId, treeSize);
        IslandCar car2 = new IslandCar(creationLog, leafs2, lib); //, sub, nextVarId, treeSize);

        return new AA<>(car1,car2);
    }


    @Override
    public String toString() {

        List<IslandLeaf> leafsList = new ArrayList<>(leafs);

        return toString(0, creationLog.toRowList(), leafsList);

        //throw new TODO();
    }


    private String toString(int i, List<CreationLog.Row> rows, List<IslandLeaf> leafsList) {

        if (rows.size() <= i) {
            return leafsList.get(i-rows.size()).toString();
        }

        CreationLog.Row row = rows.get(i);
        ProtoNode node = row.getNode();
        List<String> sons = F.map(row.getSuccIs(), j -> toString(j, rows, leafsList));

        String name = node.getName();

        return sons.isEmpty() ? name : "("+name+" "+ Joiner.on(" ").join(sons) +")";
    }

    public static void main(String[] args) {
        Checker ch = new Checker(-7770084078076581017L);
        Random rand = ch.getRandom();

        SmartLib lib =  SmartLib.DATA_SCIENTIST_WITH_PARAMS_01;
        Type goal = Types.parse("D => LD");

        Log.it(lib);

        IslandCar ic = new IslandCar(goal, lib);

        Log.itln(ic);

        AA<IslandCar> ics1 = ic.split(rand);

        Log.it(ics1._1());
        Log.itln(ics1._2());

        AA<IslandCar> ics2 = ics1._2().split(rand);

        Log.it(ics2._1());
        Log.itln(ics2._2());

        AA<IslandCar> ics3 = ics2._2().split(rand);

        Log.it(ics3._1());
        Log.itln(ics3._2());

        AA<IslandCar> ics4 = ics3._2().split(rand);

        Log.it(ics4._1());
        Log.itln(ics4._2());

        AA<IslandCar> ics5 = ics4._1().split(rand);

        Log.it(ics5._1());
        Log.itln(ics5._2());

        AA<IslandCar> ics6 = ics5._2().split(rand);

        Log.it(ics6._1());
        Log.itln(ics6._2());

        ch.results();
    }

}
