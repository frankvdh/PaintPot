/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package genetic;

import paintpot.Colour;

/**
 *
 * @author frank
 * @param <T>
 */
public interface GeneticInterface<T> {

    Individual combine(Individual other, double ratio, boolean doMutate);

    void mutateRandom(double min, double max);

    double eval(Colour target);

    @Override
    String toString();
}
