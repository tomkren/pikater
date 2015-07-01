package cz.tomkren.typewars.eva;

public class EvoOpts {

    public static final EvoOpts DEFAULT = new EvoOpts(50, 51, 500, false);

    private final int numRuns;
    private final int numGens;
    private final int popSize;
    private final boolean saveBest;

    public EvoOpts(int numRuns, int numGens, int popSize, boolean saveBest) {
        this.numRuns = numRuns;
        this.numGens = numGens;
        this.popSize = popSize;
        this.saveBest = saveBest;
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

}
