package cz.tomkren.typewars.eva;

import cz.tomkren.helpers.AA;
import cz.tomkren.helpers.TODO;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.SubtreePos;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.reusable.TMap;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** Created by tom on 1. 7. 2015. */

public class BasicXover implements Operator<PolyTree> {

    private double operatorProbability;
    private final Random rand;


    public BasicXover(double operatorProbability, Random rand) {
        this.operatorProbability = operatorProbability;
        this.rand = rand;
    }

    public AA<PolyTree> xover(PolyTree mum, PolyTree dad) {

        //SubtreePos

        TMap<SubtreePos> mumPoses = mum.getAllSubtreePoses_byTypes();
        TMap<SubtreePos> dadPoses = dad.getAllSubtreePoses_byTypes();

        Map<Type,AA<List<SubtreePos>>> intersection = TMap.intersection(mumPoses, dadPoses);



        throw new TODO();
    }



    @Override
    public List<PolyTree> operate(List<PolyTree> parents) {
        AA<PolyTree> children = xover(parents.get(0),parents.get(1));
        return Arrays.asList(children._1(),children._2());
    }

    @Override public int getNumInputs() {return 2;}
    @Override public double getProbability() {return operatorProbability;}

}
