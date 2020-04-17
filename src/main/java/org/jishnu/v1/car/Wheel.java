package org.jishnu.v1.car;

import org.jishnu.v1.physics.Direction;
import org.jishnu.v1.physics.PointMass;

public class Wheel {

    PointMass pointMass;
    Direction direction;

    public Wheel(PointMass pointMass, Direction direction) {
        this.pointMass = pointMass;
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection() {
        this.direction = direction;
    }

    public PointMass getPointMass() {
        return pointMass;
    }
}
