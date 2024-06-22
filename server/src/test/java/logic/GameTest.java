package logic;

import game.logic.util.SafeString;
import game.logic.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void testCreateGame() {
        Game game = new Game("group13", new SafeString("createGame"), GameMode.SIMPLE, 2, 1);

        assertTrue(Game.getGames().contains(game));
        assertNull(game.getCurrentShoveName());
        assertNull(game.getCurrentMoveName());
        assertEquals(game.getGameId(), String.format("%s_%s", game.getPrefix(), game.getName()));
        assertEquals(2, game.getMaxPlayers());
        assertEquals(2, game.getMinPlayers());
        assertEquals(1, game.getTreasuresRequired());
        assertEquals(GameMode.SIMPLE, game.getGameMode());
        assertEquals(GameMode.SIMPLE.getValue(), game.getGameMode().getValue());
    }

    @Test
    void testCreatePlayingGame() {
        Game game = TestUtils.createPlayingGame("createPlayingGame");

        assertNotNull(game.getMaze());
    }

    @Test
    void testCreateGameWith8Players() {
        Game game = new Game("group13", new SafeString("testCreateGameWith8Players"), GameMode.SIMPLE, 8, 1);

        game.addPlayer(TestUtils.player1);
        game.addPlayer(TestUtils.player2);
        game.addPlayer(TestUtils.player3);
        game.addPlayer(TestUtils.player4);
        game.addPlayer(TestUtils.player5);
        game.addPlayer(TestUtils.player6);
        game.addPlayer(TestUtils.player7);
        game.addPlayer(TestUtils.player8);

        assertEquals(new Position(0, 0), TestUtils.player1.getLocation());
        assertEquals(new Position(0, 6), TestUtils.player2.getLocation());
        assertEquals(new Position(6, 0), TestUtils.player3.getLocation());
        assertEquals(new Position(6, 6), TestUtils.player4.getLocation());
        assertEquals(new Position(0, 0), TestUtils.player5.getLocation());
        assertEquals(new Position(0, 6), TestUtils.player6.getLocation());
        assertEquals(new Position(6, 0), TestUtils.player7.getLocation());
        assertEquals(new Position(6, 6), TestUtils.player8.getLocation());

        assertEquals(PlayerState.PLAYING, TestUtils.player1.getPlayerState());
        assertEquals(PlayerState.PLAYING, TestUtils.player2.getPlayerState());
        assertEquals(PlayerState.PLAYING, TestUtils.player3.getPlayerState());
        assertEquals(PlayerState.PLAYING, TestUtils.player4.getPlayerState());
        assertEquals(PlayerState.PLAYING, TestUtils.player5.getPlayerState());
        assertEquals(PlayerState.PLAYING, TestUtils.player6.getPlayerState());
        assertEquals(PlayerState.PLAYING, TestUtils.player7.getPlayerState());
        assertEquals(PlayerState.PLAYING, TestUtils.player8.getPlayerState());

        assertEquals(8, game.getPlayers().size());
    }

    @Test
    void testCanCreateGames() {
        new Game("group13", new SafeString("duplicateGame"), GameMode.SIMPLE, 2, 1);

        assertThrowsExactly(IllegalStateException.class, () -> new Game("group13", new SafeString("duplicateGame"), GameMode.SIMPLE, 2, 1));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Game("group13", new SafeString("-+°"), GameMode.SIMPLE, 2, 1));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Game("group13", new SafeString("negativeMaxPlayers"), GameMode.SIMPLE, -5, 1));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Game("group13", new SafeString("negativeTreasures"), GameMode.SIMPLE, 2, -5));
        assertThrowsExactly(IllegalArgumentException.class, () -> new Game("group13", new SafeString("invalidMaxPlayers"), GameMode.SIMPLE, 30, 1));
        assertThrowsExactly(IllegalStateException.class, () -> new Game("group13", new SafeString("tooSmallForTreasures"), GameMode.SIMPLE, 2, 1, 2, 2));
    }

    @Test
    void testTurnSwitching() {
        Game game = TestUtils.createPlayingGame("turnSwitching");
        Position targetShove = new Position(6, 1);
        Position movePlayer1 = new Position(0, 0);
        Position movePlayer2 = new Position(0, 6);

        game.shove(targetShove, game.getSpareTile());
        game.move(movePlayer1);
        game.shove(targetShove, game.getSpareTile());
        game.move(movePlayer2);

        assertEquals(game.getCurrentShovePlayer(), TestUtils.player1);
    }

    @Test
    void testJoinGame() {
        Game game = TestUtils.createPlayingGame("GetPlayer");
        List<String> playerNames = new ArrayList<>();
        playerNames.add(TestUtils.player1.getName());
        playerNames.add(TestUtils.player2.getName());

        assertEquals(game.getPlayer("waut"), TestUtils.player1);
        assertEquals(game.getPlayerNames(), playerNames);
        assertTrue(game.getPlayers().contains(TestUtils.player1));
    }

    @Test
    void testCanJoinGame() {
        Game game = new Game("group13", new SafeString("CanJoinGame"), GameMode.SIMPLE, 2, 1);

        game.addPlayer(TestUtils.player1);

        assertThrowsExactly(IllegalStateException.class, () -> game.addPlayer(TestUtils.player1));
        assertThrowsExactly(IllegalStateException.class, () -> {
            game.addPlayer(TestUtils.player2);
            game.addPlayer(TestUtils.player3);
        });
    }

    @Test
    void testLeaveGame() {
        Game game = new Game("group13", new SafeString("LeaveGame"), GameMode.SIMPLE, 2, 1);

        game.addPlayer(TestUtils.player1);
        game.removePlayer(TestUtils.player1);

        assertFalse(game.getPlayers().contains(TestUtils.player1));
    }

    @Test
    void testAutoGameDeletion() {
        Game game = new Game("group13", new SafeString("LeaveGame"), GameMode.SIMPLE, 2, 1);

        game.addPlayer(TestUtils.player1);
        game.removePlayer(TestUtils.player1);

        assertFalse(Game.getGames().contains(game));
    }

    @Test
    void testMovePlayer() {
        Game game = TestUtils.createPlayingGame("MovePlayer");

        Position targetShove = new Position(6, 1);
        Position targetMove = new Position(0, 0);

        game.shove(targetShove, game.getSpareTile());
        game.move(targetMove);

        assertEquals(TestUtils.player1.getLocation(), targetMove);
        assertEquals(TestUtils.player2, game.getCurrentShovePlayer());
        assertEquals(TestUtils.player2.getName(), game.getCurrentShoveName());
    }

    @Test
    void testCanMove() {
        Game game = TestUtils.createPlayingGame("ImpossibleMove");

        Position targetShove = new Position(1, 6);
        Position impossibleMovePosition1 = new Position(99, 0);
        Position impossibleMovePosition2 = new Position(0, 99);

        game.shove(targetShove, game.getSpareTile());

        assertThrows(IllegalStateException.class, () -> game.move(impossibleMovePosition1));
        assertThrows(IllegalStateException.class, () -> game.move(impossibleMovePosition2));
    }

    @Test
    void testShoveTile() {
        Game game = TestUtils.createPlayingGame("ShoveTile");
        Position shovePosition = new Position(1, 0);
        Tile spareTile = game.getSpareTile();

        game.shove(shovePosition, spareTile);

        assertEquals(game.getMaze().getTile(shovePosition), spareTile);
        assertEquals(TestUtils.player1, game.getCurrentMovePlayer());
        assertEquals(TestUtils.player1.getName(), game.getCurrentMoveName());

    }

    @Test
    void testCanShoveTile() {
        Game game = TestUtils.createPlayingGame("CanShoveTile");
        Position player1Position = new Position(0, 0);
        Position shovePosition = new Position(0, 1);
        Position shovePosition2 = new Position(6, 1);
        Position impossibleShovePosition = new Position(99, 0);
        Tile fakeTile = new Tile(new boolean[]{true, true, true, true});

        game.shove(shovePosition, game.getSpareTile());
        game.move(player1Position);

        assertThrowsExactly(IllegalArgumentException.class, () -> game.shove(shovePosition2, game.getSpareTile()));
        assertThrowsExactly(IllegalArgumentException.class, () -> game.shove(shovePosition, fakeTile));
        assertThrowsExactly(IllegalArgumentException.class, () -> game.shove(impossibleShovePosition, game.getSpareTile()));
    }

    @Test
    void testCanShoveInHardcoreMode() {
        Game game = TestUtils.createPlayingGame("HardcoreGame", GameMode.HARDCORE);
        Position shovePosition = new Position(0, 1);
        Tile rotatedSpareTile = new Tile(game.getSpareTile());
        rotatedSpareTile.rotate(true);


        assertThrowsExactly(IllegalArgumentException.class, ()-> {
            game.shove(shovePosition, rotatedSpareTile);
        });
    }
    @Test
    void testShoveInHardcoreMode() {
        Game game = TestUtils.createPlayingGame("HardcoreGame2", GameMode.HARDCORE);
        Position shovePosition = new Position(0, 1);


        assertDoesNotThrow(()-> {
            game.shove(shovePosition, game.getSpareTile());
        });
    }
    @Test
    void testCanGetTile() {
        Game game = TestUtils.createPlayingGame("CanGetTile");

        assertNull(game.getMaze().getTile(99, 0));
        assertNull(game.getMaze().getTile(0, 99));
    }

    @Test
    void testDeleteGame() {
        Game game = TestUtils.createPlayingGame("deleteGame");

        Game.removeGame(game.getGameId());

        assertFalse(Game.getGames().contains(game));
    }

    @Test
    void testCanDeleteGame() {
        assertThrowsExactly(IllegalStateException.class, () -> Game.removeGame("fakeGameId"));
    }

    @Test
    void testRemoveAllGames() {
        TestUtils.createPlayingGame("removeAllGames1");
        TestUtils.createPlayingGame("removeAllGames2");

        Game.removeGames();

        assertTrue(Game.getGames().isEmpty());
    }
}
