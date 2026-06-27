package com.enigma;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GoldenVectorTest {

    @Test
    void encryptMatchesGoldenVector() {
        EnigmaMachine machine = EnigmaMachine.fromPassword("golden-key", 3);
        String message = "The quick brown fox jumps over the lazy dog.";
        String expected = "4QnaWv3yNKf7W/V2UmTddj9hUo+BQNGKVai48oPEL9Mj90LQtjaGFTgJv50=";
        assertEquals(expected, machine.encrypt(message));
    }
}
