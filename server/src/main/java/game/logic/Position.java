package game.logic;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Position {
    @JsonProperty("row")
    private int row;
    @JsonProperty("col")
    private int col;

    public Position(int row, int col) {
        setPosition(row, col);
    }
    public Position(Position position) {
        setPosition(position.getRow(), position.getCol());
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
    public void move(int row, int col){
       this.row += row;
       this.col += col;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
