package logic;

import game.logic.TreasureManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TreasureManagerTest {
    @Test
    void testCustomTreasureList() {
        String[] chosen = new String[]{"Bat", "Owl", "Rat"};
        TreasureManager treasureManager = new TreasureManager(chosen);

        assertEquals(chosen, treasureManager.getChosenTreasures());
        assertEquals(3, treasureManager.getCount());
    }

    @Test
    void testGetCustomTreasure() {
        String[] chosen = new String[]{"Bat", "Owl", "Rat"};
        TreasureManager treasureManager = new TreasureManager(chosen);

        assertNotNull(treasureManager.getRandom());
    }

    @Test
    void testEmptyTreasureList() {
        String[] chosen = new String[]{};
        TreasureManager treasureManager = new TreasureManager(chosen);

        assertEquals(TreasureManager.treasureChoices, treasureManager.getChosenTreasures());
        assertEquals(TreasureManager.treasureChoices.length, treasureManager.getCount());
    }

    @Test
    void testNullTreasureList() {
        TreasureManager treasureManager = new TreasureManager(null);

        assertEquals(TreasureManager.treasureChoices, treasureManager.getChosenTreasures());
        assertEquals(TreasureManager.treasureChoices.length, treasureManager.getCount());
    }
}
