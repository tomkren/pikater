package cz.tomkren.typewars.eva;

import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.eva.IndivGenerator;
import cz.tomkren.typewars.reusable.QuerySolver;

import java.util.List;

/** Created by tom on 30. 6. 2015. */

public class PolyTreeGenerator implements IndivGenerator<PolyTree> {

    private final String goalType;
    private final int maxTreeSize;
    private final QuerySolver querySolver;

    public PolyTreeGenerator(String goalType, int maxTreeSize, QuerySolver querySolver) {
        this.goalType = goalType;
        this.maxTreeSize = maxTreeSize;
        this.querySolver = querySolver;
    }

    @Override
    public List<PolyTree> generate(int numIndivs) {
        return querySolver.uniformGenerate(goalType, maxTreeSize, numIndivs);
    }
}
