package game.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Tile {
    protected static final boolean[][] tileType = {
            new boolean[]{false, false, false, true}, new boolean[]{false, false, true, false},
            new boolean[]{false, true, false, false}, new boolean[]{true, false, false, false},
            new boolean[]{true, false, true, false}, new boolean[]{false, true, false, true},
            new boolean[]{true, true, false, false}, new boolean[]{false, true, true, false},
            new boolean[]{false, false, true, true}, new boolean[]{true, false, false, true}};

    private boolean[] walls;
    private final String treasure;
    @JsonIgnore
    private final List<Player> players;

    public Tile(boolean[] walls, String treasure, Player player) {
        this.walls = walls;
        this.treasure = treasure;
        this.players = new ArrayList<>();
        if (player != null) {
            this.players.add(player);
        }
    }

    public Tile(Tile tile) {
        this(tile.walls.clone(), String.valueOf(tile.treasure));
        this.players.addAll(tile.players);
    }

    public Tile(boolean[] walls, String treasure) {
        this(walls, treasure, null);
    }

    public Tile(boolean[] walls) {
        this(walls, null, null);
    }

    public void rotate(boolean isClockwise) {
        if (isClockwise) {
            walls = new boolean[]{walls[3], walls[0], walls[1], walls[2]};
        } else {
            walls = new boolean[]{walls[1], walls[2], walls[3], walls[0]};
        }
    }

    @JsonIgnore
    public List<Tile> getRotations() {
        List<Tile> rotations = new ArrayList<>();
        rotations.add(new Tile(this));
        for (int i = 0; i < walls.length - 1; i++) {
            rotations.get(i).rotate(true);
            rotations.add(new Tile(rotations.get(i)));
        }
        rotations.get(walls.length - 1).rotate(true);
        return rotations;
    }


    @JsonProperty("walls")
    public boolean[] getWalls() {
        return walls;
    }

    @JsonProperty("treasure")
    public String getTreasure() {
        return treasure;
    }

    @JsonIgnore
    public List<Player> getPlayers() {
        return players;
    }

    @JsonProperty("players")
    public List<String> getPlayerNames() {
        List<String> playerNames = new ArrayList<>();

        for (Player player : getPlayers()) {
            playerNames.add(player.getName());
        }

        return playerNames;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return Arrays.equals(walls, tile.walls);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(treasure, players);
        result = 31 * result + Arrays.hashCode(walls);
        return result;
    }
}
