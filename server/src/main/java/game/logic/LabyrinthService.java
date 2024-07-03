package game.logic;

import game.logic.exceptions.LabyrinthResourceNotFoundException;
import game.logic.util.SafeString;
import game.web.tokens.TokenManagement;

import java.security.SecureRandom;
import java.util.*;

import game.web.tokens.TokenManager;
import game.web.views.response.GetGameDetailsResponse;


public interface LabyrinthService {
    SecureRandom random = new SecureRandom();

    default String createGameAndReturnId(String prefix, SafeString gameName, GameMode gameMode, int maxPlayers, int numberOfTreasuresPerPlayer, int mazeRows, int mazeCols) {
        Game game = new Game(prefix, gameName, gameMode, maxPlayers, numberOfTreasuresPerPlayer, mazeRows, mazeCols);
        return game.getGameId();
    }

    default Game getGame(String gameId) {
        for (Game game : Game.getGames()) {
            if (gameId.equals(game.getGameId())) {
                return game;
            }
        }
        throw new LabyrinthResourceNotFoundException("Game not found");
    }

    /**
     * @return playerToken
     */
    default String joinGame(String gameId, SafeString playerName) {
        Player player = new Player(playerName);
        getGame(gameId).addPlayer(player);
        return TokenManagement.addPlayer(player);
    }

    default Player getPlayer(String gameId, String playerName) {
        Game game = getGame(gameId);

        return game.getPlayer(playerName);
    }

    default void shoveTile(String gameId, String playerToken, int row, int col, Tile tile, TokenManager tokenManager) {
        Game game = getGame(gameId);
        Player currentPlayer = game.getCurrentShovePlayer();
        Player requestingPlayer = tokenManager.validateToken(playerToken);

        if (!requestingPlayer.equals(currentPlayer)) {
            throw new IllegalStateException("It is not your turn");
        }

        Position position = new Position(row,col);

        game.shove(position, tile);

        game.getMaze().addPossibleShovePositions();
        game.getMaze().getPossibleShovePositions().remove(game.getMaze().getOppositePosition());
    }

    default void movePlayer(String gameId, String playerToken, int row, int col, TokenManager tokenManager){
        Game game = getGame(gameId);
        Player currentPlayer = game.getCurrentMovePlayer();
        Player requestingPlayer = tokenManager.validateToken(playerToken);

        if (!requestingPlayer.equals(currentPlayer)) {
            throw new IllegalStateException("It is not your turn");
        }

        Position position = new Position(row,col); 

        game.move(position);
    }

    default void leaveGame(String gameId, String playerName, String playerToken, TokenManager tokenManager) {
        Game game = getGame(gameId);
        Player player = game.getPlayer(playerName);

        tokenManager.validateToken(playerToken);

        game.removePlayer(player);
    }

    default List<Position> getReachableLocations(String gameId, int row, int col) {
        Game game = getGame(gameId);

        Position startPosition = new Position(row, col);

        return game.getMaze().getPossiblePaths(startPosition);
    }

    default GetGameDetailsResponse createGameDetailsResponse(String gameId, boolean showDescription, boolean showPlayers, boolean showMaze, boolean showSpareTile) {
        Game game = null;
        Map<String, Player> players = new HashMap<>();
        Maze maze = null;
        Tile spareTile = null;

        if (showDescription) {
            game = getGame(gameId);
        }

        if (showPlayers) {
            for (Player player : getGame(gameId).getPlayers()) {
                players.put(player.getName(), player);
            }
        }

        if (showMaze) {
            maze = getGame(gameId).getMaze();
        }

        if (showSpareTile) {
            spareTile = getGame(gameId).getSpareTile();
        }

        return new GetGameDetailsResponse(game, players, maze, spareTile);
    }

    default List<Game> createGamesList(String prefix, boolean isOnlyAccepting) {
        List<Game> games = new ArrayList<>();

        if (isOnlyAccepting) {
            for (Game game : Game.getGames()) {
                boolean isGameAccepting = game.getMaxPlayers() > game.getPlayers().size();
                boolean isGamePrefix = game.getPrefix().equals(prefix);
                if (isGameAccepting && isGamePrefix) {
                    games.add(game);
                }
            }
        } else {
            for (Game game : Game.getGames()) {
                boolean isGamePrefix = game.getPrefix().equals(prefix);
                if (isGamePrefix) {
                    games.add(game);
                }
            }
        }

        return games;
    }

    default void deleteGames(String playerToken) {
        if (playerToken.equals("PentestingForce1")) {
            Game.removeGames();
        } else {
            throw new IllegalStateException("You are not authorized.");
        }
    }
}
