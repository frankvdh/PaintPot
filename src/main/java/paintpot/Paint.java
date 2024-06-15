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

    public static final Colour[] primary = new Colour[]{
        new Colour("Reeves Br Red", 219, 14, 8),
        new Colour("Reeves L Green", 8, 141, 34),
        new Colour("Reeves Lemon Yellow", 236, 211, 22),
        new Colour("W-N Mars Black", 58, 56, 56),
        new Colour("W-N Ivory Black", 54, 53, 53),
        new Colour("W-N Mixing White", 0xf7, 0xf5, 0xf2),
        new Colour("W-N Titanium White", 0xfa, 0xfa, 0xf7),
        new Colour("W-N Iradescent White", 0xe4, 0xe2, 0xe3),
        new Colour("W-N Process Cyan", 5, 41, 161),
        new Colour("W-N Phthalo Blue", 11, 45, 90),
        new Colour("W-N Terracotta hell", 226, 153, 76),
        new Colour("Amsterdam Perm red violet", 60, 76, 65),
        new Colour("Talens Yellow Ochre", 190, 81, 14),
        new Colour("Amsterdam Pyrrole red", 216, 19, 13),
        new Colour("W-N Terracotta hell", 226, 153, 76),
        new Colour("Talens Burnt Umber", 59, 45, 42),
        new Colour("ElGreco Carmine Red", 205, 34, 27),
        new Colour("ASTM Sap Green", 22, 107, 66),
        new Colour("ASTM Naphthol red", 203, 28, 25),
        new Colour("v Gogh Cobalt blue", 15, 33, 157),
        new Colour("Talens Ultramarine", 19, 22, 155),
        new Colour("ElGreco Yellow Lemon", 252, 243, 18)};

    public final double[] proportion = new double[primary.length];
    private Colour mixedColour;

    public static Paint random() {
        var p = new Paint();
        for (var i = 0; i < p.proportion.length; i++) {
            p.proportion[i] = Math.random();
        }
        p.normalise();
        p.mix();
        return p;
    }

    public Paint() {
    }

    public Paint(int x) {
        for (var i = 0; i < proportion.length; i++) {
            proportion[i] = 0;
        }
        proportion[x] = 1;
        mix();
    }

    public Paint(Paint p1, Paint p2, double ratio) {
        for (var i = 0; i < proportion.length; i++) {
            proportion[i] = Math.random() < ratio ? p1.proportion[i] : p2.proportion[i];
        }
        normalise();
        mix();
    }

    public Paint(Paint p1) {
        System.arraycopy(p1.proportion, 0, proportion, 0, proportion.length);
        mixedColour = new Colour(p1.getMixedColour());
    }

    public void add(int x, double t) {
        for (var i = 0; i < proportion.length; i++) {
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
        for (var i = 0; i < proportion.length; i++) {
            min = Math.min(min, proportion[i]);
            total += proportion[i];
        }
        if (total == 1.0 && min == 0.0) {
            return;
        }
        total -= min * proportion.length;
        for (var i = 0; i < proportion.length; i++) {
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
        mixedColour = new Colour(primary[0]);
        for (var i = 1; i < proportion.length; i++) {
            mixedProp += proportion[i];
            var t = proportion[i] / mixedProp;
            mixedColour = mixer.mix(getMixedColour(), primary[i], t);
        }
        getMixedColour().setName();
        getMixedColour().rgbToLab();
        return getMixedColour();
    }

    final public double eval(Colour target) {
        return getMixedColour().deltaESq(target);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder(String.format("#%02x%02x%02x\n", getMixedColour().getRed(), getMixedColour().getGreen(), getMixedColour().getBlue()));
        for (var i = 0; i < proportion.length; i++) {
            if (proportion[i] > EPSILON) {
                sb.append(String.format("%.3f %s\n", proportion[i], primary[i].name()));
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
