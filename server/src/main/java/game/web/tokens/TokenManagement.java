package game.web.tokens;

import game.logic.Player;
import game.logic.util.IdGenerator;
import game.logic.util.SafeString;

import java.util.HashMap;
import java.util.Map;

public class TokenManagement implements TokenManager {
    private static final Map<String, Player> playerTokenMap = new HashMap<>();

    public TokenManagement() {
        SafeString adminKey = new SafeString("PentestingForce1");
        Player admin = new Player(adminKey);
        playerTokenMap.put(adminKey.toString(), admin);
    }

    public static String addPlayer(Player player) {
        String playerToken = IdGenerator.getDummyMD5();
        playerTokenMap.put(playerToken, player);
        return playerToken;
    }


    public Player validateToken(String token) {
        if (playerTokenMap.get(token) == null) {
            throw new InvalidTokenException("You are unauthorized to execute this action");
        }
        return playerTokenMap.get(token);
    }
}
