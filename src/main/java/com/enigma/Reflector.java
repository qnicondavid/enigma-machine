package com.enigma;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Reflector {

    private final int[] map = new int[256];

    public Reflector(int seed) {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            values.add(i);
        }
        Random rand = new Random(seed);
        while (values.size() > 1) {
            int a = values.remove(rand.nextInt(values.size()));
            int b = values.remove(rand.nextInt(values.size()));
            map[a] = b;
            map[b] = a;
        }
    }

    public int reflect(int c) {
        return map[c];
    }
}
