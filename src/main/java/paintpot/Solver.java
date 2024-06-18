/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package paintpot;

import genetic.Genetic;
import genetic.HookeJeeves;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author frank
 */
public class Solver {

    private static final double EPSILON = 0.001;
    private final Colour target;

    public static final Colour[] initialPalette = new Colour[]{
        new Colour("Reeves Br Red", 219, 14, 8),
        new Colour("Reeves L Green", 8, 141, 34),
        new Colour("Reeves Lemon Yellow", 236, 211, 22),
        new Colour("Reeves Titanium White", 0xf8f7f6),
        new Colour("W-N Mars Black", 58, 56, 56),
        new Colour("W-N Ivory Black", 54, 53, 53),
        new Colour("W-N Mixing White", 0xf7, 0xf5, 0xf2),
        new Colour("W-N Titanium White", 0xfa, 0xfa, 0xf7),
        new Colour("W-N Iradescent White", 0xe4, 0xe2, 0xe3),
        new Colour("W-N Process Cyan", 5, 41, 161),
        new Colour("W-N Phthalo Blue", 11, 45, 90),
        new Colour("W-N Terracotta hell", 226, 153, 76),
        new Colour("W-N Terracotta hell", 226, 153, 76),
        new Colour("Amsterdam Pyrrole red", 216, 19, 13),
        new Colour("Amsterdam Perm red violet", 60, 76, 65),
        new Colour("Talens Yellow Ochre", 190, 81, 14),
        new Colour("Talens Burnt Umber", 59, 45, 42),
        new Colour("Talens Ultramarine", 19, 22, 155),
        new Colour("ASTM Sap Green", 22, 107, 66),
        new Colour("ASTM Naphthol red", 203, 28, 25),
        new Colour("v Gogh Cobalt blue", 15, 33, 157),
        new Colour("ElGreco Carmine Red", 205, 34, 27),
        new Colour("ElGreco Yellow Lemon", 252, 243, 18)};

// https://artistpigments.org/brands/liquitex-heavy-body-acrylic/6v2kz-phthalocyanine-green-blue-shade
//    https://sensuallogic.com/artistcolordata
    private static final Colour[] liquitexHeavyBody = new Colour[]{
        new Colour("Yellow Light Hansa", 0xfcee13),
        new Colour("Naphthol Red Light", 0xd11700),
        //      new Colour("Quinacridone Magenta", ),
        new Colour("Ivory Black", 0x1d1d1d),
        //        new Colour("Emerald Green", ),
        new Colour("Phthaloocyanine Green Blue Shade", 0x002a2b),
        new Colour("Phthaloocyanine Blue Green Shade", 33, 25, 64),
        //        new Colour("Brilliant Blue", ),
        //        new Colour("Bronze Yellow", ),
        new Colour("Dioxazine Purple", 0x231919),
        new Colour("Titanium White", 0xf9faf7),};
// http://www.art-paints.com/Paints/Acrylic/Chromacryl/Students/Warm-Blue/Warm-Blue.html#google_vignette
    private static final Colour[][] chromacryl = new Colour[][]{
        {
            new Colour("Warm Blue", 0x252355),
            new Colour("Cobalt Blue", 0x2c4271),
            new Colour("Cool Blue", 0x2c233e),
            new Colour("Violet", 0x1c103e),
            new Colour("White", 0xf5f5f5),
            new Colour("Silver", 0x5f6269),
            new Colour("Black", 0x261b2b),
            new Colour("Cool Yellow", 0xedd22d),
            new Colour("Warm Yellow", 0xcf9e41),
            new Colour("Yellow Oxide", 0x9f7f42),
            new Colour("Gold", 0x705b2e),
            new Colour("Raw Sienna", 0x6f522a),
            new Colour("Burnt Sienna", 0x412220),
            new Colour("Burnt Umber", 0x291a1d),
            new Colour("Raw Umber", 0x21190c),
            new Colour("Warm Red", 0x87262d),
            new Colour("Cool Red", 0x5a201f),
            new Colour("Red Oxide", 0x601917),
            new Colour("Vermillion", 0xa13b24),
            new Colour("Skin Tone", 0xdbba9b),
            new Colour("Magenta", 0x6a2250),
            new Colour("Deep Green", 0x334338),
            new Colour("Light Green", 0x2c7236),
            new Colour("Forest Green", 0x252620),},
        //WarehouseStationery
        {
            new Colour("Silver", 0x5f6269),
            new Colour("Red Oxide", 0x601917),
            // new Colour("Fluoro Green", ),
            new Colour("Gold", 0x705b2e),
            new Colour("Magenta", 0x6a2250),
            new Colour("Forest Green", 0x252620),
            // new Colour("Fluoro Yellow", ),
            // new Colour("Fluoro Orange", ),
            // new Colour("Fluoro Pink", ),
            new Colour("White", 0xf5f5f5),
            new Colour("Burnt Umber", 0x291a1d),
            new Colour("Skin Tone", 0xdbba9b),
            new Colour("Yellow Oxide", 0x9f7f42),
            new Colour("Black", 0x261b2b),
            new Colour("Cool Red", 0x5a201f),
            new Colour("Deep Green", 0x334338),
            // new Colour("Neutral Grey", ),
            new Colour("Cobalt Blue", 0x2c4271),
            // new Colour("Gold Oxide", ),
            new Colour("Warm Yellow", 0xcf9e41),
            new Colour("Warm Red", 0x87262d),
            new Colour("Light Green", 0x2c7236),
            new Colour("Raw Umber", 0x21190c),
            new Colour("Vermillion", 0xa13b24),
            new Colour("Cool Yellow", 0xedd22d),
            new Colour("Warm Blue", 0x252355),
            new Colour("Violet", 0x1c103e),
            new Colour("Cool Blue", 0x2c233e),
            new Colour("Burnt Sienna", 0x412220),
            new Colour("Raw Sienna", 0x6f522a),},
        // Students 10-pack
        {
            new Colour("White", 0xf5f5f5),
            new Colour("Cool Yellow", 0xedd22d),
            new Colour("Warm Yellow", 0xcf9e41),
            new Colour("Warm Red", 0x87262d),
            new Colour("Cool Red", 0x5a201f),
            new Colour("Red Oxide", 0x601917),
            new Colour("Warm Blue", 0x252355),
            new Colour("Cool Blue", 0x2c233e),
            new Colour("Yellow Oxide", 0x9f7f42),
            new Colour("Black", 0x261b2b),},
        // Students 5-pack
        {
            new Colour("White", 0xf5f5f5),
            new Colour("Cool Yellow", 0xedd22d),
            new Colour("Cool Red", 0x5a201f),
            new Colour("Cool Blue", 0x2c233e),
            new Colour("Black", 0x261b2b),},
        // Artists
        {
            new Colour("Cool Red", 0xb11d13),
            new Colour("Cool Yellow", 0xe9e60f),
            new Colour("Cool Blue", 0x154f99),
            new Colour("Black", 0x46423f),
            new Colour("White", 0xe9e8e6),},
        // School
        {
            new Colour("Cool Red", 0x950026),
            new Colour("Cool Yellow", 0xffe902),
            new Colour("Cool Blue", 0x000062),
            new Colour("Black", 0x040404),
            new Colour("White", 0xeeebeb), // 12 colours in all
        }
    };

