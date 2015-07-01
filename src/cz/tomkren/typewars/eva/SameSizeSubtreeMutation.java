package cz.tomkren.typewars.eva;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.SubtreePos;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.reusable.QuerySolver;
import cz.tomkren.typewars.reusable.SmartLib;

import java.util.List;
import java.util.Random;

/** Created by tom on 30. 6. 2015. */

// TODO kontrolovat zda se fakt strom změnil (třeba na 5 pokusů)

public class SameSizeSubtreeMutation implements Operator<PolyTree> {

    private final QuerySolver querySolver;
    private double operatorProbability;
    private final Random rand;

    public SameSizeSubtreeMutation(QuerySolver querySolver, double operatorProbability) {
        this.querySolver = querySolver;
        this.operatorProbability = operatorProbability;
        rand = querySolver.getRand();
    }

    public PolyTree mutate(PolyTree tree) {
        // select subtree
        SubtreePos subtreePos = tree.getRandomSubtreePos(rand);
        PolyTree subTree = tree.getSubtree(subtreePos);

        // generate new subtree with same size and type
        Type goalType = subTree.getType();
        int treeSize  = subTree.getSize();
        PolyTree newSubtree = querySolver.query(goalType, treeSize).generateOne();

        //Log.it("sub: "+subTree);
        //Log.it("new: "+newSubtree);

        // create new tree with that subtree
        return tree.changeSubtree(subtreePos, newSubtree);
    }

    @Override
    public List<PolyTree> operate(List<PolyTree> parents) {
        return F.singleton(mutate(parents.get(0)));
    }

    @Override public int getNumInputs() {return 1;}
    @Override public double getProbability() {return operatorProbability;}


    public static void main(String[] args) {
        Checker ch = new Checker();

        SmartLib lib = SmartLib.DATA_SCIENTIST_01;
        QuerySolver querySolver = new QuerySolver(lib, ch.getRandom());
        List<PolyTree> trees = querySolver.uniformGenerate("D => LD", 35, 100);

        SameSizeSubtreeMutation mut = new SameSizeSubtreeMutation(querySolver,1.0);

        for (PolyTree tree : trees) {
            PolyTree mutant = mut.mutate(tree);
            Log.it(tree);
            Log.it(mutant);
            Log.it("------------------------------------------");
        }



        ch.results();
    }
}
