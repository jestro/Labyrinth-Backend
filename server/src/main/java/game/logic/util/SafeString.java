package game.logic.util;

import java.util.Objects;

public class SafeString {
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ'0123456789";
    private final String string;

    public SafeString(String string) throws IllegalStateException {

        for (char character : string.toCharArray()) {

            if (!isCharAllowed(character)) {
                throw new IllegalArgumentException("String contains illegal characters");
            }
        }
        this.string = string;
    }

    public static boolean isCharAllowed(char character) {
        for (char allowedCharacter : ALLOWED_CHARACTERS.toCharArray()) {
            if (character == allowedCharacter) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SafeString that = (SafeString) o;
        return Objects.equals(string, that.string);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string);
    }
}
