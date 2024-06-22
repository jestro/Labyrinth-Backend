package logic;

import game.logic.Game;
import game.logic.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MazeTest {
    @Test
    void testGetPossiblePaths() {
        Game game = TestUtils.createPlayingGame("GetPossiblePaths");
        Position player1Position = new Position(0, 0);

        assertFalse(game.getMaze().getPossiblePaths(player1Position).isEmpty());
    }

    @Test
    void testTryGetPossiblePaths() {
        Game game = TestUtils.createPlayingGame("TryGetPossiblePaths");
        Position impossiblePosition = new Position(99, 99);

        assertThrowsExactly(IllegalArgumentException.class, () -> game.getMaze().getPossiblePaths(impossiblePosition));
    }

    @Test
    void testGetBoard() {
        Game game = TestUtils.createPlayingGame("GetBoard");

        assertNotNull(game.getMaze().getBoard());
    }
}
