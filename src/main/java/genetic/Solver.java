/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package genetic;

import paintpot.Colour;

/**
 *
 * @author frank
 */
public class Solver {

    private static final double EPSILON = 0.000001;
    private final Colour target;

    private static final Colour[] targets = new Colour[]{
        new Colour("0 sky", 158, 180, 208),
        new Colour("1 dark sky", 80, 108, 140),
        new Colour("2 grey", 79, 88, 99),
        new Colour("3 tussock", 177, 172, 136),
        new Colour("4 green bush", 47, 61, 58),
        new Colour("5 grey bush", 75, 73, 60),
        new Colour("6 med tussock", 159, 152, 103),
        new Colour("7 dark tussock", 111, 118, 77),
        new Colour("8 light orange", 211, 156, 95),
        new Colour("9 shade orange", 70, 66, 36),
        new Colour("10 orange", 228, 141, 33),
        new Colour("11 dark orange", 157, 95, 46)
    };

    public static void main(String[] args) {
        for (var t : targets) {
            new Solver(t).solve();
        }
    }

    private Solver(Colour target) {
        this.target = target;
    }

    private void solve() {
        var genetic = new Genetic(target, 0.75, 0.6, 5000);
        var maxIter = 50;
        var result = genetic.calculate(maxIter);
        System.out.println("Target = " + target.toString() + " Match = " + result.toString());
        if (result.getValue() > EPSILON) {
            result = HookeJeeves.run(result, 20, 0.05, EPSILON, target);
            System.out.println("Target = " + target.toString() + " HJ Match = " + result.toString());
        }

    }
}
