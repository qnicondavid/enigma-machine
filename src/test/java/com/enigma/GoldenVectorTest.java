package com.enigma;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GoldenVectorTest {

    @Test
    void encryptMatchesGoldenVector() {
        EnigmaMachine machine = EnigmaMachine.fromPassword("golden-key", 3);
        String message = "The quick brown fox jumps over the lazy dog.";
        String expected = "fRhGTTQO00LkLyhdd2BqlAAN6i1byE5cm3neehu9s1kIOBwalaL6aWtXUko=";
        assertEquals(expected, machine.encrypt(message));
    }
}
