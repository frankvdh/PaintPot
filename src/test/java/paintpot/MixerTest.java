/*
 * This file © 2024 by Frank van der Hulst is licensed under CC BY-NC-SA 4.0. To view a copy of this license, visit https://creativecommons.org/licenses/by-nc-sa/4.0/
 */
package paintpot;

import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author frank
 */
public class MixerTest {

    public MixerTest() {
    }

    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }

    @org.junit.jupiter.api.AfterAll
    public static void tearDownClass() throws Exception {
    }

    @org.junit.jupiter.api.BeforeEach
    public void setUp() throws Exception {
    }

    @org.junit.jupiter.api.AfterEach
    public void tearDown() throws Exception {
    }

    /**
     * Test of palette method, of class Spectral.
     */
    @org.junit.jupiter.api.Test
    public void testPalette() {
        System.out.println("palette");
        Color color1 = Color.YELLOW;
        Color color2 = Color.BLUE;
        int size = 10;
        Mixer instance = new Mixer();
        Color[] expResult = new Color[]{Color.YELLOW, new Color(219, 235, 43), new Color(177, 210, 62), new Color(132, 184, 73), new Color(83, 156, 81), new Color(18, 130, 86), new Color(0, 106, 91), new Color(0, 87, 102), new Color(0, 73, 135), Color.BLUE};
        Color[] result = instance.palette(color1, color2, size);
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of mix method, of class Spectral.
     */
    @org.junit.jupiter.api.Test
    public void testMix() {
        System.out.println("mix");
        Color c1 = Color.BLUE;
        Color c2 = Color.YELLOW;
        double t = 0.5;
        Mixer instance = new Mixer();
        Colour expResult = new Colour("#388f54", 56, 143, 84);
        Colour result = instance.mix(c1, c2, t);
        assertEquals(expResult, result);
    }
}
