package org.jishnu.v1.car;

import org.jishnu.v1.physics.Joint;
import org.jishnu.v1.physics.PointMass;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Car {
    private List<Joint> joints = new ArrayList<>();
    private Wheel frontLeftWheel, frontRightWheel, rearLeftWheel, rearRightWheel;
    private float powerFront, powerRear;
    private float brakePowerFront, breakPowerRear;
    private float frontWheelForceApplied, rearWheelForceApplied;
    private float frontBrakeForceApplied, rearBrakeForceApplied;
    private int totalMass;
    private Set<PointMass> pointMassSet = new HashSet<>();

    private Car() {
    }

    public Car(
            List<Joint> joints,
            Wheel frontLeftWheel,
            Wheel frontRightWheel,
            Wheel rearLeftWheel,
            Wheel rearRightWheel,
            float powerFront,
            float powerRear,
            float brakePowerFront,
            float breakPowerRear
    ) {
        this.joints = joints;
        this.frontLeftWheel = frontLeftWheel;
        this.frontRightWheel = frontRightWheel;
        this.rearLeftWheel = rearLeftWheel;
        this.rearRightWheel = rearRightWheel;
        this.powerFront = powerFront;
        this.powerRear = powerRear;
        this.brakePowerFront = brakePowerFront;
        this.breakPowerRear = breakPowerRear;

        joints.forEach(joint -> {
            pointMassSet.add(joint.getMass1());
            pointMassSet.add(joint.getMass2());
        });
    }

    public void accelerate(int level) {
        frontWheelForceApplied = powerFront * level;
        rearWheelForceApplied = powerRear * level;
    }

    public void brake(int level) {
        frontBrakeForceApplied = brakePowerFront * level;
        rearBrakeForceApplied = breakPowerRear * level;
    }

    public void steeringLeftToRight(int level) {

    }

    public List<Joint> getJoints() {
        return joints;
    }

}
