import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enigma {
    static EnigmaMachine machine;

    public static void main(String args[]) {
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
        a = machine.encrypt(a);
        System.out.println("Encrypted:\n" + a);
        a = machine.encrypt(a);
        System.out.println("Decrypted:\n" + a);
    }

    public static void initialise() {
        String password = "G#9vT!x2@Lr7$eZpQ8^mW3&bK1*DcYf";
        int seed = password.hashCode();
        machine = new EnigmaMachine(seed, 10);
    }

    public static class EnigmaMachine {
        private List<Rotor> rotors;
        private Reflector reflector;
        private Plugboard plugboard;

        public EnigmaMachine(int seed, int rotorCount) {
            rotors = new ArrayList<>();
            Random rand = new Random(seed);
            for (int i = 0; i < rotorCount; i++) {
                Rotor rotor = new Rotor(seed + i);
                int initialPos = rand.nextInt(256);
                rotor.setInitialPosition(initialPos);
                rotors.add(rotor);
            }
            reflector = new Reflector(seed);
            plugboard = new Plugboard(~seed);
        }

        public String encrypt(String a) {
            for (Rotor rotor : rotors) {
                rotor.reset();
            }
            StringBuilder answer = new StringBuilder();
            for (int i = 0; i < a.length(); i++) {
                char c = a.charAt(i);
                c = plugboard.swap(c);
                for (Rotor rotor : rotors) {
                    c = rotor.encrypt(c);
                }
                c = reflector.reflect(c);
                for (int j = rotors.size() - 1; j >= 0; j--) {
                    c = rotors.get(j).decrypt(c);
                }
                c = plugboard.swap(c);
                answer.append(c);
                for (int r = 0; r < rotors.size(); r++) {
                    rotors.get(r).rotate();
                    if (!rotors.get(r).atTurnover())
                        break;
                }
            }
            return answer.toString();
        }

        public static class Rotor {
            private char[] forwardMap = new char[256];
            private char[] reverseMap = new char[256];
            private char[] initialMap = new char[256];
            private int position = 0;
            private int initialPosition = 0;
            private int turnoverPoint;

            public Rotor(int seed) {
                Random rand = new Random(seed);
                turnoverPoint = seed % 256;
                char[] chars = new char[256];
                for (int i = 0; i < 256; i++) {
                    chars[i] = (char) i;
                }
                for (int i = 255; i > 0; i--) {
                    int j = rand.nextInt(i + 1);
                    char temp = chars[i];
                    chars[i] = chars[j];
                    chars[j] = temp;
                }
                for (int i = 0; i < 256; i++) {
                    forwardMap[i] = chars[i];
                    reverseMap[forwardMap[i]] = (char) i;
                    initialMap[i] = chars[i];
                }
            }

            public void setInitialPosition(int pos) {
                initialPosition = pos % 256;
                position = initialPosition;
            }

            public void reset() {
                position = initialPosition;
                for (int i = 0; i < 256; i++) {
                    forwardMap[i] = initialMap[i];
                    reverseMap[forwardMap[i]] = (char) i;
                }
            }

            public char encrypt(char c) {
                int shifted = (c + position) % 256;
                return forwardMap[shifted];
            }

            public char decrypt(char c) {
                int index = reverseMap[c];
                return (char) ((index - position + 256) % 256);
            }

            public void rotate() {
                position = (position + 1) % 256;
            }

            public boolean atTurnover() {
                return position == turnoverPoint;
            }
        }

        static class Reflector {
            private char[] map = new char[256];

            public Reflector(int seed) {
                List<Character> chars = new ArrayList<>();
                for (int i = 0; i < 256; i++) {
                    chars.add((char) i);
                }
                Random rand = new Random(seed);
                for (int i = 255; i > 0; i -= 2) {
                    int j = rand.nextInt(i + 1);
                    int k = rand.nextInt(i);
                    if (j == k)
                        k = (k + 1) % i;
                    char a = chars.remove(j);
                    char b = chars.remove(k);
                    map[a] = b;
                    map[b] = a;
                }
            }

            public char reflect(char c) {
                return map[c];
            }
        }

        static class Plugboard {
            private char[] map = new char[256];

            public Plugboard(int seed) {
                for (int i = 0; i < 256; i++) {
                    map[i] = (char) i;
                }
                List<Character> chars = new ArrayList<>();
                for (int i = 0; i < 256; i++) {
                    chars.add((char) i);
                }
                Random rand = new Random(seed);
                for (int i = 255; i > 0; i -= 2) {
                    int j = rand.nextInt(i + 1);
                    int k = rand.nextInt(i);
                    if (j == k)
                        k = (k + 1) % i;
                    char a = chars.remove(j);
                    char b = chars.remove(k);
                    map[a] = b;
                    map[b] = a;
                }
            }

            public char swap(char c) {
                return map[c];
            }
        }
    }
}