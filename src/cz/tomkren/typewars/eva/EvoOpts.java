package cz.tomkren.typewars.eva;

public class EvoOpts {

    public static final EvoOpts DEFAULT = new EvoOpts(50, 51, 500, false);

    private final int numRuns;
    private final int numGens;
    private final int popSize;

    private final boolean saveBest;

    private final boolean performUniqueCheck;
    private final int maxNumUniqueCheckFails;



    public EvoOpts(int numRuns, int numGens, int popSize, boolean saveBest, boolean performUniqueCheck, int maxNumUniqueCheckFails) {
        this.numRuns = numRuns;
        this.numGens = numGens;
        this.popSize = popSize;
        this.saveBest = saveBest;
        this.performUniqueCheck = performUniqueCheck;
        this.maxNumUniqueCheckFails = maxNumUniqueCheckFails;
    }

    public EvoOpts(int numRuns, int numGens, int popSize, boolean saveBest) {
        this(numRuns, numGens, popSize, saveBest, true, popSize);
    }

    public EvoOpts(int numGens, int popSize, boolean saveBest) {
        this(1, numGens, popSize, saveBest);
    }

    public int getNumRuns() {
        return numRuns;
    }
    public int getNumGens() {
        return numGens;
    }
    public int getPopSize() {
        return popSize;
    }
    public boolean isSaveBest() {
        return saveBest;
    }

    boolean isUniqueCheckPerformed() {
        return performUniqueCheck;
    }
    int getMaxNumUniqueCheckFails() {
        return maxNumUniqueCheckFails;
    }

}
