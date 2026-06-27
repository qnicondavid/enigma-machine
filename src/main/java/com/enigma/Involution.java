package com.enigma;

import java.util.Random;

public final class Involution {

    private final int[] map = new int[256];

    public Involution(int seed) {
        int[] pool = new int[256];
        for (int i = 0; i < 256; i++) {
            pool[i] = i;
        }
        Random rand = new Random(seed);
        for (int i = 255; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = pool[i];
            pool[i] = pool[j];
            pool[j] = temp;
        }
        for (int i = 0; i < 256; i += 2) {
            int a = pool[i];
            int b = pool[i + 1];
            map[a] = b;
            map[b] = a;
        }
    }

    public int apply(int c) {
        return map[c];
    }
}
