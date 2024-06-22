package game.logic;

import game.logic.util.SafeString;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

import static game.logic.LabyrinthService.random;

public class Game {
    private static final List<Game> games = new ArrayList<>();
    private final String prefix;
    private final SafeString name;
    private final GameMode gameMode;
    private final int maxPlayers;
    private final int treasuresRequired;
    private final TreasureManager treasures;
    private final List<Player> players;
    private Player currentShovePlayer;
    private Player currentMovePlayer;
    private Maze maze;
    private Tile spareTile;
    private final int mazeRows;
    private final int mazeCols;

    public Game(String prefix, SafeString name, GameMode gameMode, int maxPlayers, int treasuresRequired) {
        this(prefix, name, gameMode, maxPlayers, treasuresRequired, 7, 7);
    }

    public Game(String prefix, SafeString name, GameMode gameMode, int maxPlayers, int treasuresRequired, int mazeRows, int mazeCols) {
        if (!isValid(maxPlayers, treasuresRequired)) throw new IllegalArgumentException();
        if (gameExists(name)) throw new IllegalStateException("Game with same name already exists");
        this.treasures = new TreasureManager(null);
        if (!isValidSizeForTreasures(mazeCols, mazeRows))
            throw new IllegalStateException("Maze is too small for treasure amount");
        this.prefix = prefix;
        this.name = name;
        this.gameMode = gameMode;
        this.maxPlayers = maxPlayers;
        this.treasuresRequired = treasuresRequired;
        this.mazeRows = mazeRows;
        this.mazeCols = mazeCols;
        players = new ArrayList<>();
        games.add(this);
    }

    @JsonIgnore
    public String getPrefix() {
        return prefix;
    }

    @JsonProperty("gameName")
    public String getName() {
        return name.toString();
    }

    @JsonIgnore
    public GameMode getGameMode() {
        return gameMode;
    }

    @JsonProperty("gameMode")
    public String getGameModeValue() {
        return gameMode.getValue();
    }

    @JsonIgnore
    public List<Player> getPlayers() {
        return players;
    }

    @JsonIgnore
    public Player getPlayer(String name) {
        for (Player player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        throw new LabyrinthResourceNotFoundException("Player not found");
    }

    @JsonProperty("players")
    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();

        for (Player player : getPlayers()) {
            playerNames.add(player.getName());
        }

        return playerNames;
    }

    @JsonProperty("maximumPlayers")
    public int getMaxPlayers() {
        return maxPlayers;
    }

    @JsonProperty("minimumPlayers")
    public int getMinPlayers() {
        return 2;
    }

    @JsonProperty("treasuresRequired")
    public int getTreasuresRequired() {
        return treasuresRequired;
    }

    @JsonIgnore
    public Player getCurrentShovePlayer() {
        return currentShovePlayer;
    }

    @JsonProperty("currentShovePlayer")
    public String getCurrentShoveName() {
        if (currentShovePlayer == null) return null;
        return currentShovePlayer.getName();
    }

    @JsonIgnore
    public Player getCurrentMovePlayer() {
        return currentMovePlayer;
    }

    @JsonProperty("currentMovePlayer")
    public String getCurrentMoveName() {
        if (currentMovePlayer == null) return null;
        return currentMovePlayer.getName();
    }

    @JsonIgnore
    public Maze getMaze() {
        return maze;
    }

    @JsonIgnore
    public Tile getSpareTile() {
        return spareTile;
    }

    public static List<Game> getGames() {
        return games;
    }

    @JsonProperty("gameId")
    public String getGameId() {
        return String.format("%s_%s", prefix, getName());
    }

    public static boolean gameExists(SafeString name) {
        for (Game game : games) {
            if (game.getName().equals(name.toString())) {
                return true;
            }
        }
        return false;
    }

    public void setRandomObjective(Player player) {
        String random = treasures.getRandom();

        while (random.equals(player.getObjective()) || player.getTreasuresFound().contains(random)) {
            random = treasures.getRandom();
        }

        player.setObjective(random);
    }

    public static void removeGames() {
        games.clear();
    }

    public static void removeGame(String gameId) {
        for (Game currentGame : games) {
            if (currentGame.getGameId().equals(gameId)) {
                games.remove(currentGame);
                return;
            }
        }
        throw new IllegalStateException("Game does not exist");
    }

    private static boolean isValid(int maxPlayers, int treasuresRequired) {
        return (maxPlayers >= 2 && maxPlayers <= 8) && treasuresRequired >= 1;
    }

