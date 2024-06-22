package logic;

import game.logic.Game;
import game.logic.Position;
import game.logic.Tile;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TileTest {

    @Test
    void testTileClockwise(){
        boolean[] walls = new boolean[] {true, true, false, false};
        boolean[] wallsRotate = new boolean[] {false, true, true, false};

        Tile tile = new Tile(walls);

        tile.rotate(true);

        assertEquals(Arrays.toString(tile.getWalls()), Arrays.toString(wallsRotate));
    }

    @Test
    void testTileCounterClockwise(){
        boolean[] walls = new boolean[] {true, true, false, false};
        boolean[] wallsRotate = new boolean[] {true, false, false, true};

        Tile tile = new Tile(walls);

        tile.rotate(false);

        assertEquals(Arrays.toString(tile.getWalls()), Arrays.toString(wallsRotate));
    }

    @Test
    void testPutPlayerOnTile(){
        Game game = TestUtils.createPlayingGame("PutPlayerOnTile");

        game.getMaze().getTile(0, 1).addPlayer(TestUtils.player1);

        assertTrue(game.getMaze().getTile(0, 1).getPlayers().contains(TestUtils.player1));
    }

    @Test
    void testRemovePlayerFromTile(){
        Game game = TestUtils.createPlayingGame("RemovePlayerFromTile");

        game.getMaze().getTile(0, 0).removePlayer(TestUtils.player1);

        assertFalse(game.getMaze().getTile(0, 0).getPlayers().contains(TestUtils.player1));
    }

    @Test
    void testShovePlayerOnBottomEdge() {
        Game game = TestUtils.createPlayingGame("testShovePlayerOnEdge1");

        Position startPosition = new Position(0, 0);
        Position shovePosition = new Position(0, 3);
        Position edgePosition = new Position(6, 3);

        game.getMaze().getTile(startPosition).removePlayer(TestUtils.player1);
        game.getMaze().getTile(edgePosition).addPlayer(TestUtils.player1);
        TestUtils.player1.setLocation(edgePosition);

        game.shove(shovePosition, game.getSpareTile());

        assertTrue(game.getMaze().getTile(shovePosition).getPlayers().contains(TestUtils.player1));
        assertEquals(TestUtils.player1.getLocation(), shovePosition);
    }

    @Test
    void testShovePlayerOnLeftEdge() {
        Game game = TestUtils.createPlayingGame("testShovePlayerOnEdge2");

        Position startPosition = new Position(0, 0);
        Position shovePosition = new Position(3, 6);
        Position edgePosition = new Position(3, 0);

        game.getMaze().getTile(startPosition).removePlayer(TestUtils.player1);
        game.getMaze().getTile(edgePosition).addPlayer(TestUtils.player1);
        TestUtils.player1.setLocation(edgePosition);

        game.shove(shovePosition, game.getSpareTile());

        assertTrue(game.getMaze().getTile(shovePosition).getPlayers().contains(TestUtils.player1));
        assertEquals(TestUtils.player1.getLocation(), shovePosition);
    }

    @Test
    void testShovePlayerOnTopEdge() {
        Game game = TestUtils.createPlayingGame("testShovePlayerOnEdge3");

        Position startPosition = new Position(0, 0);
        Position shovePosition = new Position(6, 3);
        Position edgePosition = new Position(0, 3);

        game.getMaze().getTile(startPosition).removePlayer(TestUtils.player1);
        game.getMaze().getTile(edgePosition).addPlayer(TestUtils.player1);
        TestUtils.player1.setLocation(edgePosition);

        game.shove(shovePosition, game.getSpareTile());

        assertTrue(game.getMaze().getTile(shovePosition).getPlayers().contains(TestUtils.player1));
        assertEquals(TestUtils.player1.getLocation(), shovePosition);
    }

    @Test
    void testShovePlayerOnRightEdge() {
        Game game = TestUtils.createPlayingGame("testShovePlayerOnEdge4");

        Position firstPosition = new Position(0, 0);
        Position shovePosition = new Position(3, 0);
        Position edgePosition = new Position(3, 6);

        game.getMaze().getTile(firstPosition).removePlayer(TestUtils.player1);
        game.getMaze().getTile(edgePosition).addPlayer(TestUtils.player1);
        TestUtils.player1.setLocation(edgePosition);

        game.shove(shovePosition, game.getSpareTile());

        assertTrue(game.getMaze().getTile(shovePosition).getPlayers().contains(TestUtils.player1));
        assertEquals(TestUtils.player1.getLocation(), shovePosition);
    }

    @Test
    void testCreateTileWithPlayer() {
        Tile tile = new Tile(new boolean[]{true, true, false, false}, null, TestUtils.player1);
        Tile tile2 = new Tile(new boolean[]{true, true, false, false}, null, null);

        assertFalse(tile.getPlayers().isEmpty());
        assertTrue(tile2.getPlayers().isEmpty());
    }

    @Test
    void testGetPlayerNames() {
        Game game = TestUtils.createPlayingGame("GetPlayerNamesTile");
        Tile tile = game.getMaze().getTile(0,0);
        List<String> playerNames = new ArrayList<>();

        playerNames.add(TestUtils.player1.getName());

        assertEquals(tile.getPlayerNames(), playerNames);
    }

    @Test
    void testCloneTile() {
        Tile tile = new Tile(new boolean[]{true, true, false, false}, null, TestUtils.player1);
        Tile cloneTile = new Tile(tile);

        assertEquals(tile, cloneTile);
    }
}
