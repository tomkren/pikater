package cz.tomkren.typewars.eva;

import cz.tomkren.helpers.Checker;
import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Log;
import cz.tomkren.typewars.PolyTree;
import cz.tomkren.typewars.SubtreePos;
import cz.tomkren.typewars.Type;
import cz.tomkren.typewars.reusable.QuerySolver;
import cz.tomkren.typewars.SmartLib;

import java.util.List;
import java.util.Random;

/** Created by tom on 30. 6. 2015. */

// TODO !!! přidat parametr maxSubtreeSize páč nechceme generovaním obřích stromů strávit mládí (IMHO)

// TODO kontrolovat zda se fakt strom změnil (třeba na 5 pokusů)

public class SameSizeSubtreeMutation implements Operator<PolyTree> {

    private final QuerySolver querySolver;
    private final int maxSubtreeSize;
    private double operatorProbability;
    private final Random rand;

    public static final int MAX_TRIES = 10;

    public SameSizeSubtreeMutation(QuerySolver querySolver, int maxSubtreeSize, double operatorProbability) {
        this.querySolver = querySolver;
        this.maxSubtreeSize = maxSubtreeSize;
        this.operatorProbability = operatorProbability;
        rand = querySolver.getRand();
    }

    @Override
    public List<PolyTree> operate(List<PolyTree> parents) {
        return F.singleton(mutate(parents.get(0)));
    }

    @Override public int getNumInputs() {return 1;}
    @Override public double getProbability() {return operatorProbability;}


    public PolyTree mutate(PolyTree tree) {
        PolyTree mutant = null;
        for (int i = 0; i < MAX_TRIES; i++) {
            mutant = mutate_oneTry(tree);
            if (!mutant.toString().equals(tree.toString())) {
                return mutant;
            }
        }
        return mutant;
    }

    public PolyTree mutate_oneTry(PolyTree tree) {
        // select subtree

        SubtreePos subtreePos;
        PolyTree subTree;
        do {
            subtreePos = tree.getRandomSubtreePos(rand);
            subTree = tree.getSubtree(subtreePos);
        } while (subTree.getSize() > maxSubtreeSize);

        // generate new subtree with same size and type
        Type goalType = subTree.getType();
        int treeSize  = subTree.getSize();
        PolyTree newSubtree = querySolver.generateOne(goalType, treeSize);

        //Log.it("sub: "+subTree);
        //Log.it("new: "+newSubtree);

        // create new tree with that subtree
        return tree.changeSubtree(subtreePos, newSubtree);
    }


    public static void main(String[] args) {
        Checker ch = new Checker();

        SmartLib lib = SmartLib.DATA_SCIENTIST_01;
        QuerySolver querySolver = new QuerySolver(lib, ch.getRandom());
        List<PolyTree> trees = querySolver.simpleUniformGenerate("D => LD", 35, 100);

        SameSizeSubtreeMutation mut = new SameSizeSubtreeMutation(querySolver,35,1.0);

        for (PolyTree tree : trees) {
            PolyTree mutant = mut.mutate_oneTry(tree);
            Log.it(tree);
            Log.it(mutant);
            Log.it("------------------------------------------");
        }



        ch.results();
    }
}
