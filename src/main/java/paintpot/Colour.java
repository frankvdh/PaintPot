/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package paintpot;

import java.awt.Color;

/**
 *
 * @author frank
 */
public class Colour extends Color {

    private String name;
    // RYB Color Model components
    final private double[] lab = new double[3];

    public Colour(String name, int R, int G, int B) {
        super(R, G, B);
        this.name = name;
        rgbToLab();
    }

    public Colour(int R, int G, int B, int A) {
        this(String.format("#%02x%02x%02x", R, G, B), R, G, B);
    }

    public Colour(float R, float G, float B, float A) {
        this(Math.round(R * 255), Math.round(G * 255), Math.round(B * 255), Math.round(A * 255));
    }

    public Colour(Colour c) {
        super(c.getRed(), c.getGreen(), c.getBlue());
        this.name = c.name;
        System.arraycopy(c.lab, 0, lab, 0, lab.length);
    }

    public void setName() {
        name = String.format("#%02x%02x%02x", getRed(), getGreen(), getBlue());
    }

    public String name() {
        return name;
    }

    public double simpleColourDifference(Color target) {
        var rMean = ((long) getRed() + target.getRed()) / 2;
        var r = (long) getRed() - target.getRed();
        var g = (long) getGreen() - target.getGreen();
        var b = (long) getBlue() - target.getBlue();
        return Math.sqrt((((512 + rMean) * r * r) >> 8) + 4 * g * g + (((767 - rMean) * b * b) >> 8));
    }

    Color labToRgb(double[] lab) {
        var y = (lab[0] + 16) / 116;
        var x = lab[1] / 500 + y;
        var z = y - lab[2] / 200;
        double r, g, b;

        x = 0.95047 * ((x * x * x > 0.008856) ? x * x * x : (x - 16 / 116) / 7.787);
        y = 1.00000 * ((y * y * y > 0.008856) ? y * y * y : (y - 16 / 116) / 7.787);
        z = 1.08883 * ((z * z * z > 0.008856) ? z * z * z : (z - 16 / 116) / 7.787);

        r = x * 3.2406 + y * -1.5372 + z * -0.4986;
        g = x * -0.9689 + y * 1.8758 + z * 0.0415;
        b = x * 0.0557 + y * -0.2040 + z * 1.0570;

        r = (r > 0.0031308) ? (1.055 * Math.pow(r, 1 / 2.4) - 0.055) : 12.92 * r;
        g = (g > 0.0031308) ? (1.055 * Math.pow(g, 1 / 2.4) - 0.055) : 12.92 * g;
        b = (b > 0.0031308) ? (1.055 * Math.pow(b, 1 / 2.4) - 0.055) : 12.92 * b;

        return new Color(Math.max(0, Math.min(1, (float) r)) * 255,
                Math.max(0, Math.min(1, (float) g)) * 255,
                Math.max(0, Math.min(1, (float) b)) * 255);
    }

    final public void rgbToLab() {
        double r = getRed() / 255.0;
        double g = getGreen() / 255.0;
        double b = getBlue() / 255.0;
        double x, y, z;

        r = (r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
        g = (g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
        b = (b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

        x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.95047;
        y = (r * 0.2126 + g * 0.7152 + b * 0.0722) / 1.00000;
        z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.08883;

        x = (x > 0.008856) ? Math.pow(x, 1 / 3.0) : (7.787 * x) + 16 / 116.0;
        y = (y > 0.008856) ? Math.pow(y, 1 / 3.0) : (7.787 * y) + 16 / 116.0;
        z = (z > 0.008856) ? Math.pow(z, 1 / 3.0) : (7.787 * z) + 16 / 116.0;

        lab[0] = (116 * y) - 16;
        lab[1] = 500 * (x - y);
        lab[2] = 200 * (y - z);
    }

    public double deltaE(Colour other) {
        return Math.sqrt(deltaESq(other));
    }

// calculate the perceptual distance between colors in CIELAB
// https://github.com/THEjoezack/ColorMine/blob/master/ColorMine/ColorSpaces/Comparisons/Cie94Comparison.cs
    public double deltaESq(Colour other) {
        var deltaL = lab[0] - other.lab[0];
        var deltaA = lab[1] - other.lab[1];
        var deltaB = lab[2] - other.lab[2];
        var c1 = Math.sqrt(lab[1] * lab[1] + lab[2] * lab[2]);
        var deltaC = c1 - Math.sqrt(other.lab[1] * other.lab[1] + other.lab[2] * other.lab[2]);
        var deltaH = deltaA * deltaA + deltaB * deltaB - deltaC * deltaC;
        deltaH = deltaH < 0 ? 0 : Math.sqrt(deltaH);
        var deltaCkcsc = deltaC / (1.0 + 0.045 * c1);
        var deltaHkhsh = deltaH / (1.0 + 0.015 * c1);
        return deltaL * deltaL + deltaCkcsc * deltaCkcsc + deltaHkhsh * deltaHkhsh;
    }

    @Override
    public String toString() {
        return String.format("%s: #%02x%02x%02x", name, getRed(), getGreen(), getBlue());
    }
}
