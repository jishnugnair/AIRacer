package org.jishnu.v1.physics;

public class Direction {

    private int directionX, directionY, directionZ;

    public Direction(int directionX, int directionY, int directionZ) {
        this.directionX = directionX;
        this.directionY = directionY;
        this.directionZ = directionZ;
    }

    public int getDirectionX() {
        return directionX;
    }

    public int getDirectionY() {
        return directionY;
    }

    public int getDirectionZ() {
        return directionZ;
    }
}
