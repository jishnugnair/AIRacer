package org.jishnu.v2.cars;

import java.awt.*;
import java.awt.geom.Line2D;

public class Car {
    private int length = CarConstants.chassisLength / 2;
    private int breadth = CarConstants.chassisWidth / 2;
    private int diagonal = (int) Math.hypot(length, breadth);
    private float diagonalAngle = (float) Math.atan2(breadth, length);
    private int mass = CarConstants.chassisWeight;
    private int wheelPower = CarConstants.accelerationForce / CarConstants.powerStepCount;
    private int brakePower = CarConstants.brakeForce / CarConstants.breakStepCount;
    private float steeringAngle = CarConstants.turnAngle / CarConstants.steeringStepCount;
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
    private float startVelocity = 6;
    private float velocity = startVelocity;
    private int lookingDirections = CarConstants.sideViewCount * 2 + 1;
    private Line2D[] lineOfSight = new Line2D[lookingDirections];
    private Color color;
    private float pi = (float) Math.PI;
    private int[] stats = new int[CarConstants.sideViewCount];
    private boolean carAlive = true;
    private boolean[][] trackLayout;

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

    public void resetCar(long timestamp) {
        positionX = startPositionX;
        positionY = startPositionY;
        carDirection = startCarDirection;
        velocity = startVelocity;
        prevTimestamp = timestamp;
        currentTimestamp = ++timestamp;
        wheelForceApplied = 0;
        travelDistance = 0;
        travelTime = 0;
        carAlive = true;
    }

    public void accelerate(int level) {
        if (carAlive && currentTimestamp > prevTimestamp) {
            wheelForceApplied = wheelPower * level;
            if (wheelForceApplied != 0)
                velocity += (float) mass / wheelForceApplied;
            prevTimestamp = currentTimestamp;
            wheelForceApplied = 0;
        }
    }

    public Color getColor() {
        return color;
    }

    public void brake(int level) {
        //if (currentTimestamp > prevTimestamp && velocity > 0) {
        if (velocity > 0) {
            brakeForceApplied = brakePower * level;
            prevTimestamp = currentTimestamp;
        }
    }

    public void steerLeft(int level) {
        //if (currentTimestamp > prevTimestamp && velocity > 0) {
        if (velocity > 0) {
            carDirection -= steeringAngle * level;
            prevTimestamp = currentTimestamp;
        }
    }

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

    private boolean isCarAlive() {
        calculateEdges();
        for (int i = 0; i < 2; i++) {
            if (trackLayout[xn[i]][yn[i]]) {
                velocity = 0;
                return false;
            }
        }
        return true;
    }

    public boolean isRunning() {
        return carAlive;
    }

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

    public double[] getStats() {
        updatePosition();
        calculateEdges();
        float lookingAngle = -CarConstants.sideViewAngle * CarConstants.sideViewCount;

        double[] distances = new double[lookingDirections];
        int checkPointX = 0;
        int checkPointY = 0;
        for (int i = 0; i < lookingDirections; i++) {
            for (int j = CarConstants.viewDistanceSteps; j <= CarConstants.viewDistance; j += CarConstants.viewDistanceSteps) {
                checkPointX = (int) Math.round(positionX + j * Math.cos(carDirection + lookingAngle));
                checkPointY = (int) Math.round(positionY + j * Math.sin(carDirection + lookingAngle));
                if (trackLayout[checkPointX][checkPointY]) {
                    break;
                }
                distances[i] = (double) (j - CarConstants.viewDistance) / CarConstants.viewDistance;
            }
            lookingAngle += CarConstants.sideViewAngle;
            lineOfSight[i].setLine(positionX, positionY, checkPointX, checkPointY);
        }
        //System.out.println(distances[0] + " " + distances[1] + " " + distances[2]);
        return distances;
    }
}