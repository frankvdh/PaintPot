/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package paintpot;

import java.awt.Color;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author frank
 */
public class PaletteTest {

    public PaletteTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of add method, of class Palette.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        double t = 0.5;
        Paint instance = new Paint(6);
        instance.add(0, t);
        assertEquals(0xfffd4d4d, instance.getMixedColour().getRGB());
        instance.add(0, t);
        assertEquals(0xffe6201b, instance.getMixedColour().getRGB());
        instance.add(0, t);
        assertEquals(0xffde130d, instance.getMixedColour().getRGB());
        instance.add(0, t);
        assertEquals(0xffdc0f09, instance.getMixedColour().getRGB());
        instance.add(0, t);
        assertEquals(0xffdb0e08, instance.getMixedColour().getRGB());
    }

    /**
     * Test of add method, of class Palette.
     */
    @Test
    public void testAddYB() {
        System.out.println("addYB");
        double t = 0.5;
        Paint instance = new Paint(6);
        instance.add(2, t);
        assertEquals(0xfff1e448, instance.getMixedColour().getRGB());
    }

    /**
     * Test of add method, of class Palette.
     */
    @Test
    public void testAddYBG() {
        System.out.println("addYB");
        double t = 0.66;
        Paint instance = new Paint(6);
        instance.add(2, t);
        assertEquals(0xffeeda2b, instance.getMixedColour().getRGB());
    }

    /**
     * Test of mix method, of class Palette.
     */
    @Test
    public void testMix() {
        System.out.println("mix");
        Paint instance = new Paint(4);
        Color expResult = Paint.primary[4];
        Color result = instance.mix();
        assertEquals(expResult, result);
    }
}
