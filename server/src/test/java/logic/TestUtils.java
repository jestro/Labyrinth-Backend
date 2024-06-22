package logic;

import game.logic.util.SafeString;
import game.logic.Game;
import game.logic.GameMode;
import game.logic.Player;

public class TestUtils {
    public static Player player1 = new Player(new SafeString("waut"));
    public static Player player2 = new Player(new SafeString("wim"));
    public static Player player3 = new Player(new SafeString("yarne"));
    public static Player player4 = new Player(new SafeString("jestro"));
    public static Player player5 = new Player(new SafeString("lars"));
    public static Player player6 = new Player(new SafeString("mason"));
    public static Player player7 = new Player(new SafeString("jackie"));
    public static Player player8 = new Player(new SafeString("micheal"));

    public static Game createPlayingGame(String name) {
        return createPlayingGame(name, GameMode.SIMPLE);
    }

    public static Game createPlayingGame(String name, GameMode gameMode){
        Game game = new Game("group13", new SafeString(name), gameMode, 2, 1);

        game.addPlayer(player1);
        game.addPlayer(player2);

        return game;
    }
}
