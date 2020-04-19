package org.jishnu.v2.cars;

public class CarConstants {
    public static final int breakStepCount = 10;
    public static final int powerStepCount = 10;
    public static final int steeringStepCount = 10;
    public static final int accelerationForce = 100;
    public static final int brakeForce = 100;
    public static final int chassisWidth = 50;
    public static final int chassisLength = 90;
    public static final int chassisWeight = 500;
    public static final int viewDistance = 400;
    public static final int viewDistanceSteps = 10;
    public static final int sideViewCount = 1;
    public static final float turnAngle = (float) Math.PI / 12;
    public static final float sideViewAngle = (float) Math.PI / 4;
    public static final int carControlCount = 0;
    public static final CarControl[] carControls = {
            (car, level) -> car.accelerate(level),
            (car, level) -> car.brake(level),
            (car, level) -> car.steerLeft(level),
            (car, level) -> car.steerRight(level)
    };
}