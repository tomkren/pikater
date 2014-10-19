package org.pikater.core.utilities.evolution.operators;

import org.pikater.core.ontology.subtrees.newoption.values.DoubleValue;
import org.pikater.core.utilities.evolution.Population;
import org.pikater.core.utilities.evolution.RandomNumberGenerator;
import org.pikater.core.utilities.evolution.individuals.Individual;
import org.pikater.core.utilities.evolution.individuals.RealIndividual;

/**
 * Simulated binary crossover operator. 
 * 
 * Performs the crossover operation described in:
 * 
 * Deb, Kalyanmoy and Ram B Agrawal (1994). �Simulated Binary Crossover for Continuous
 * Search Space.� In: Complex Systems 9, pp. 1�34
 * 
 * @author Martin Pilat
 */

public class SBXoverOperator implements Operator {

    private double xoverRate;

    private static final double EPS = 0.00001;
    private static final double ETA_C = 20;

    /**
     * Constructor, sets the crossover rate.
     * 
     * @param crossRate the crossover rate
     */

    public SBXoverOperator(double crossRate) {
        xoverRate = crossRate;
    }

    public void operate(Population parents, Population offspring) {
        int popSize = parents.getPopulationSize();
        for(int i = 0; i < popSize - 1; i+=2) {
            Individual p1 = parents.get(i);
            Individual p2 = parents.get(i+1);
            RealIndividual ch1 = (RealIndividual)p1.clone();
            RealIndividual ch2 = (RealIndividual)p2.clone();
            if (RandomNumberGenerator.getInstance().nextDouble() < xoverRate) {
                cross(ch1, ch2);
            }
            offspring.add(ch1);
            offspring.add(ch2);
        }
    }

    /**
     * Performs the crossover of itself.
     * 
     * @param a First parent, is changed in the method
     * @param b Second parent, is changed in the method
     */

    private void cross(RealIndividual a, RealIndividual b) {
        double y1, y2, yLow, yHi, tmp;
        for (int i = 0; i < a.length(); i++) {
            y1 = (Double)a.get(i);
            y2 = (Double)b.get(i);
            if (Math.abs(y1 - y2) < EPS){
                continue;
            }
            if (y1 > y2) {
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }
            yLow = a.getMin();
            yHi = a.getMax();
            double rand = RandomNumberGenerator.getInstance().nextDouble();
            double beta = 1.0 + (2.0*(y1-yLow)/(y2-y1));
            double alpha = 2.0 - Math.pow(beta,-(ETA_C + 1.0));
            double betaq = 0;
            if (rand <= (1.0/alpha)) {
                betaq = Math.pow(rand*alpha, 1.0 / (ETA_C + 1.0));
            } else {
                betaq = Math.pow(1.0 / (2.0 - rand*alpha), 1.0 / (ETA_C + 1.0));
            }
            double c1 = 0.5*((y1+y2)-betaq*(y2-y1));
            beta = 1.0 + (2.0*(yHi-y2)/(y2-y1));
            alpha = 2.0 - Math.pow(beta,-(ETA_C+1.0));
            if (rand <= (1.0/alpha)) {
                betaq = Math.pow(rand*alpha, 1.0 / (ETA_C + 1.0));
            } else {
                betaq = Math.pow(1.0 / (2.0 - rand*alpha), 1.0 / (ETA_C + 1.0));
            }
            double c2 = 0.5*((y1+y2)+betaq*(y2-y1));
            if (c1<yLow) {
                c1=yLow;
            }
            if (c2<yLow) {
                c2=yLow;
            }
            if (c1>yHi) {
                c1=yHi;
            }
            if (c2>yHi) {
                c2=yHi;
            }
            if (RandomNumberGenerator.getInstance().nextDouble()<=0.5) {
                a.set(i, new DoubleValue(c2));
                b.set(i, new DoubleValue(c1));
            } else {
                a.set(i, new DoubleValue(c1));
                b.set(i, new DoubleValue(c2));
            }
        }
    }
}
