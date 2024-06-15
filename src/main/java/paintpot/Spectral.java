/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package paintpot;

import java.awt.Color;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 *
 * @author frank -- translated from JavaScript https://github.com/rvanwijnen/spectral.js
 */
public class Spectral {
//  MIT License
//
//  Copyright (c) 2023 Ronald van Wijnen
//
//  Permission is hereby granted, free of charge, to any person obtaining a
//  copy of this software and associated documentation files (the "Software"),
//  to deal in the Software without restriction, including without limitation
//  the rights to use, copy, modify, merge, publish, distribute, sublicense,
//  and/or sell copies of the Software, and to permit persons to whom the
//  Software is furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
//  DEALINGS IN THE SOFTWARE.

    private static final int SIZE = 38;
    private static final double GAMMA = 2.4;
    private static final double EPSILON = 0.0001;

    // K1 and K2 are used for the Saunderson correction which is used for how the color looks behind a refractive
    // surface like glass
    //
    // 1 is used for a vacuum, no surface
    //
    // Refractive Index
    //
    // https://en.wikipedia.org/wiki/Refractive_index
    // https://en.wikipedia.org/wiki/List_of_refractive_indices
    //
    // Glass = 1.5
    // Vacuum = 1
    private static final double RI = 1.0;
    private static final double K1 = pow(RI - 1, 2) / pow(RI + 1, 2);

    // 0 = neutral, - = lighten, + = darken
    private static final int K2 = 0;

    public Color spectral_mix(Color c1, Color c2, float t) {
        Vec4 color1 = new Vec4(c1);
        Vec4 color2 = new Vec4(c2);
        var newColour = spectral_mix(new Rgb(color1.r, color1.g, color1.b), new Rgb(color2.r, color2.g, color2.b), t);
        var v = new Vec4(newColour, color1.a * t + color2.a * (1 - t));
        return new Color(v.r, v.g, v.b, v.a);
    }

    private static final double[][] XYZ_RGB = {
        new double[]{3.24306333, -1.53837619, -0.49893282},
        new double[]{-0.96896309, 1.87542451, 0.04154303},
        new double[]{0.05568392, -0.20417438, 1.05799454},};

    private static float dot(double[] row, Rgb col) {
        return (float) (row[0] * col.r + row[1] * col.g + row[2] * col.b);
    }

    private static Rgb spectral_xyz_to_srgb(Rgb xyz) {
        return spectral_linear_to_srgb(new Rgb(dot(XYZ_RGB[0], xyz), dot(XYZ_RGB[1], xyz), dot(XYZ_RGB[2], xyz)));
    }

    private static float clamp(float value, float min, float max) {
        return min(max(value, min), max);
    }

    private static float spectral_uncompand(double x) {
        return (float) ((x < 0.04045) ? x / 12.92 : pow((x + 0.055) / 1.055, GAMMA));
    }

    private static float spectral_compand(float x) {
        return clamp((float) ((x < 0.0031308) ? x * 12.92 : 1.055 * pow(x, 1.0 / GAMMA) - 0.055), 0, 1);
    }

    private static Rgb spectral_srgb_to_linear(Rgb srgb) {
        return new Rgb(spectral_uncompand(srgb.r), spectral_uncompand(srgb.g), spectral_uncompand(srgb.b));
    }

    private static Rgb spectral_linear_to_srgb(Rgb lrgb) {
        return new Rgb(spectral_compand(lrgb.r), spectral_compand(lrgb.g), spectral_compand(lrgb.b));
    }

