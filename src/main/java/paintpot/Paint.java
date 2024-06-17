/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package paintpot;

import java.awt.Color;

/**
 *
 * @author frank
 */
public class Paint {

    private static final double EPSILON = 0.001;

    public static Colour[] palette;
    public static int paletteLength;
    public final double[] proportion;
    private Colour mixedColour;

    public static void setPalette(Colour[] palette) {
        Paint.palette = palette;
        paletteLength = palette.length;
    }

    public Paint() {
        proportion = new double[paletteLength];
    }

    public static Paint random() {
        var p = new Paint();
        for (var i = 0; i < p.paletteLength; i++) {
            p.proportion[i] = Math.random();
        }
        p.normalise();
        p.mix();
        return p;
    }

    public Paint(int x) {
        this();
        for (var i = 0; i < paletteLength; i++) {
            proportion[i] = 0;
        }
        proportion[x] = 1;
        mix();
    }

    public Paint(Paint p1, Paint p2, double ratio) {
        this();
        for (var i = 0; i < paletteLength; i++) {
            proportion[i] = Math.random() < ratio ? p1.proportion[i] : p2.proportion[i];
        }
        normalise();
        mix();
    }

    public Paint(Paint p1) {
        this();
        System.arraycopy(p1.proportion, 0, proportion, 0, paletteLength);
        mixedColour = new Colour(p1.getMixedColour());
    }

    public Paint(double[] prop) {
        assert prop.length == paletteLength;
        proportion = prop;
        normalise();
        mix();
    }

    public void add(int x, double t) {
        for (var i = 0; i < paletteLength; i++) {
            proportion[i] *= (1 - t);
        }
        proportion[x] += t;
        mix();
    }

    /**
     * Normalise proportions to range 0.0..1.0 and total 1.0
     *
     */
    public final void normalise() {
        var min = 0.0;
        var total = 0.0;
        for (var i = 0; i < paletteLength; i++) {
            min = Math.min(min, proportion[i]);
            total += proportion[i];
        }
        if (total == 1.0 && min == 0.0) {
            return;
        }
        total -= min * paletteLength;
        for (var i = 0; i < paletteLength; i++) {
            proportion[i] = (proportion[i] - min) / total;
        }
    }

    /**
     * Mix the components of this Palette using Kubelka-Munk
     *
     * @return
     */
    public final Color mix() {
        normalise();
        var mixer = new Mixer();
        var mixedProp = proportion[0];
        mixedColour = new Colour(palette[0]);
        for (var i = 1; i < paletteLength; i++) {
            mixedProp += proportion[i];
            var t = proportion[i] / mixedProp;
            mixedColour = mixer.mix(getMixedColour(), palette[i], t);
        }
        getMixedColour().setName();
        getMixedColour().rgbToLab();
        return getMixedColour();
    }

    final public double eval(Colour target) {
        var numPaints = 0;
        for (var p: proportion) {
            if (p > EPSILON) numPaints++;
        }
        return getMixedColour().deltaESq(target) + numPaints;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder(String.format("#%02x%02x%02x\n", getMixedColour().getRed(), getMixedColour().getGreen(), getMixedColour().getBlue()));
        for (var i = 0; i < paletteLength; i++) {
            if (proportion[i] > EPSILON) {
                sb.append(String.format("%.3f %s\n", proportion[i], palette[i].name()));
            }
        }
        return sb.toString();
    }

    /**
     * @return the mixedColour
     */
    public Colour getMixedColour() {
        return mixedColour;
    }
}
