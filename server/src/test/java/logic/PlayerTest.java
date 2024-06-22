package logic;

import game.logic.util.SafeString;
import game.logic.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void testPlayerCreation() {
        Player player = new Player(new SafeString("test"));

        assertNull(player.getLocation());
        assertEquals("test", player.getName());
        assertNull(player.getPlayerState());
    }

    @Test
    void testInvalidPlayer(){
        assertThrowsExactly(IllegalArgumentException.class, () -> new Player(new SafeString("+-*")));
    }

    @Test
    void testNewObjective() {
        Game game = TestUtils.createPlayingGame("NewObjective");

        for (int i = 0; i < Math.pow(TreasureManager.treasureChoices.length, 2); i++) {
            String previousObjective = TestUtils.player1.getObjective();
            game.setRandomObjective(TestUtils.player1);

            assertNotEquals(TestUtils.player1.getObjective(), previousObjective);
        }
    }

    @Test
    void testPlayerStateValue() {
        TestUtils.createPlayingGame("PlayerState");

        assertEquals(TestUtils.player1.getPlayerState().getValue(), PlayerState.PLAYING.getValue());
    }

    @Test
    void testPlayerWaiting(){
        Game game = new Game("group13", new SafeString("PlayerWaiting"), GameMode.SIMPLE, 4, 1);

        Player player1 = new Player(new SafeString("wim"));

        game.addPlayer(player1);

        assertEquals(PlayerState.WAITING, player1.getPlayerState());
    }

    @Test
    void testPlayerWinAndLoss(){
        Game game = TestUtils.createPlayingGame("PlayerWinAndLoss");

        Position targetShove = new Position(0,3);
        Position targetMove = new Position(0,0);

        game.collectTreasure(TestUtils.player1.getObjective(), TestUtils.player1);

        game.shove(targetShove, game.getSpareTile());
        game.move(targetMove);

        assertEquals(PlayerState.LOST, TestUtils.player2.getPlayerState());
        assertEquals(PlayerState.WON, TestUtils.player1.getPlayerState());
    }

    @Test
    void testPlayerLeft(){
        Game game = TestUtils.createPlayingGame("PlayerLeft");

        game.removePlayer(TestUtils.player1);

        assertEquals(PlayerState.LEFT, TestUtils.player1.getPlayerState());
    }
}
