package com.enigma;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Plugboard {

    private final int[] map = new int[256];

    public Plugboard(int seed) {
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            map[i] = i;
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

    public int swap(int c) {
        return map[c];
    }
}
