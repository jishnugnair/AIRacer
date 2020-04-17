package org.jishnu.v2.cars;

import java.awt.*;

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
    private float steeringAngleApplied;
    private float carDirection;
    private float deltaPositionX;
    private float deltaPositionY;
    private float positionX;
    private float positionY;
    private int[] xn = new int[4];
    private int[] yn = new int[4];
    private long prevTimestamp;
    private long currentTimestamp;
    private float velocity = 10;
    private Color color;
    private float pi = (float) Math.PI;
    private int[] stats = new int[CarConstants.sideViewCount];
    private boolean carAlive = true;
    private boolean[][] trackLayout;

    public Car(int positionX, int positionY, float carDirection, Color color, boolean[][] trackLayout, long timestamp) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.carDirection = carDirection;
        this.color = color;
        this.trackLayout = trackLayout;
        prevTimestamp = timestamp;
        currentTimestamp = ++timestamp;
    }

    public void accelerate(int level) {
        if (carAlive && currentTimestamp > prevTimestamp) {
            wheelForceApplied = wheelPower * level;
            if (wheelForceApplied != 0)
                velocity += mass / wheelForceApplied;
            prevTimestamp = currentTimestamp;
            wheelForceApplied = 0;
        }
    }

    public Color getColor() {
        return color;
    }

    public void brake(int level) {
        if (currentTimestamp > prevTimestamp && velocity > 0) {
            brakeForceApplied = brakePower * level;
            prevTimestamp = currentTimestamp;
        }
    }

    public void steerLeft(int level) {
        if (currentTimestamp > prevTimestamp && velocity > 0) {
            carDirection -= steeringAngle * level;
            prevTimestamp = currentTimestamp;
        }
    }

    public void steerRight(int level) {
        if (currentTimestamp > prevTimestamp && velocity > 0) {
            carDirection += steeringAngle * level;
            prevTimestamp = currentTimestamp;
        }
    }

    public void setStartPositionAndDirection(int positionX, int positionY, float carDirection, long timestamp) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.carDirection = carDirection;
        prevTimestamp = timestamp;
        currentTimestamp = timestamp;
    }

    public Polygon getPolygon(long timestamp) {
        if(carAlive) {
            updatePosition(timestamp);
            calculateEdges();
        }
        return new Polygon(xn, yn, 4);
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

    private void updatePosition(long timestamp) {
        currentTimestamp = timestamp;
        deltaPositionX = (float) (Math.cos(carDirection) * velocity * (currentTimestamp - prevTimestamp) / 100);
        deltaPositionY = (float) (Math.sin(carDirection) * velocity * (currentTimestamp - prevTimestamp) / 100);
        positionX += deltaPositionX;
        positionY += deltaPositionY;
        travelDistance += Math.hypot(deltaPositionX, deltaPositionY);
        prevTimestamp = timestamp;
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
        for(int i = 0; i < 2; i++) {
            if(trackLayout[xn[i]][yn[i]]) {
                velocity = 0;
                return false;
            }
        }
        return true;
    }

    public int getPositionX() {
        return Math.round(positionX);
    }

    public int getPositionY() {
        return Math.round(positionY);
    }

    public int getLength() {
        return length;
    }

    public int getBreadth() {
        return breadth;
    }

    public double[] getStats() {
        float lookingAngle = carDirection - CarConstants.sideViewAngle;
        int lookingDirections = CarConstants.sideViewCount * 2 + 1;
        double[] distances = new double[lookingDirections];
        int checkPointX, checkPointY;
        for(int i = 0; i < lookingDirections; i++) {
            for(int j = CarConstants.viewDistanceSteps; j <= CarConstants.viewDistance; j += CarConstants.viewDistanceSteps) {
                checkPointX = (int) Math.round(positionX + j * Math.cos(carDirection + lookingAngle));
                checkPointY = (int) Math.round(positionY + j * Math.sin(carDirection + lookingAngle));
                if(trackLayout[checkPointX][checkPointY]) {
                    break;
                }
                distances[i] = j;
            }
            lookingAngle += CarConstants.sideViewAngle;
        }
        return distances;
    }
}