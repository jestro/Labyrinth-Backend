package game.logic.exceptions;

import framework.logic.GameResourceNotFoundException;

public class LabyrinthResourceNotFoundException extends GameResourceNotFoundException {
    public LabyrinthResourceNotFoundException(String msg) {
        super(msg);
    }
}
