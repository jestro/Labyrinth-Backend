package game.logic.util;

import static game.logic.LabyrinthService.random;

public final class IdGenerator {
    private static final char[] hex = "0123456789abcdef".toCharArray();

    private static final int ID_LENGTH = 32;

    private IdGenerator() {
        throw new IllegalStateException("Utility class");
    }

    private static char getRandomHex() {
        return hex[random.nextInt(hex.length)];
    }

    public static String getDummyMD5() {
        StringBuilder md5 = new StringBuilder();
        for (int i = 0; i < ID_LENGTH; i++) {
            md5.append(getRandomHex());
        }
        return md5.toString();
    }
}
