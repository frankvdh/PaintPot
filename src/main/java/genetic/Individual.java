/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package genetic;

import paintpot.Colour;
import paintpot.Paint;

/**
 *
 * @author frank
 */
public class Individual implements Comparable<Individual>, GeneticInterface<Paint> {

    private double value;
    private int numBreeds;
    private Paint data;

    public Individual(Paint p) {
        data = p;
    }

    public Individual(Paint p, double value) {
        data = p;
        this.value = value;
    }

    public Individual(Individual p) {
        this(new Paint(p.getData()));
    }

    @Override
    final public double eval(Colour target) {
        value = getData().eval(target);
        return getValue();
    }

    @Override
    public void mutateRandom(double min, double max) {
        mutate((int) (Math.random() * getData().proportion.length), getRandomBetween(min, max));
    }

    public void mutate(int index, double value) {
        value += getData().proportion[index];
        data.proportion[index] = value < 0 ? 0 : value;
        getData().normalise();
        getData().mix();
    }

    @Override
    public int compareTo(Individual other) {
        return (int) Math.signum(other.getValue() - getValue());
    }

    @Override
    public String toString() {
        return String.format("%.0f%%, %s", (1 - getValue()) * 100, getData().toString());
    }

    @Override
    public Individual combine(Individual mate, double ratio, boolean doMutate) {
        var child = new Individual(new Paint(getData(), mate.getData(), ratio));
        if (doMutate) {
            child.mutateRandom(-1.0, 1.0);
        }
        return child;
    }

    public static double getRandomBetween(final double min, final double max) {
        return min + (Math.random() * (max - min));
    }

    /**
     * @return the data
     */
    public Paint getData() {
        return data;
    }

    /**
     * @return the numBreeds
     */
    public int getNumBreeds() {
        return numBreeds;
    }

    /**
     * @param numBreeds the numBreeds to set
     */
    public void setNumBreeds(int numBreeds) {
        this.numBreeds = numBreeds;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

}
