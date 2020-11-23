package org.jishnu.v2.cars;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * This class represents a <code>Car</code>, with variables storing state
 * of the car and methods to manipulate and monitor them.
 */
public class Car {
    private int length = CarConfigs.chassisLength / 2;
    private int breadth = CarConfigs.chassisWidth / 2;
    private int diagonal = (int) Math.hypot(length, breadth);
    private float diagonalAngle = (float) Math.atan2(breadth, length);
    private int mass = CarConfigs.chassisWeight;
    private int wheelPower = CarConfigs.accelerationForce / CarConfigs.powerStepCount;
    private int brakePower = CarConfigs.brakeForce / CarConfigs.breakStepCount;
    private float steeringAngle = CarConfigs.turnAngle / CarConfigs.steeringStepCount;
    private float dragCoefficient;
    private float rollingFriction;
    private int wheelForceApplied;
    private int brakeForceApplied;
    private float travelDistance = 0;
    private float travelTime = 0;
    private float steeringAngleApplied;
    private float startCarDirection;
    private float carDirection;
    private float startPositionX;
    private float startPositionY;
    private float positionX;
    private float positionY;
    private int[] xn = new int[4];
    private int[] yn = new int[4];
    private long prevTimestamp;
    private long currentTimestamp;
    private float startVelocity = 10;
    private float velocity = startVelocity;
    private int lookingDirections = CarConfigs.sideViewCount * 2 + 1;
    private Line2D[] lineOfSight = new Line2D[lookingDirections];
    private Color color;
    private float pi = (float) Math.PI;
    private final int[] stats = new int[CarConfigs.sideViewCount];
    private boolean carAlive = true;
    private final boolean[][] trackLayout;

    public Car(int positionX, int positionY, float carDirection, Color color, boolean[][] trackLayout, long timestamp) {
        startPositionX = this.positionX = positionX;
        startPositionY = this.positionY = positionY;
        startCarDirection = this.carDirection = carDirection;
        this.color = color;
        this.trackLayout = trackLayout;
        prevTimestamp = timestamp;
        currentTimestamp = ++timestamp;
        instantiateLineOfSight();
    }

    /**
     * Resets the car's coordinates to the original start position
     */
    public void resetCar() {
        positionX = startPositionX;
        positionY = startPositionY;
        carDirection = startCarDirection;
        velocity = startVelocity;
        prevTimestamp = System.currentTimeMillis();
        currentTimestamp = prevTimestamp + 1;
        wheelForceApplied = 0;
        travelDistance = 0;
        travelTime = 0;
        carAlive = true;
    }

    /**
     * Increases or decreases the applied driving force on wheels
     * @param level sets the amount of driving force applied on wheels hence the acceleration
     */
    public void accelerate(int level) {
        if (carAlive && currentTimestamp > prevTimestamp) {
            wheelForceApplied = wheelPower * level;
            if (wheelForceApplied != 0)
                velocity += (float) mass / wheelForceApplied;
            prevTimestamp = currentTimestamp;
            wheelForceApplied = 0;
            System.out.println("Accelerating");
        }
    }

    public Color getColor() {
        return color;
    }

    /**
     * Increases or decreases the applied braking force on wheels
     * @param level sets the amount of braking force applied on wheels hence the acceleration
     */
    public void brake(int level) {
        //if (currentTimestamp > prevTimestamp && velocity > 0) {
        if (velocity > 0) {
            brakeForceApplied = brakePower * level;
            prevTimestamp = currentTimestamp;
        }
    }

    /**
     * Steers the car left, the angle is divided in discreet steps selected by <b>level</b>
     * @param level sets the angle of wheel
     */
    public void steerLeft(int level) {
        //if (currentTimestamp > prevTimestamp && velocity > 0) {
        if (velocity > 0) {
            carDirection -= steeringAngle * level;
            prevTimestamp = currentTimestamp;
        }
    }

    /**
     * Steers the car right, the angle is divided in discreet steps selected by <b>level</b>
     * @param level sets the angle of wheel
     */
    public void steerRight(int level) {
        //if (currentTimestamp > prevTimestamp && velocity > 0) {
        if (velocity > 0) {
            carDirection += steeringAngle * level;
            prevTimestamp = currentTimestamp;
        }
    }

