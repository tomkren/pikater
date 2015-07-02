package cz.tomkren.typewars.eva;

import cz.tomkren.helpers.AB;
import cz.tomkren.helpers.F;
import cz.tomkren.helpers.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Distribution<T extends Probable> {

    private double sum;
    private int    len;
    private List<T> xs;

    private T best;
    private double pBest;
    private List<Consumer<T>> bestListeners;





    public T tournamentGet(double pReturnWinner, Random rand) {

        T x1 = F.randomElement(xs, rand);
        T x2 = F.randomElement(xs, rand);

        boolean x1wins = x1.getProbability() > x2.getProbability();

        if (rand.nextDouble() <= pReturnWinner) {
            return x1wins ? x1 : x2;
        } else {
            return x1wins ? x2 : x1;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        List<T> xsSorted = F.sort(xs, x -> -x.getProbability());

        for (T x : xsSorted) {
            sb.append("  ").append(x.getProbability()).append("\t").append(x.toString()).append("\n");
        }

        return sb.toString();
    }

    public void addBestListener(Consumer<T> bestListener) {
        if (bestListeners == null) {
            bestListeners = new ArrayList<>();
        }
        bestListeners.add(bestListener);
    }

    private void init() {
        len = 0;
        sum = 0;
        pBest = 0;
    }

    public Distribution() {
        init();
        xs = new ArrayList<>();
    }

    public Distribution(List<T> xs) {
        init();
        this.xs = xs;
        xs.forEach(this::addUpdate);
        len = xs.size();
    }

    public Distribution<T> add(T x) {
        xs.add(x);
        addUpdate(x);
        len ++;
        return this;
    }

    public Distribution<T> addAll(List<T> xs) {
        xs.forEach(this::add);
        return this;
    }

    public int size() {
        return len;
    }



    public void forEach(Consumer<T> f) {
        xs.forEach(f::accept);
    }

    public void forEach(BiConsumer<T,Double> f) {
        for (T x : xs) {
            f.accept(x,x.getProbability());
        }
    }

    private T getA(int i) {
        return xs.get(i);
    }
    private double getP(int i) {
        return xs.get(i).getProbability();
    }

    private void addUpdate(T x) {
        double p = x.getProbability();
        checkP(p);
        sum += p;
        if (p >= pBest) {
            best  = x;
            pBest = p;

            if (bestListeners != null) {
                for (Consumer<T> bestListener : bestListeners) {
                    bestListener.accept(best);
                }
            }
        }
    }

    private void checkP(double p) {
        if (p < 0) {
            throw new Error("Probability for dist mus be >= 0, was "+p);
        }
    }

    public T get(Random r) {

        if (sum == 0.0) {
            Log.it("Warning : sum == 0 in Distribution");
            return F.randomElement(xs,r);
        }

        double ball = /*Math.random()*/ r.nextDouble()  * sum;
        double sumNow = 0;
        int i;
        for (i = 0; i < len; i++) {
            sumNow += getP(i);
            if (ball < sumNow) {
                break;
            }
        }
        return getA(i);
    }

    public double getPSum() {
        return sum;
    }

    public double avgVal() {
        return sum/len;
    }

    public T getBest() {
        return best;
    }


    public static void main(String[] args) {

        double[] ps = {1.,2.,4.,8.,16.};
        int[]    ns = {0, 0, 0, 0, 0  };
        int      n  = 31000000;
        double sum  = 0;

        for (double p : ps) {
            sum += p;
        }

        List<AB<Integer,Double>> xs = new ArrayList<>(Arrays.asList(AB.mk(0, ps[0]), AB.mk(1, ps[1]), AB.mk(2, ps[2])));
        Distribution<AB<Integer,Double>> d = new Distribution<>(xs);
        d.add(AB.mk(4,ps[4])).add(AB.mk(3, ps[3]));

        Random rand = new Random();

        for (int i = 0; i<n; i++) {
            int j = (int) ((AB) d.get(rand))._1();
            ns[j]++;
        }


        Log.it("Nameřeno:");
        Log.it("\npst 1: "+ns[0]+"\npst 2: "+ns[1]+"\npst 4: "+ns[2]+"\npst 8: "+ns[3]+"\npst 16: "+ns[4]+"\n");
        Log.it("Expected:");
        Log.it("\npst 1: "+(n*ps[0]/sum)+"\npst 2: "+(n*ps[1]/sum)+"\npst 4: "+(n*ps[2]/sum)+"\npst 8: "+(n*ps[3]/sum)+"\npst 16: "+(n*ps[4]/sum)+"\n");
    }


}