    public boolean isValidSizeForTreasures(int cols, int rows) {
        return (cols * rows - 4) >= treasures.getCount();
    }

    public void addPlayer(Player player) {
        if (getPlayerNames().contains(player.getName())) throw new IllegalStateException("There is already a player in the game with this name");
        if(players.size() == maxPlayers) throw new IllegalStateException("Game is full");

        players.add(player);
        player.setPlayerState(PlayerState.WAITING);

        if (players.size() == maxPlayers) {
            startGame();
        }
    }

    public void createMaze(int rows, int cols) {
        maze = new Maze(rows, cols);
        Position position = new Position(0, 0);

        Map<Position, String> treasurePositions = mapTreasurePositions(rows, cols);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                position.setPosition(row, col);

                int randomIndex = random.nextInt(Tile.tileType.length);

                Tile tile;

                if ((row == 0) && (col == 0)) {
                    tile = new Tile(new boolean[]{true, false, false, true});
                } else if ((row == 0) && (col == cols - 1)) {
                    tile = new Tile(new boolean[]{true, true, false, false});
                } else if ((row == rows - 1) && (col == 0)) {
                    tile = new Tile(new boolean[]{false, false, true, true});
                } else if ((row == rows - 1) && (col == cols - 1)) {
                    tile = new Tile(new boolean[]{false, true, true, false});
                } else if (treasurePositions.containsKey(new Position(row, col))) {
                    tile = new Tile(Tile.tileType[randomIndex], treasurePositions.get(new Position(row, col)));
                } else {
                    tile = new Tile(Tile.tileType[randomIndex]);
                }

                maze.addTile(tile, position);
            }
        }

        spareTile = new Tile(Tile.tileType[random.nextInt(Tile.tileType.length)]);
    }

    public Map<Position, String> mapTreasurePositions(int rows, int cols) {
        Map<Position, String> treasurePositions = new HashMap<>();

        treasurePositions.put(new Position(0, 0), null);
        treasurePositions.put(new Position(0, cols - 1), null);
        treasurePositions.put(new Position(rows - 1, 0), null);
        treasurePositions.put(new Position(rows - 1, cols - 1), null);

        for (String treasure : treasures.getChosenTreasures()) {
            int randomRow;
            int randomCol;

            boolean isnull = true;

            while (isnull) {
                randomRow = random.nextInt(rows);
                randomCol = random.nextInt(cols);

                if (!treasurePositions.containsKey(new Position(randomRow, randomCol))) {
                    treasurePositions.put(new Position(randomRow, randomCol), treasure);
                    isnull = false;
                }
            }
        }

        return treasurePositions;
    }

    public void loadPlayers() {
        int i = 0;
        for (Player player : this.players) {
            if (i % 4 == 0) {
                maze.getTile(0, 0).addPlayer(player);
            } else if (i % 4 == 1) {
                maze.getTile(0, maze.getCols() - 1).addPlayer(player);
            } else if (i % 4 == 2) {
                maze.getTile(maze.getRows() - 1, 0).addPlayer(player);
            } else if (i % 4 == 3) {
                maze.getTile(maze.getRows() - 1, maze.getCols() - 1).addPlayer(player);
            }
            i++;
        }
    }

    public void shove(Position targetPosition, Tile shoveTile) {
        int targetRow = targetPosition.getRow();
        int targetCol = targetPosition.getCol();

        if (!spareTile.getRotations().contains(shoveTile)) {
            throw new IllegalArgumentException("Tile is not valid");
        }

        if (gameMode == GameMode.HARDCORE && !spareTile.equals(shoveTile)) {
            throw new IllegalArgumentException("Rotating tiles is not possible in hardcore mode");
        }

        if (getMaze().getOppositePosition().equals(targetPosition)) {
            throw new IllegalArgumentException("Shoving in the opposite position is not possible");
        }

        for (Position position : maze.getPossibleShovePositions()) {
            if (position.equals(targetPosition)) {
                spareTile = maze.shoveSides(targetRow, targetCol, new Tile(shoveTile.getWalls(), spareTile.getTreasure()));
                shovePlayers(targetRow, targetCol);

                currentMovePlayer = currentShovePlayer;
                currentShovePlayer = null;
                return;
            }
        }
        throw new IllegalArgumentException("You cannot shove here");
    }

    public void move(Position targetPosition) {
        if (!maze.isPathValid(getCurrentMovePlayer().getLocation(), targetPosition)) {
            throw new IllegalStateException("Unreachable tile");
        }

        Tile targetTile = maze.getTile(targetPosition);
        String treasureOnTarget = targetTile.getTreasure();

        movePlayer(targetTile, targetPosition);

        collectTreasure(treasureOnTarget, getCurrentMovePlayer());

        checkAndEndGame();

        switchTurn();
    }

    public void collectTreasure(String treasure, Player player) {
        if (treasure != null && treasure.equals(player.getObjective())) {
            player.getTreasuresFound().add(treasure);
            setRandomObjective(player);
        }
    }

    public void movePlayer(Tile targetTile, Position targetPosition) {
        Tile currentTile = maze.getTile(getCurrentMovePlayer().getLocation());

        currentTile.removePlayer(getCurrentMovePlayer());
        targetTile.addPlayer(getCurrentMovePlayer());
        getCurrentMovePlayer().setLocation(targetPosition);
    }

    public void shovePlayers(int targetRow, int targetCol) {
        players.forEach((player -> {
            if (targetCol == 0 && player.getLocation().getRow() == targetRow) {
                player.getLocation().move(0, 1);
            } else if (targetCol == maze.getCols() - 1 && player.getLocation().getRow() == targetRow) {
                player.getLocation().move(0, -1);
            } else if (targetRow == 0 && player.getLocation().getCol() == targetCol) {
                player.getLocation().move(1, 0);
            } else if (targetRow == maze.getRows() - 1 && player.getLocation().getCol() == targetCol) {
                player.getLocation().move(-1, 0);
            }

            player.setLocation(maze.getPositionWithinBounds(player.getLocation()));
        }));

        putPushedPlayersBackOnBoard(targetRow, targetCol);
    }

    private void putPushedPlayersBackOnBoard(int targetRow, int targetCol) {
        if (targetCol == 0) {
            maze.getTile(targetRow, 0).getPlayers().addAll(spareTile.getPlayers());
        } else if (targetCol == maze.getCols() - 1) {
            maze.getTile(targetRow, maze.getCols() - 1).getPlayers().addAll(spareTile.getPlayers());
        } else if (targetRow == 0) {
            maze.getTile(0, targetCol).getPlayers().addAll(spareTile.getPlayers());
        } else if (targetRow == maze.getRows() - 1) {
            maze.getTile(maze.getRows() - 1, targetCol).getPlayers().addAll(spareTile.getPlayers());
        }
        spareTile.getPlayers().clear();
    }

    public void switchTurn() {
        int nextTurnIndex = (players.indexOf(currentMovePlayer) + 1) % players.size();

        currentShovePlayer = players.get(nextTurnIndex);

        currentMovePlayer = null;
    }

    public void checkAndEndGame() {
        if (getCurrentMovePlayer().getTreasuresFound().size() == treasuresRequired) {
            for (Player player : this.players) {
                player.setPlayerState(PlayerState.LOST);
            }
            getCurrentMovePlayer().setPlayerState(PlayerState.WON);
        }
    }

    public void loadPlayerStateAndLocation() {
        int i = 0;
        for (Player player : this.players) {
            player.setPlayerState(PlayerState.PLAYING);
            setRandomObjective(player);
            if (i % 4 == 0) {
                player.setLocation(new Position(0, 0));
            } else if (i % 4 == 1) {
                player.setLocation(new Position(0, maze.getCols() - 1));
            } else if (i % 4 == 2) {
                player.setLocation(new Position(maze.getRows() - 1, 0));
            } else if (i % 4 == 3) {
                player.setLocation(new Position(maze.getRows() - 1, maze.getCols() - 1));
            }

            i++;
        }
    }

    public void startGame() {
        createMaze(mazeRows, mazeCols);
        loadPlayerStateAndLocation();

        loadPlayers();
        this.currentShovePlayer = this.players.get(0);
    }

    public boolean isLobbyEmpty() {
        for (Player player : players) {
            String playerState = player.getPlayerState().toString();
            if (!playerState.equals(PlayerState.LEFT.toString())) {
                return false;
            }
        }

        return true;
    }

    public void removePlayer(Player player) {
        if (player.getPlayerState().equals(PlayerState.PLAYING)) {
            player.setPlayerState(PlayerState.LEFT);
        } else {
            players.remove(player);
        }

        if (isLobbyEmpty()) {
            Game.removeGame(getGameId());
        }
    }
}
