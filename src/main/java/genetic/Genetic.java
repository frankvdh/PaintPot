/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import paintpot.Colour;
import paintpot.Paint;

/**
 *
 * @author frank
 */
public class Genetic {

    private static final double EPSILON = 0.000001;
    private double mutRate;
    private final double initMutRate;
    private final ArrayList<Individual> generation;
    private final int popSize;
    private final int numBreed;
    private final Colour target;
// 9bb6eb
    // 10.74 d0c4c4
    // 10.74 a0b8cb
    // 10.89 9bb6cb
    //       9abdc9
    //       99bcc8
    // 24.79 9bbfcb

    public Genetic(Colour targetValue, double mutRate, double breedFraction, int maxPopSize) {
        this.initMutRate = mutRate;
        this.popSize = maxPopSize;
        this.numBreed = (int) (popSize * breedFraction);
        target = targetValue;
        generation = new ArrayList<>();
        for (var i = 0; i < maxPopSize; i++) {
            var p = new Individual(Paint.random());
            p.eval(target);
            generation.add(p);

        }
    }

    public Individual calculate(int maxIter) {
        int counter = 10;
        Individual prev = null;
        mutRate = initMutRate;
        Paint bestFound = null;
        var bestValue = Double.MAX_VALUE;
        for (var i = 0; i < maxIter; i++) {
            Collections.sort(generation, (a, b) -> b.compareTo(a));
            var best = generation.getFirst();
//            System.out.printf("\nGen %5d", i);
            if (best.getValue() < bestValue) {
                bestFound = new Paint(best.getData());
                bestValue = best.getValue();
                if (bestValue < EPSILON) {
                    break;
                }
            }
            singleIter();
            var current = generation.getFirst();
            if (current == prev) {
                if (--counter > 0) {
                    continue;
                }
                if (mutRate < 100 * initMutRate) {
                    mutRate *= 2;
                }
                var p = generation.getFirst();
                p.mutateRandom(-1.0, 2.0);
                p.eval(target);
                counter = 20;

            } else {
                counter = 10;
                mutRate = initMutRate;
                prev = current;
            }
        }
        return new Individual(bestFound, bestValue);
    }

    private void singleIter() {
//        System.out.printf("N = %d ", generation.size());
//        System.out.print(generation.getFirst().toString());
        var threshold = generation.get(numBreed).getValue();
//        System.out.printf(" -> %5.2f - %5.2f", generation.getFirst().value, threshold);
        var totalBreeds = 0;
        for (var i = 0; i < numBreed; i++) {
            var g = generation.get(i);
            var numBreeds = (int) (Math.round(g.getValue() / threshold + 1));
            g.setNumBreeds(numBreeds);
            totalBreeds += numBreeds;
        }
        TreeMap<Double, Individual> breedProb = new TreeMap<>();
        var cumProb = 0.0;
        for (var i = 0; i < generation.size() / 2; i++) {
            var g = generation.get(i);
            cumProb += g.getNumBreeds() / (double) totalBreeds;
            breedProb.put(cumProb, g);
        }
        breedProb.put(1.0, generation.get(generation.size() / 2 + 1));
//        System.out.printf(" -> %2d ", breedProb.size());
        generation.clear();
        for (var i = 0; i < popSize; i++) {
            var b1 = getRandom(breedProb);
            var b2 = getRandom(breedProb);
            if (b1 == b2) {
                b2 = new Individual(Paint.random());
                b2.eval(target);
            }
            var child = b1.combine(b2, b2.getValue() / b1.getValue(), Math.random() < mutRate);
            child.eval(target);
            generation.add(child);
//        System.out.printf("\nAdd %2d: %s", generation.size(), child);
        }
//        System.out.printf(" Next %d", generation.size());
    }

    private Individual getRandom(TreeMap<Double, Individual> breedProb) {
        var r = Math.random();
//System.out.print(r);
        return breedProb.ceilingEntry(r).getValue();

    }
}
