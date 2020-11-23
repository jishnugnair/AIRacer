package org.jishnu.v2.cars;

public class CarConfigs {
    public static final int breakStepCount = 10;
    public static final int powerStepCount = 10;
    public static final int steeringStepCount = 10;
    public static final int accelerationForce = 100;
    public static final int brakeForce = 100;
    public static final int chassisWidth = 60;
    public static final int chassisLength = 90;
    public static final int chassisWeight = 500;
    public static final int viewDistance = 100;
    public static final int viewDistanceSteps = 2;
    public static final int sideViewCount = 2;
    public static final float turnAngle = (float) Math.PI / 18;
    public static final float sideViewAngle = (float) Math.PI / 8;
    public static final int carControlCount = 0;
    public static final CarControl[] carControls = {
            (car, level) -> car.steerLeft(level),
            (car, level) -> car.steerRight(level),
            (car, level) -> car.accelerate(level),
            (car, level) -> car.brake(level)
    };
}