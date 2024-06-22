package game.web;

import framework.web.OpenApiBridge;
import framework.web.Operation;
import framework.web.views.response.ResponseWithHiddenStatus;
import game.logic.*;
import game.web.views.request.*;
import game.web.views.response.*;
import game.logic.util.SafeString;
import game.web.tokens.TokenManagement;
import game.web.tokens.TokenManager;
import io.vertx.ext.web.handler.BearerAuthHandler;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class LabyrinthOpenApiBridge extends OpenApiBridge { // NOSONAR this is not a monster class, it is a bridge :-)

    private static final Logger LOGGER = Logger.getLogger(LabyrinthOpenApiBridge.class.getName());

    @SuppressWarnings({"FieldCanBeLocal"})
    private final TokenManager tokenManager;
    @SuppressWarnings({"FieldCanBeLocal"})
    private final LabyrinthService service;

    public LabyrinthOpenApiBridge() {
        this(new LabyrinthServiceImpl(), new TokenManagement());
    }

    LabyrinthOpenApiBridge(LabyrinthService service, TokenManager tokenManager) {
        setSecurityHandlers(Map.ofEntries(
                Map.entry("playerToken", BearerAuthHandler.create(tokenManager)))
        );

        this.service = service;
        this.tokenManager = tokenManager;

        if (this.tokenManager == null || this.service == null) {
            LOGGER.log(Level.SEVERE, "TokenManager and LabyrinthService are not provided ...");
        }
    }

    /**
     * Added @SuppressWarnings("unused") on top of class to avoid warnings
     * from code inspection for not having any usages.
     * They do have usages, due to the @Operation annotation.
     * Do not get confused, all endpoints work
     * <p>
     * The TreasureListResponse class has more info
     */

    @Operation("get-info")
    public ResponseWithHiddenStatus getInfo(GetInfoRequest ctx) {
        LOGGER.log(Level.INFO, "In request handler of: get-info");
        return new MessageResponse(200, "API Stable");
    }

    @Operation("get-treasures")
    public ResponseWithHiddenStatus getTreasures(GetTreasuresRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: get-treasures");
        return new TreasureListResponse(List.of(TreasureManager.treasureChoices));
    }


    @Operation("get-games")
    public ResponseWithHiddenStatus getGames(GetGamesRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: get-games");
        String prefix = request.getPrefix();
        boolean isOnlyAccepting = request.onlyAccepting();
        List<Game> games = service.createGamesList(prefix, isOnlyAccepting);

        return new GetGamesResponse(games);
    }


    @Operation("create-game")
    public ResponseWithHiddenStatus createGame(CreateGameRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: create-game");
        String playerToken;
        SafeString playerName = request.getPlayerName();
        String prefix = request.getPrefix();
        SafeString gameName = request.getGameName();
        GameMode gameMode = request.getGameMode();
        int maxPlayers = request.getMaxPlayers();
        int numberOfTreasuresPerPlayer = request.getNumberOfTreasuresPerPlayer();
        int mazeRows = request.getMazeRows();
        int mazeCols = request.getMazeCols();

        try {
            String gameId = service.createGameAndReturnId(prefix, gameName, gameMode, maxPlayers, numberOfTreasuresPerPlayer, mazeRows, mazeCols);
            playerToken = service.joinGame(gameId, playerName);
        } catch (Exception e) {
            return new FailureResponse(409, e.getMessage());
        }
        return new CreateGameResponse(200, prefix, gameName, playerName, playerToken);
    }

    @Operation("delete-games")
    public ResponseWithHiddenStatus deleteGames(DeleteGamesRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: delete-games");
        boolean isAuthorized = service.deleteGames(request.getPlayerToken());
        if (isAuthorized) {
            return new MessageResponse(200, "All games have been deleted!");
        }

        return new FailureResponse(401, "You are not authorized to delete games!");
    }

    @Operation("get-game-details")
    public ResponseWithHiddenStatus sendGetGameDetailsResponse(GetGameDetailsRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: get-game-details");
        String gameId = request.getGameId();
        boolean showDescription = request.showDescription();
        boolean showPlayers = request.showPlayers();
        boolean showMaze = request.showMaze();
        boolean showSpareTile = request.showSpareTile();

        return service.createGameDetailsResponse(gameId, showDescription, showPlayers, showMaze, showSpareTile);
    }


    @Operation("shove-tile")
    public ResponseWithHiddenStatus shoveTile(ShoveTileRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: shove-tile");
        String gameId = request.getGameId();
        String playerToken = request.getPlayerToken();
        int row = request.getRow();
        int col = request.getCol();
        Tile tile = request.getTile();

        try {
            service.shoveTile(gameId, playerToken, row, col, tile, tokenManager);
        } catch (Exception e) {
            return new FailureResponse(409, e.getMessage());
        }

        return new ShoveResponse(service.getGame(request.getGameId()).getMaze(), service.getGame(request.getGameId()).getSpareTile());
    }

    @Operation("get-reachable-locations")
    public ResponseWithHiddenStatus getReachableLocations(GetReachableLocationsRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: get-reachable-locations");
        int row = request.getRow();
        int col = request.getCol();
        String gameId = request.getGameId();

        try {
            return new GetReachableLocationsResponse(service.getReachableLocations(gameId, row, col));
        } catch (Exception e) {
            return new FailureResponse(409, e.getMessage());
        }

    }

    @Operation("get-player-details")
    public ResponseWithHiddenStatus getPlayerDetails(GetPlayerDetailsRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: get-player-details");
        String gameId = request.getGameId();
        String playerName = request.getPlayerName();

        try {
            return new GetPlayerDetailsResponse(service.getPlayerDetails(gameId, playerName));
        } catch (Exception e) {
            return new FailureResponse(409, e.getMessage());
        }
    }

    @Operation("join-game")
    public ResponseWithHiddenStatus joinGame(JoinGameRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: join-game");
        String playerToken;
        String gameId = request.getGameId();
        SafeString playerName = request.getPlayerName();

        try {
            playerToken = service.joinGame(gameId, playerName);
        } catch (Exception e) {
            return new FailureResponse(409, e.getMessage());
        }
        return new JoinGameResponse(200, request.getGameId(), request.getPlayerName(), playerToken);
    }

    @Operation("leave-game")
    public ResponseWithHiddenStatus leaveGame(LeaveGameRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: leave-game");
        String gameId = request.getGameId();
        String playerToken = request.getPlayerToken();
        String playerName = request.getPlayerName();

        try {
            service.leaveGame(gameId, playerName, playerToken, tokenManager);
        } catch (LabyrinthResourceNotFoundException e) {
            return new FailureResponse(404, e.getMessage());
        } catch (Exception e) {
            return new FailureResponse(409, e.getMessage());
        }

        return new MessageResponse(200, "Successfully left game");
    }

    @Operation("move-player")
    public ResponseWithHiddenStatus movePlayer(MovePlayerRequest request) {
        LOGGER.log(Level.INFO, "In request handler of: move-player");
        String gameId = request.getGameId();
        String playerToken = request.getPlayerToken();
        int row = request.getRow();
        int col = request.getCol();

        try {
            service.movePlayer(gameId, playerToken, row, col, tokenManager);
        } catch (Exception e) {
            return new FailureResponse(409, e.getMessage());
        }

        return new MoveResponse(service.getGame(request.getGameId()).getPlayer(request.getPlayerName()));
    }


}