    private static final Colour[] targets = new Colour[]{
        new Colour("4 green bush", 47, 61, 58),
        new Colour("2 grey", 79, 88, 99),
        new Colour("0 sky", 158, 180, 208),
        new Colour("6 med tussock", 159, 152, 103),
        new Colour("8 light orange", 211, 156, 95),
        new Colour("3 tussock", 177, 172, 136),
        new Colour("1 dark sky", 80, 108, 140),
        new Colour("7 dark tussock", 111, 118, 77),
        new Colour("5 grey bush", 75, 73, 60),
        new Colour("9 shade orange", 70, 66, 36),
        new Colour("11 dark orange", 157, 95, 46),
        new Colour("10 orange", 228, 141, 33),
    };

    private static final ArrayList<Colour> palette = new ArrayList<>();
    private static final HashMap<String, Integer> neededPaints = new HashMap<>();

    public static void main(String[] args) {
        palette.addAll(Arrays.asList(chromacryl[1]));
        for (var t : targets) {
            var thisPalette = new Colour[palette.size()];
            for (var i = 0; i < thisPalette.length; i++) {
                thisPalette[i] = palette.get(i);
            }
            var c = new Solver(t).solve(thisPalette);
            c.setName(t.name());
            palette.add(c);
        }
        System.out.println("Needed paints: " + neededPaints.size());
        for (var p: neededPaints.entrySet()) {
            System.out.println(p.getKey() + ": " + p.getValue());
        }
        System.out.println();
    }

    private Solver(Colour target) {
        this.target = target;
    }

    private Colour solve(Colour[] palette) {
        Paint.setPalette(palette);
        var genetic = new Genetic(target, 0.75, 0.6, 5000);
        var maxIter = 100;
        var result = genetic.calculate(maxIter);
        if (result.getValue() > EPSILON) {
            result = HookeJeeves.run(result, 20, 0.05, EPSILON, genetic);
        }
        for (var i = 0; i < result.getData().proportion.length; i++) {
            var p = result.getData().proportion[i];
            if (p < EPSILON) continue;
            var name = palette[i].name();
            if (name.matches("^\\d.*")) continue;
            if (neededPaints.containsKey(name))
               neededPaints.replace(name, neededPaints.get(name) + 1);
            else
               neededPaints.put(name, 1);
        }
        System.out.println("Target = " + target.toString() + " Match = " + result.toString());
        return result.getData().getMixedColour();
    }
}
