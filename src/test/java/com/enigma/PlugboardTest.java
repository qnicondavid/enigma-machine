package com.enigma;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PlugboardTest {

    @Test
    void swapIsAnInvolution() {
        Plugboard plugboard = new Plugboard(~12345);
        for (int c = 0; c < 256; c++) {
            assertEquals(c, plugboard.swap(plugboard.swap(c)));
        }
    }

    @Test
    void everyValueMaps() {
        Plugboard plugboard = new Plugboard(~12345);
        boolean[] seen = new boolean[256];
        for (int c = 0; c < 256; c++) {
            seen[plugboard.swap(c)] = true;
        }
        for (int i = 0; i < 256; i++) {
            assertEquals(true, seen[i], "value " + i + " is not in the image");
        }
    }
}
