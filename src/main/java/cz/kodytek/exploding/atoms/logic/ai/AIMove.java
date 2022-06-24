package cz.kodytek.exploding.atoms.logic.ai;

import cz.kodytek.exploding.atoms.logic.models.Coordinate;

public class AIMove {
    private final int status;
    private final Coordinate coords;

    public AIMove(int status, Coordinate coords) {
        this.status = status;
        this.coords = coords;
    }

    public int getStatus() {
        return status;
    }

    public Coordinate getCoords() {
        return coords;
    }
}
