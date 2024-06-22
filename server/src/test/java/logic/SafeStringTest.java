package logic;

import game.logic.util.SafeString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SafeStringTest {
    @Test
    void testSafeString(){
        SafeString str = new SafeString("hallo");

        assertEquals("hallo", str.toString());
    }

    @Test
    void testException(){
        assertThrows(IllegalArgumentException.class, () -> new SafeString("(╯°□°)╯︵"));
    }
}
