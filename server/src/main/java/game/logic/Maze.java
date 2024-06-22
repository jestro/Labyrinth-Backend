package game.logic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Maze {
    private final int rows;
    private final int cols;
    private final Tile[][] board;

    @JsonIgnore
    private final List<Position> possibleShovePositions;
    private Position oppositePosition;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.board = new Tile[rows][cols];

        possibleShovePositions = new ArrayList<>();

        addPossibleShovePositions();
    }

    @JsonProperty("rows")
    public int getRows() {
        return rows;
    }

    @JsonProperty("cols")
    public int getCols() {
        return cols;
    }

    @JsonProperty("board")
    public Tile[][] getBoard() {
        return board;
    }


    public List<Position> getPossibleShovePositions() {
        return possibleShovePositions;
    }

    public boolean isPathValid(Position start, Position end) {
        return getPossiblePaths(start).contains(end);
    }

    public List<Position> getPossiblePaths(Position start) {
        Tile startTile = getTile(start);
        if (startTile == null) throw new IllegalArgumentException();
        return searchAdjacentPaths(start, new ArrayList<>());
    }

    private List<Position> searchAdjacentPaths(Position tilePosition, List<Position> knownPaths) {
        Tile tile = getTile(tilePosition);
        //Return the already known paths if this tile is uninteresting
        if (tile == null || knownPaths.contains(tilePosition)) return knownPaths;

        // Adds itself to the list because it is valid
        knownPaths.add(tilePosition);
        boolean[] walls = tile.getWalls();
        //Search further through all its neighbours
        for (int pathIndex = 0; pathIndex < walls.length; pathIndex++) {
            if (!walls[pathIndex]) {
                // Adds onto the knownPaths
                searchAdjacentPaths(getNeighbourPosition(tilePosition, pathIndex), knownPaths);
            }
        }
        return knownPaths;
    }

    private Position getNeighbourPosition(Position position, int pathIndex) {
        int neighbourPathIndex = (pathIndex + 2) % 4;
        // Selects the position that is connected to the current path
        Position neighbourPosition = new Position(position);
        if (pathIndex == 0) neighbourPosition.move(-1, 0);
        if (pathIndex == 1) neighbourPosition.move(0, 1);
        if (pathIndex == 2) neighbourPosition.move(1, 0);
        if (pathIndex == 3) neighbourPosition.move(0, -1);

        Tile neighbour = getTile(neighbourPosition);
        //If neighbouring tile has a path connecting it to the current tile
        if (neighbour != null && !neighbour.getWalls()[neighbourPathIndex]) {
            return neighbourPosition;
        }
        return null;
    }

    public Tile getTile(int row, int col) {
        return getTile(new Position(row, col));
    }

    public Tile getTile(Position position) {
        if (position == null) return null;
        if (isPositionWithinBounds(position)) {
            return board[position.getRow()][position.getCol()];
        }
        return null;
    }

    public Position getOppositePosition() {
        if (oppositePosition == null) {
            return new Position(-1, -1);
        }
        return oppositePosition;
    }

    public boolean isPositionWithinBounds(Position position) {
        return (position.getRow() < rows && position.getRow() >= 0)
                && (position.getCol() < cols && position.getCol() >= 0);
    }

    public void addTile(Tile tile, Position position) {
        board[position.getRow()][position.getCol()] = tile;
    }

    public void addPossibleShovePositions() {
        possibleShovePositions.removeAll(getPossibleShovePositions());
        for (int row = 1; row < rows; row += 2) {
            possibleShovePositions.add(new Position(row, 0));
            possibleShovePositions.add(new Position(row, cols - 1));
        }
        for (int col = 1; col < cols; col += 2) {
            possibleShovePositions.add(new Position(0, col));
            possibleShovePositions.add(new Position(rows - 1, col));
        }
    }

    public Tile shoveSides(int targetRow, int targetCol, Tile spareTile) {
        Tile newSpareTile = null;

        if (targetCol == 0) {
            newSpareTile = board[targetRow][cols - 1];
            oppositePosition = new Position(targetRow, cols - 1);
            for (int i = cols - 1; i >= 1; i--) {
                addTile(board[targetRow][i - 1], new Position(targetRow, i));
            }
        } else if (targetCol == cols - 1) {
            newSpareTile = board[targetRow][0];
            oppositePosition = new Position(targetRow, 0);
            for (int i = 1; i < cols; i++) {
                addTile(board[targetRow][i], new Position(targetRow, i - 1));
            }
        } else if (targetRow == 0) {
            newSpareTile = board[rows - 1][targetCol];
            oppositePosition = new Position(rows - 1, targetCol);
            for (int i = rows - 1; i >= 1; i--) {
                addTile(board[i - 1][targetCol], new Position(i, targetCol));
            }
        } else if (targetRow == rows - 1) {
            newSpareTile = board[0][targetCol];
            oppositePosition = new Position(0, targetCol);
            for (int i = 1; i < rows; i++) {
                addTile(board[i][targetCol], new Position(i - 1, targetCol));
            }
        }

        addTile(spareTile, new Position(targetRow, targetCol));

        return newSpareTile;
    }

    public Position getPositionWithinBounds(Position pos) {
        return new Position((pos.getRow() + rows) % rows, (pos.getCol() + cols) % cols);
    }
}
