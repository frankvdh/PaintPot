/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package paintpot;

/**
 *
 * @author frank
 */
public class Rgb {

    float r, g, b;

    Rgb(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    void addTerm(double r, double g, double b, double mult) {
        this.r += r * mult;
        this.g += g * mult;
        this.b += b * mult;
    }
}
