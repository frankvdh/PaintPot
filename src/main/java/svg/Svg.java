/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package svg;

import com.kitfox.svg.Path;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import java.awt.Color;
import static java.awt.geom.PathIterator.SEG_CLOSE;
import static java.awt.geom.PathIterator.SEG_CUBICTO;
import static java.awt.geom.PathIterator.SEG_LINETO;
import static java.awt.geom.PathIterator.SEG_QUADTO;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author frank
 */
public class Main {

    public static void main(String[] args) throws IOException, SVGException {
        var universe = new SVGUniverse();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        var url = classLoader.getResource("bailey.svg");
        var base = universe.loadSVG(url);
        var diagram = universe.getDiagram(base);
        var root = diagram.getRoot();
        var children = root.getChildren(null);
        var colours = new HashMap<Color, Double>();
        var totalArea = 0.0;
        for (var c : children) {
            if (c instanceof Path p) {
                String[] fillString = null;
                var style = p.getPresAbsolute("style");
                for (var s : style.getStringValue().split(";")) {
                    if (s.matches("\\s*fill\\:\\s*.*")) {
                        fillString = s.substring(s.indexOf('(') + 1, s.indexOf(')') - 1).trim().split(",");
                    }
                }
                var fillColour = new Color(Integer.parseInt(fillString[0].trim()), Integer.parseInt(fillString[1].trim()), Integer.parseInt(fillString[2].trim()));

                var id = p.getPresAbsolute("data-facetId").getStringValue();

                var shape = p.getShape();
                var area = 0.0;

                var pi = shape.getPathIterator(null);
                double[] curr = new double[6];
                pi.currentSegment(curr);
                double[] first = new double[]{curr[0], curr[1]};
                var prev = new double[]{first[0], first[1]};
                pi.next();
                while (!pi.isDone()) {
                    double thisArea;
                    var type = pi.currentSegment(curr);
                    switch (type) {
                        case SEG_LINETO -> {
                            thisArea = (prev[1] + curr[1]) * (curr[0] - prev[0]) / 2;
                            prev[0] = curr[0];
                            prev[1] = curr[1];
                        }
                        case SEG_QUADTO -> {
                            thisArea = (prev[1] + curr[3]) * (curr[2] - prev[0]) / 2;
                            prev[0] = curr[2];
                            prev[1] = curr[3];
                        }
                        case SEG_CUBICTO -> {
                            thisArea = (prev[1] + curr[5]) * (curr[4] - prev[0]) / 2;
                            prev[0] = curr[4];
                            prev[1] = curr[5];
                        }
                        case SEG_CLOSE -> {
                            thisArea = (prev[1] + first[1]) * (first[0] - prev[0]) / 2;
                        }
                        default ->
                            thisArea = 0;
                    }
                    area += thisArea;
//                    System.out.println("" + type + ": " + Arrays.toString(curr) + ", " + thisArea);
                    pi.next();
                }
                var thisArea = (prev[1] + first[1]) * (first[0] - prev[0]) / 2;
                area = Math.abs(area + thisArea);
                var box = p.getBoundingBox();
                var boxArea = box.getWidth() * box.getHeight();
//                System.out.println(id + ": " + fillColour + ", " + boxArea + ", " + area);
                if (colours.containsKey(fillColour)) {
                    colours.replace(fillColour, colours.get(fillColour) + boxArea);
                } else {
                    colours.put(fillColour, boxArea);
                }
                totalArea += area;
            }
        }
        // 775 * 1024 = 793600 pixels
        System.out.println("Total Area = " + totalArea + ", " + root.getDeviceWidth() + " by " + root.getDeviceHeight() + " = " + root.getDeviceWidth() * root.getDeviceHeight());
        for (var e : colours.entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue() / totalArea);
        }
    }

    // Java program to evaluate area 
// of a polygon using shoelace formula
//    import java.io.
    // (X[i], Y[i]) are coordinates of i'th point.
    public static double polygonArea(double X[], double Y[], int n) {
        // Initialize area
        double area = 0.0;

        // Calculate value of shoelace formula
        int j = n - 1;
        for (int i = 0; i < n; i++) {
            area += (X[j] + X[i]) * (Y[j] - Y[i]);

            // j is previous vertex to i
            j = i;
        }

        // Return absolute value
        return Math.abs(area / 2.0);
    }
}