    private static double[] spectral_linear_to_reflectance(Rgb lrgb) {
        // Spectral upsampling
        var w = min(lrgb.r, min(lrgb.g, lrgb.b));
        lrgb.r -= w;
        lrgb.g -= w;
        lrgb.b -= w;
        var c = min(lrgb.g, lrgb.b);
        var m = min(lrgb.r, lrgb.b);
        var y = min(lrgb.r, lrgb.g);
        var r = min(max(0, lrgb.r - lrgb.b), max(0, lrgb.r - lrgb.g));
        var g = min(max(0, lrgb.g - lrgb.b), max(0, lrgb.g - lrgb.r));
        var b = min(max(0, lrgb.b - lrgb.g), max(0, lrgb.b - lrgb.r));

        var R = new double[SIZE];
        R[0] = max(EPSILON, w + c * 0.96853629 + m * 0.51567122 + y * 0.02055257 + r * 0.03147571 + g * 0.49108579 + b * 0.97901834);
        R[1] = max(EPSILON, w + c * 0.96855103 + m * 0.54015520 + y * 0.02059936 + r * 0.03146636 + g * 0.46944057 + b * 0.97901649);
        R[2] = max(EPSILON, w + c * 0.96859338 + m * 0.62645502 + y * 0.02062723 + r * 0.03140624 + g * 0.40165780 + b * 0.97901118);
        R[3] = max(EPSILON, w + c * 0.96877345 + m * 0.75595012 + y * 0.02073387 + r * 0.03119611 + g * 0.24490420 + b * 0.97892146);
        R[4] = max(EPSILON, w + c * 0.96942204 + m * 0.92826996 + y * 0.02114202 + r * 0.03053888 + g * 0.06826880 + b * 0.97858555);
        R[5] = max(EPSILON, w + c * 0.97143709 + m * 0.97223624 + y * 0.02233154 + r * 0.02856855 + g * 0.02732883 + b * 0.97743705);
        R[6] = max(EPSILON, w + c * 0.97541862 + m * 0.98616174 + y * 0.02556857 + r * 0.02459485 + g * 0.01360600 + b * 0.97428075);
        R[7] = max(EPSILON, w + c * 0.98074186 + m * 0.98955255 + y * 0.03330189 + r * 0.01929520 + g * 0.01000187 + b * 0.96663223);
        R[8] = max(EPSILON, w + c * 0.98580992 + m * 0.98676237 + y * 0.05185294 + r * 0.01423112 + g * 0.01284127 + b * 0.94822893);
        R[9] = max(EPSILON, w + c * 0.98971194 + m * 0.97312575 + y * 0.10087639 + r * 0.01033111 + g * 0.02636635 + b * 0.89937713);
        R[10] = max(EPSILON, w + c * 0.99238027 + m * 0.91944277 + y * 0.24000413 + r * 0.00765876 + g * 0.07058713 + b * 0.76070164);
        R[11] = max(EPSILON, w + c * 0.99409844 + m * 0.32564851 + y * 0.53589066 + r * 0.00593693 + g * 0.70421692 + b * 0.46420440);
        R[12] = max(EPSILON, w + c * 0.99517200 + m * 0.13820628 + y * 0.79874659 + r * 0.00485616 + g * 0.85473994 + b * 0.20123039);
        R[13] = max(EPSILON, w + c * 0.99576545 + m * 0.05015143 + y * 0.91186529 + r * 0.00426186 + g * 0.95081565 + b * 0.08808402);
        R[14] = max(EPSILON, w + c * 0.99593552 + m * 0.02912336 + y * 0.95399623 + r * 0.00409039 + g * 0.97170370 + b * 0.04592894);
        R[15] = max(EPSILON, w + c * 0.99564041 + m * 0.02421691 + y * 0.97137099 + r * 0.00438375 + g * 0.97651888 + b * 0.02860373);
        R[16] = max(EPSILON, w + c * 0.99464769 + m * 0.02660696 + y * 0.97939505 + r * 0.00537525 + g * 0.97429245 + b * 0.02060067);
        R[17] = max(EPSILON, w + c * 0.99229579 + m * 0.03407586 + y * 0.98345207 + r * 0.00772962 + g * 0.97012917 + b * 0.01656701);
        R[18] = max(EPSILON, w + c * 0.98638762 + m * 0.04835936 + y * 0.98553736 + r * 0.01366120 + g * 0.94258630 + b * 0.01451549);
        R[19] = max(EPSILON, w + c * 0.96829712 + m * 0.00011720 + y * 0.98648905 + r * 0.03181352 + g * 0.99989207 + b * 0.01357964);
        R[20] = max(EPSILON, w + c * 0.89228016 + m * 0.00008554 + y * 0.98674535 + r * 0.10791525 + g * 0.99989891 + b * 0.01331243);
        R[21] = max(EPSILON, w + c * 0.53740239 + m * 0.85267882 + y * 0.98657555 + r * 0.46249516 + g * 0.13823139 + b * 0.01347661);
        R[22] = max(EPSILON, w + c * 0.15360445 + m * 0.93188793 + y * 0.98611877 + r * 0.84604333 + g * 0.06968113 + b * 0.01387181);
        R[23] = max(EPSILON, w + c * 0.05705719 + m * 0.94810268 + y * 0.98559942 + r * 0.94275572 + g * 0.05628787 + b * 0.01435472);
        R[24] = max(EPSILON, w + c * 0.03126539 + m * 0.94200977 + y * 0.98507063 + r * 0.96860996 + g * 0.06111561 + b * 0.01479836);
        R[25] = max(EPSILON, w + c * 0.02205445 + m * 0.91478045 + y * 0.98460039 + r * 0.97783966 + g * 0.08987709 + b * 0.01515250);
        R[26] = max(EPSILON, w + c * 0.01802271 + m * 0.87065445 + y * 0.98425301 + r * 0.98187757 + g * 0.13656016 + b * 0.01540513);
        R[27] = max(EPSILON, w + c * 0.01613460 + m * 0.78827548 + y * 0.98403909 + r * 0.98377315 + g * 0.22169624 + b * 0.01557233);
        R[28] = max(EPSILON, w + c * 0.01520947 + m * 0.65738359 + y * 0.98388535 + r * 0.98470202 + g * 0.32176956 + b * 0.01565710);
        R[29] = max(EPSILON, w + c * 0.01475977 + m * 0.59909403 + y * 0.98376116 + r * 0.98515481 + g * 0.36157329 + b * 0.01571025);
        R[30] = max(EPSILON, w + c * 0.01454263 + m * 0.56817268 + y * 0.98368246 + r * 0.98537114 + g * 0.48361920 + b * 0.01571916);
        R[31] = max(EPSILON, w + c * 0.01444459 + m * 0.54031997 + y * 0.98365023 + r * 0.98546685 + g * 0.46488579 + b * 0.01572133);
        R[32] = max(EPSILON, w + c * 0.01439897 + m * 0.52110241 + y * 0.98361309 + r * 0.98550011 + g * 0.47440306 + b * 0.01572502);
        R[33] = max(EPSILON, w + c * 0.01437620 + m * 0.51041094 + y * 0.98357259 + r * 0.98551031 + g * 0.48576990 + b * 0.01571717);
        R[34] = max(EPSILON, w + c * 0.01436343 + m * 0.50526577 + y * 0.98353856 + r * 0.98550741 + g * 0.49267971 + b * 0.01571905);
        R[35] = max(EPSILON, w + c * 0.01435687 + m * 0.50255080 + y * 0.98351247 + r * 0.98551323 + g * 0.49625685 + b * 0.01571059);
        R[36] = max(EPSILON, w + c * 0.01435370 + m * 0.50126452 + y * 0.98350101 + r * 0.98551563 + g * 0.49807754 + b * 0.01569728);
        R[37] = max(EPSILON, w + c * 0.01435408 + m * 0.50083021 + y * 0.98350852 + r * 0.98551547 + g * 0.49889859 + b * 0.01570020);
        return R;
    }

