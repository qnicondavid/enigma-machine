package com.enigma;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

class ReflectorTest {

    @Test
    void reflectionIsAnInvolution() {
        Reflector reflector = new Reflector(99);
        for (int c = 0; c < 256; c++) {
            assertEquals(c, reflector.reflect(reflector.reflect(c)));
        }
    }

    @Test
    void noValueReflectsToItself() {
        Reflector reflector = new Reflector(99);
        for (int c = 0; c < 256; c++) {
            assertNotEquals(c, reflector.reflect(c));
        }
    }
}
