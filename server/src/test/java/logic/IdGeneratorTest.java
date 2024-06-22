package logic;

import game.logic.util.IdGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {
    @Test
    void testCreateMD5(){
        String id = IdGenerator.getDummyMD5();

        assertEquals(32, id.length());
    }
    @Test
    void testCreateUniqueMD5(){
        String id1 = IdGenerator.getDummyMD5();
        String id2 = IdGenerator.getDummyMD5();

        assertNotEquals(id1, id2);
    }
}
