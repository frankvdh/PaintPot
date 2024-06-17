/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package genetic;

import paintpot.Paint;

/**
 *
 * @author frank
 */
public class Individual implements Comparable<Individual> {

    double value;
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
        return String.format("%.2f, %s", getValue(), getData().toString());
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