    public Polygon getPolygon() {
        return new Polygon(xn, yn, 4);
    }

    public Line2D[] getLineOfSight() {
        return lineOfSight;
    }

    /**
     * Calculates the current edges of the car
     */
    private void calculateEdges() {
        xn[0] = (int) Math.round(positionX + diagonal * Math.cos(carDirection - diagonalAngle));
        yn[0] = (int) Math.round(positionY + diagonal * Math.sin(carDirection - diagonalAngle));
        xn[1] = (int) Math.round(positionX + diagonal * Math.cos(carDirection + diagonalAngle));
        yn[1] = (int) Math.round(positionY + diagonal * Math.sin(carDirection + diagonalAngle));
        xn[2] = (int) Math.round(positionX + diagonal * Math.cos(carDirection + pi - diagonalAngle));
        yn[2] = (int) Math.round(positionY + diagonal * Math.sin(carDirection + pi - diagonalAngle));
        xn[3] = (int) Math.round(positionX + diagonal * Math.cos(carDirection + pi + diagonalAngle));
        yn[3] = (int) Math.round(positionY + diagonal * Math.sin(carDirection + pi + diagonalAngle));
    }

    /**
     * Updates the current position of the car
     */
    private void updatePosition() {
        currentTimestamp = System.currentTimeMillis();
        float deltaPositionX = (float) (Math.cos(carDirection) * velocity * (currentTimestamp - prevTimestamp) / 100f);
        float deltaPositionY = (float) (Math.sin(carDirection) * velocity * (currentTimestamp - prevTimestamp) / 100f);
        positionX += deltaPositionX;
        positionY += deltaPositionY;
        travelDistance += Math.hypot(deltaPositionX, deltaPositionY);
        travelTime += currentTimestamp - prevTimestamp;
        prevTimestamp = currentTimestamp;
        carAlive = isCarAlive();
        if (positionX > 1920)
            positionX = 0;
        else if (positionX < 0)
            positionX = 1920;
        else if (positionY > 1080)
            positionY = 0;
        else if (positionY < 0)
            positionY = 1080;
    }

    /**
     * Confirms if the car is alive. A car is considered alive if all of
     * its edges are within boundaries of the road.
     * @return boolean status of car, <b>true</b> if alive and <b>false</b>
     * if dead
     */
    private boolean isCarAlive() {
        calculateEdges();
        for (int i = 0; i < 2; i++) {
            if (trackLayout[xn[i]][yn[i]] || travelDistance > 5500) {
                velocity = 0;
                return false;
            }
        }
        return true;
    }

    public boolean isRunning() {
        return carAlive;
    }

    /**
     * Initializes array of <code>Line2D</code> objects with end points at center of the car
     * and edge of the road in all looking directions
     */
    private void instantiateLineOfSight() {
        for (int i = 0; i < lookingDirections; i++)
            lineOfSight[i] = new Line2D.Float();
    }

    public float getTravelDistance() {
        return travelDistance;
    }

    public float getAverageSpeed() {
        return travelDistance / travelTime;
    }

    /**
     * Calculates the distance of edge of road from car in different directions
     * @return the array of distances between the car and
     * edge of the road in each looking direction
     */
    public double[] getStats() {
        updatePosition();
        calculateEdges();
        float lookingAngle = -CarConfigs.sideViewAngle * CarConfigs.sideViewCount;

        double[] distances = new double[lookingDirections];
        int checkPointX = 0;
        int checkPointY = 0;
        for (int i = 0; i < lookingDirections; i++) {
            for (int j = CarConfigs.viewDistanceSteps; j <= CarConfigs.viewDistance; j += CarConfigs.viewDistanceSteps) {
                checkPointX = (int) Math.round(positionX + j * Math.cos(carDirection + lookingAngle));
                checkPointY = (int) Math.round(positionY + j * Math.sin(carDirection + lookingAngle));
                if (trackLayout[checkPointX][checkPointY]) {
                    break;
                }
                distances[i] = (double) (j - CarConfigs.viewDistance) / CarConfigs.viewDistance;
            }
            lookingAngle += CarConfigs.sideViewAngle;
            lineOfSight[i].setLine(positionX, positionY, checkPointX, checkPointY);
        }
        return distances;
    }
}