    private static Rgb spectral_reflectance_to_xyz(double R[]) {
        var xyz = new Rgb(0, 0, 0);
        xyz.addTerm(0.00006469, 0.00000184, 0.00030502, R[0]);
        xyz.addTerm(0.00021941, 0.00000621, 0.00103681, R[1]);
        xyz.addTerm(0.00112057, 0.00003101, 0.00531314, R[2]);
        xyz.addTerm(0.00376661, 0.00010475, 0.01795439, R[3]);
        xyz.addTerm(0.01188055, 0.00035364, 0.05707758, R[4]);
        xyz.addTerm(0.02328644, 0.00095147, 0.11365162, R[5]);
        xyz.addTerm(0.03455942, 0.00228226, 0.17335873, R[6]);
        xyz.addTerm(0.03722379, 0.00420733, 0.19620658, R[7]);
        xyz.addTerm(0.03241838, 0.00668880, 0.18608237, R[8]);
        xyz.addTerm(0.02123321, 0.00988840, 0.13995048, R[9]);
        xyz.addTerm(0.01049099, 0.01524945, 0.08917453, R[10]);
        xyz.addTerm(0.00329584, 0.02141831, 0.04789621, R[11]);
        xyz.addTerm(0.00050704, 0.03342293, 0.02814563, R[12]);
        xyz.addTerm(0.00094867, 0.05131001, 0.01613766, R[13]);
        xyz.addTerm(0.00627372, 0.07040208, 0.00775910, R[14]);
        xyz.addTerm(0.01686462, 0.08783871, 0.00429615, R[15]);
        xyz.addTerm(0.02868965, 0.09424905, 0.00200551, R[16]);
        xyz.addTerm(0.04267481, 0.09795667, 0.00086147, R[17]);
        xyz.addTerm(0.05625475, 0.09415219, 0.00036904, R[18]);
        xyz.addTerm(0.06947040, 0.08678102, 0.00019143, R[19]);
        xyz.addTerm(0.08305315, 0.07885653, 0.00014956, R[20]);
        xyz.addTerm(0.08612610, 0.06352670, 0.00009231, R[21]);
        xyz.addTerm(0.09046614, 0.05374142, 0.00006813, R[22]);
        xyz.addTerm(0.08500387, 0.04264606, 0.00002883, R[23]);
        xyz.addTerm(0.07090667, 0.03161735, 0.00001577, R[24]);
        xyz.addTerm(0.05062889, 0.02088521, 0.00000394, R[25]);
        xyz.addTerm(0.03547396, 0.01386011, 0.00000158, R[26]);
        xyz.addTerm(0.02146821, 0.00810264, 0.00000000, R[27]);
        xyz.addTerm(0.01251646, 0.00463010, 0.00000000, R[28]);
        xyz.addTerm(0.00680458, 0.00249138, 0.00000000, R[29]);
        xyz.addTerm(0.00346457, 0.00125930, 0.00000000, R[30]);
        xyz.addTerm(0.00149761, 0.00054165, 0.00000000, R[31]);
        xyz.addTerm(0.00076970, 0.00027795, 0.00000000, R[32]);
        xyz.addTerm(0.00040737, 0.00014711, 0.00000000, R[33]);
        xyz.addTerm(0.00016901, 0.00006103, 0.00000000, R[34]);
        xyz.addTerm(0.00009522, 0.00003439, 0.00000000, R[35]);
        xyz.addTerm(0.00004903, 0.00001771, 0.00000000, R[36]);
        xyz.addTerm(0.00002000, 0.00000722, 0.00000000, R[37]);
        return xyz;
    }

