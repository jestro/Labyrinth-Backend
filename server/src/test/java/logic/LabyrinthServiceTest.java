package logic;

import game.logic.util.SafeString;

import game.logic.*;
import game.web.tokens.InvalidTokenException;
import game.web.tokens.TokenManagement;
import game.web.tokens.TokenManager;
import game.web.views.response.GetGameDetailsResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LabyrinthServiceTest {
    LabyrinthService testService = new LabyrinthServiceImpl();
    TokenManager testTokenManager = new TokenManagement();

    @Test
    void testGetGame() {
        Game game = new Game("group13", new SafeString("test"), GameMode.SIMPLE, 2, 1);
        Game retrievedGame = testService.getGame("group13_test");

        assertEquals(game, retrievedGame);
    }

    @Test
    void testCanGetGame() {
        assertThrows(LabyrinthResourceNotFoundException.class, () -> testService.getGame("invalidID"));
    }

    @Test
    void testJoinGame() {
        Game game = new Game("group13", new SafeString("joinGame"), GameMode.SIMPLE, 2, 1);
        SafeString name = new SafeString("player1");

        String playerToken = testService.joinGame(game.getGameId(), name);

        assertNotNull(playerToken);
    }

    @Test
    void testLeaveGame() {
        String gameId = testService.createGameAndReturnId("group13", new SafeString("leaveGame"), GameMode.SIMPLE, 2, 1, 7, 9);
        String token = testService.joinGame(gameId, new SafeString("Player1"));
        testService.leaveGame(gameId, "Player1", token, testTokenManager);

        assertThrows(LabyrinthResourceNotFoundException.class, () -> testService.getGame(gameId));
    }

    @Test
    void testCanLeaveGame() {
        String gameId = testService.createGameAndReturnId("group13", new SafeString("canLeaveGame"), GameMode.SIMPLE, 2, 1, 7, 7);
        testService.joinGame(gameId, new SafeString("Player1"));

        assertThrows(InvalidTokenException.class, () -> testService.leaveGame(gameId, "Player1", "NOT MY TOKEN", testTokenManager));
    }

    @Test
    void testDeleteGames() {
        TestUtils.createPlayingGame("deleteGames");
        boolean isAuthorized = testService.deleteGames("PentestingForce1");

        assertTrue(isAuthorized);
        assertTrue(Game.getGames().isEmpty());
    }

    @Test
    void testCanDeleteGames() {
        TestUtils.createPlayingGame("deleteGames");
        boolean isAuthorized = testService.deleteGames("Random");

        assertFalse(isAuthorized);
        assertFalse(Game.getGames().isEmpty());
    }

    @Test
    void testShoveTile() {
        String gameId = testService.createGameAndReturnId("group13", new SafeString("normalStartGameAndShove"), GameMode.SIMPLE, 2, 1, 7, 7);
        Game game = testService.getGame(gameId);
        String token = testService.joinGame(gameId, new SafeString("Player1"));
        testService.joinGame(gameId, new SafeString("Player2"));
        Tile spareTile = game.getSpareTile();

        testService.shoveTile(gameId, token, 0, 5, spareTile, testTokenManager);

        assertEquals(spareTile, game.getMaze().getTile(0, 5));
    }

    @Test
    void testCanShoveTile() {
        String gameId = testService.createGameAndReturnId("group13", new SafeString("normalStartGameAndIllegalShove"), GameMode.SIMPLE, 2, 1, 7, 7);
        Game game = testService.getGame(gameId);
        testService.joinGame(gameId, new SafeString("Player1"));
        String token2 = testService.joinGame(gameId, new SafeString("Player2"));
        Tile spareTile = game.getSpareTile();

        assertThrows(IllegalStateException.class, () -> testService.shoveTile(gameId, token2, 0, 5, spareTile, testTokenManager));
    }

    @Test
    void testMovePlayer() {
        String gameId = testService.createGameAndReturnId("group13", new SafeString("normalStartGameAndMove"), GameMode.SIMPLE, 2, 1, 9, 7);
        Game game = testService.getGame(gameId);
        String token = testService.joinGame(gameId, new SafeString("Player1"));
        testService.joinGame(gameId, new SafeString("Player2"));
        testService.shoveTile(gameId, token, 0, 5, game.getSpareTile(), testTokenManager);

        Player player1 = game.getPlayer("Player1");
        List<Position> reachableLocations = testService.getReachableLocations(gameId, player1.getLocation().getRow(), player1.getLocation().getCol());
        Position movePosition = reachableLocations.get(reachableLocations.size() - 1);

        assertDoesNotThrow(() -> testService.movePlayer(gameId, token, movePosition.getRow(), movePosition.getCol(), testTokenManager));
    }

    @Test
    void testCanMovePlayer() {
        String gameId = testService.createGameAndReturnId("group13", new SafeString("normalStartGameAndIllegalMove"), GameMode.SIMPLE, 2, 1, 7, 7);
        String token = testService.joinGame(gameId, new SafeString("Player1"));
        testService.joinGame(gameId, new SafeString("Player2"));

        assertThrows(IllegalStateException.class, () -> testService.movePlayer(gameId, token, 0, 5, testTokenManager));
    }

    @Test
    void testCreateGameDetailsResponse() {
        Game game = TestUtils.createPlayingGame("createGameDetailsResponse");
        GetGameDetailsResponse fullResponse = testService.createGameDetailsResponse(game.getGameId(), false, false, false, false);
        GetGameDetailsResponse emptyResponse = testService.createGameDetailsResponse(game.getGameId(), true, true, true, true);

        assertEquals(200, fullResponse.getStatus());
        assertEquals(200, emptyResponse.getStatus());
    }

    @Test
    void testGetPlayerDetails() {
        String gameId = testService.createGameAndReturnId("group13", new SafeString("getPlayerDetails"), GameMode.SIMPLE, 2, 1, 7, 7);
        testService.joinGame(gameId, new SafeString("Player1"));

        assertDoesNotThrow(()-> {
            testService.getPlayerDetails(gameId, "Player1");
        });
    }

    @Test
    void testCanGetPlayerDetails() {
        String gameId = testService.createGameAndReturnId("group13", new SafeString("canGetPlayerDetails"), GameMode.SIMPLE, 2, 1, 7, 7);
        testService.joinGame(gameId, new SafeString("Player1"));

        assertThrows(LabyrinthResourceNotFoundException.class, ()-> testService.getPlayerDetails(gameId, "Nobody"));
    }

    @Test
    void testCreateGamesList() {
        Game emptyGame = new Game("group13", new SafeString("CreateGamesList1"), GameMode.SIMPLE, 2, 1);
        Game uniquePrefixGame = new Game("test", new SafeString("CreateGamesList2"), GameMode.SIMPLE, 2, 1);
        Game fullGame = TestUtils.createPlayingGame("CreateGamesList3");
        List<Game> gamesListOnlyAccepting = testService.createGamesList("group13", true);
        List<Game> gamesList = testService.createGamesList("group13", false);

        assertFalse(gamesListOnlyAccepting.contains(fullGame));
        assertTrue(gamesListOnlyAccepting.contains(emptyGame));
        assertFalse(gamesListOnlyAccepting.contains(uniquePrefixGame));
        assertTrue(gamesList.contains(fullGame));
        assertFalse(gamesList.contains(uniquePrefixGame));
        assertTrue(gamesList.contains(emptyGame));
    }
}