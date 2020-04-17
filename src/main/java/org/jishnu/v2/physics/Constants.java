package org.jishnu.v2.physics;

import org.jishnu.v1.physics.Direction;
import org.jishnu.v1.physics.Force;

public class Constants {
    public static final Force g = new Force(9.8f, new Direction(0, 0, 1));
    public static final int wheelRoadFriction = 1000;
    public static final int rollingFriction = 10;
    public static final int airFriction = 2;

}