package org.jishnu.v1.physics;

public class Force {
    private float magnitude;
    private Direction direction;

    public Force(float magnitude, Direction direction) {
        this.magnitude = magnitude;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public float getMagnitude() {
        return magnitude;
    }
}
