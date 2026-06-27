package com.enigma;

import java.util.Random;

public final class Rotor {

    private final int[] forwardMap = new int[256];
    private final int[] reverseMap = new int[256];
    private final int turnoverPoint;
    private final int initialPosition;
    private int position;

    public Rotor(int seed, int initialPosition) {
        Random rand = new Random(seed);
        this.turnoverPoint = Math.floorMod(seed, 256);
        this.initialPosition = Math.floorMod(initialPosition, 256);
        this.position = this.initialPosition;

        int[] wiring = new int[256];
        for (int i = 0; i < 256; i++) {
            wiring[i] = i;
        }
        for (int i = 255; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = wiring[i];
            wiring[i] = wiring[j];
            wiring[j] = temp;
        }
        for (int i = 0; i < 256; i++) {
            forwardMap[i] = wiring[i];
            reverseMap[wiring[i]] = i;
        }
    }

    public void reset() {
        position = initialPosition;
    }

    public int encrypt(int c) {
        return forwardMap[(c + position) & 0xFF];
    }

    public int decrypt(int c) {
        return (reverseMap[c] - position) & 0xFF;
    }

    public void rotate() {
        position = (position + 1) & 0xFF;
    }

    public boolean atTurnover() {
        return position == turnoverPoint;
    }

    public int position() {
        return position;
    }

    public int turnoverPoint() {
        return turnoverPoint;
    }
}
