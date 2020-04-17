package org.jishnu.v1.car;

import org.jishnu.v1.physics.Joint;

import java.util.ArrayList;
import java.util.List;

public class CarBuilder {
    private List<Joint> joints = new ArrayList<>();
    private Wheel frontLeftWheel, frontRightWheel, rearLeftWheel, rearRightWheel;
    private float powerFront, powerRear;
    private float brakePowerFront, breakPowerRear;

    public CarBuilder addJoint(Joint joint) {
        joints.add(joint);
        return this;
    }

    public CarBuilder addJoints(List<Joint> joints) {
        joints.addAll(joints);
        return this;
    }

    public CarBuilder setWheels(Wheel frontLeftWheel, Wheel frontRightWheel, Wheel rearLeftWheel, Wheel rearRightWheel) {
        this.frontLeftWheel = frontLeftWheel;
        this.frontRightWheel = frontRightWheel;
        this.rearLeftWheel = rearLeftWheel;
        this.rearRightWheel = rearRightWheel;
        return this;
    }

    public CarBuilder setFrontBackPower(float powerFront, float powerBack) {
        this.powerRear = powerBack / 2 / Constants.powerStepCount;
        this.powerFront = powerFront / 2 / Constants.powerStepCount;
        return this;
    }

    public CarBuilder setFrontBackBrakePower(float brakePowerFront, float brakePowerRear) {
        this.brakePowerFront = brakePowerFront / 2 / Constants.breakStepCount;
        this.breakPowerRear = brakePowerRear / 2 / Constants.breakStepCount;
        return this;
    }

    public Car build() {
        if(joints.size() < 4)
            throw new IllegalArgumentException("Need atleast 4 joints to build a car");

        if(frontLeftWheel == null || frontRightWheel == null
                || rearLeftWheel == null || rearRightWheel == null)
            throw new IllegalArgumentException("Null values provided for a wheel");

        if(powerFront < 0 || powerRear < 0)
            throw new IllegalArgumentException("Power cannot be negative");

        if(powerFront + powerRear == 0)
            throw new IllegalArgumentException("Both front and back power cannot be zero");

        if(brakePowerFront < 0 || breakPowerRear < 0)
            throw new IllegalArgumentException("Break power cannot be negative");

        if(brakePowerFront + breakPowerRear == 0)
            throw new IllegalArgumentException("Both front and back brake power cannot be zero");

        return new Car(
                joints,
                frontLeftWheel,
                frontRightWheel,
                rearLeftWheel,
                rearRightWheel,
                powerFront,
                powerRear,
                brakePowerFront,
                breakPowerRear
        );
    }

}