    private static double spectral_linear_to_concentration(double l1, double l2, double t) {
        var t1 = l1 * pow(1.0 - t, 2.0);
        var t2 = l2 * pow(t, 2.0);
        return t2 / (t1 + t2);
    }

    private static Rgb spectral_mix(Rgb color1, Rgb color2, double t) {
        var R1 = spectral_linear_to_reflectance(spectral_srgb_to_linear(color1));
        var R2 = spectral_linear_to_reflectance(spectral_srgb_to_linear(color2));

        t = spectral_linear_to_concentration(spectral_reflectance_to_xyz(R1).g, spectral_reflectance_to_xyz(R2).g, t);

        var R = new double[SIZE];
        for (var i = 0; i < R.length; i++) {
            var KS = (1.0 - t) * (pow(1.0 - R1[i], 2.0) / (2.0 * R1[i])) + t * (pow(1.0 - R2[i], 2.0) / (2.0 * R2[i]));
            var KM = 1.0 + KS - sqrt(pow(KS, 2.0) + 2.0 * KS);

            //Saunderson correction
            // var S = ((1.0 - K1) * (1.0 - K2) * KM) / (1.0 - K2 * KM);
            R[i] = KM;
        }

        return spectral_xyz_to_srgb(spectral_reflectance_to_xyz(R));
    }

    private static class Vec4 {

        float r, g, b, a;

        Vec4(Rgb c, float a) {
            this.r = c.r;
            this.g = c.g;
            this.b = c.b;
            this.a = a;
        }

        Vec4(Color c) {
            this.r = (float) (c.getRed() / 255.0);
            this.g = (float) (c.getGreen() / 255.0);
            this.b = (float) (c.getBlue() / 255.0);
            this.a = (float) (c.getAlpha() / 255.0);
        }
    }
}
