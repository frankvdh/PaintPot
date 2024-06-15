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
public class HookeJeeves {

    public static Individual run(Individual par,
            final int maxIter,
            final double startStep,
            final double endStep,
            final Colour target) {
        var currentStep = startStep;
        var parValue = par.getValue();
        boolean changed = false;
        for (var i = 0; i < maxIter; i++) {
//            System.out.printf("%f %f\n", par.value, currentStep);
            for (var j = 0; j < Paint.primary.length; j++) {
                var test = new Individual(par);
                test.mutate(j, -currentStep);
                var testValue = test.eval(target);
                if (testValue < parValue) {
                    parValue = testValue;
                    par = test;
                    changed = true;
                    continue;
                }
                test = new Individual(par);
                test.mutate(j, currentStep);
                testValue = test.eval(target);
                if (testValue < parValue) {
                    parValue = testValue;
                    par = test;
                    changed = true;
                }
            }
            if (!changed) {
                currentStep /= 2.0;
                if (currentStep < endStep) {
                    break;
                }
//                System.out.printf("%f -> %f\n", parv.value, par.value);
            }
        }
        return par;
    }
}
