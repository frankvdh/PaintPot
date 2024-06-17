/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package genetic;

/**
 *
 * @author frank
  */
public interface GeneticInterface {

    Individual combine(Individual i, Individual other, double ratio, boolean doMutate);

    void mutateRandom(Individual i, double min, double max);

    double eval(Individual i);

    @Override
    String toString();
}
