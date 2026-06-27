import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

public class Enigma {
    static EnigmaMachine machine;

    public static void main(String[] args) {
        initialise();
        String a = "In the heart of the machine, patterns emerge. Bits flicker, bytes align, and logic gates whisper secrets long forgotten. "
                +
                "The engineer watches, not with eyes alone, but with intuition honed by years of debugging and design. "
                +
                "Each cycle brings revelation: a loop unrolled, a cache warmed, a branch predicted. " +
                "He smiles at the elegance of entropy tamed, where chaos yields to clarity through code. " +
                "From silicon dreams to magnetic memories, the story unfolds - not in words, but in voltages and timing. "
                +
                "This is the poetry of computation: precise, relentless, beautiful. " +
                "And as the final instruction retires, he knows the system has spoken.";
        System.out.println("Original:\n" + a);
        String enc = machine.encrypt(a);
        System.out.println("Encrypted (Base64):\n" + enc);
        String dec = machine.decrypt(enc);
        System.out.println("Decrypted:\n" + dec);

        selfTest();
    }

    public static void initialise() {
        String password = "G#9vT!x2@Lr7$eZpQ8^mW3&bK1*DcYf";
        int seed = password.hashCode();
        machine = new EnigmaMachine(seed, 3);
    }

    public static void selfTest() {
        System.out.println("\n=== self-test ===");

        EnigmaMachine m1 = new EnigmaMachine("seed-A".hashCode(), 3);
        String msg = "Reciprocity check 123.";
        check("reciprocity (round-trip)", m1.decrypt(m1.encrypt(msg)).equals(msg));

        EnigmaMachine m2 = new EnigmaMachine("negativeSeedTest".hashCode(), 3);
        check("negative-seed round-trip", m2.decrypt(m2.encrypt(msg)).equals(msg));

        String unicode = "cafe € 😀 你好";
        EnigmaMachine m3 = new EnigmaMachine("unicode".hashCode(), 3);
        check("unicode round-trip", m3.decrypt(m3.encrypt(unicode)).equals(unicode));

        check("empty string", m1.decrypt(m1.encrypt("")).equals(""));
    }

    private static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
        if (!ok) throw new AssertionError(name);
    }

    public static class EnigmaMachine {
        private final List<Rotor> rotors;
        private final Reflector reflector;
        private final Plugboard plugboard;

        public EnigmaMachine(int seed, int rotorCount) {
            rotors = new ArrayList<>();
            Random rand = new Random(seed);
            for (int i = 0; i < rotorCount; i++) {
                Rotor rotor = new Rotor(seed + i);
                rotor.setInitialPosition(rand.nextInt(256));
                rotors.add(rotor);
            }
            reflector = new Reflector(seed);
            plugboard = new Plugboard(~seed);
        }

        public String encrypt(String text) {
            byte[] out = transform(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(out);
        }

        public String decrypt(String base64) {
            byte[] out = transform(Base64.getDecoder().decode(base64));
            return new String(out, StandardCharsets.UTF_8);
        }

        public byte[] transform(byte[] input) {
            for (Rotor rotor : rotors) {
                rotor.reset();
            }
            byte[] output = new byte[input.length];
            for (int i = 0; i < input.length; i++) {
                step();

                int c = input[i] & 0xFF;
                c = plugboard.swap(c);
                for (Rotor rotor : rotors) {
                    c = rotor.encrypt(c);
                }
                c = reflector.reflect(c);
                for (int j = rotors.size() - 1; j >= 0; j--) {
                    c = rotors.get(j).decrypt(c);
                }
                c = plugboard.swap(c);
                output[i] = (byte) c;
            }
            return output;
        }

        private void step() {
            for (Rotor rotor : rotors) {
                rotor.rotate();
                if (!rotor.atTurnover()) {
                    break;
                }
            }
        }

        public static class Rotor {
            private final int[] forwardMap = new int[256];
            private final int[] reverseMap = new int[256];
            private final int turnoverPoint;
            private int position = 0;
            private int initialPosition = 0;

            public Rotor(int seed) {
                Random rand = new Random(seed);
                turnoverPoint = Math.floorMod(seed, 256);

                int[] chars = new int[256];
                for (int i = 0; i < 256; i++) {
                    chars[i] = i;
                }
                for (int i = 255; i > 0; i--) {
                    int j = rand.nextInt(i + 1);
                    int temp = chars[i];
                    chars[i] = chars[j];
                    chars[j] = temp;
                }
                for (int i = 0; i < 256; i++) {
                    forwardMap[i] = chars[i];
                    reverseMap[chars[i]] = i;
                }
            }

            public void setInitialPosition(int pos) {
                initialPosition = Math.floorMod(pos, 256);
                position = initialPosition;
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
        }

        static class Reflector {
            private final int[] map = new int[256];

            public Reflector(int seed) {
                List<Integer> chars = new ArrayList<>();
                for (int i = 0; i < 256; i++) {
                    chars.add(i);
                }
                Random rand = new Random(seed);
                while (chars.size() > 1) {
                    int a = chars.remove(rand.nextInt(chars.size()));
                    int b = chars.remove(rand.nextInt(chars.size()));
                    map[a] = b;
                    map[b] = a;
                }
            }

            public int reflect(int c) {
                return map[c];
            }
        }

        static class Plugboard {
            private final int[] map = new int[256];

            public Plugboard(int seed) {
                List<Integer> chars = new ArrayList<>();
                for (int i = 0; i < 256; i++) {
                    map[i] = i;
                    chars.add(i);
                }
                Random rand = new Random(seed);
                while (chars.size() > 1) {
                    int a = chars.remove(rand.nextInt(chars.size()));
                    int b = chars.remove(rand.nextInt(chars.size()));
                    map[a] = b;
                    map[b] = a;
                }
            }

            public int swap(int c) {
                return map[c];
            }
        }
    }
}
