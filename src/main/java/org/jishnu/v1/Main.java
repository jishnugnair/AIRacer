package org.jishnu.v1;

import org.jishnu.v1.car.CarBuilder;
import org.jishnu.v1.car.Wheel;
import org.jishnu.v1.physics.Direction;
import org.jishnu.v1.physics.Joint;
import org.jishnu.v1.physics.PointMass;
import org.jishnu.v1.ui.Frame;

public class Main {
    public static void main(String[] args) {

        var mass1 = new PointMass(100, 100, 200);
        var mass2 = new PointMass(400, 100, 200);
        var mass3 = new PointMass(400, 700, 200);
        var mass4 = new PointMass(100, 700, 200);
        var mass5 = new PointMass(250, 400, 200);

        var joint1 = new Joint(mass1, mass2);
        var joint2 = new Joint(mass2, mass3);
        var joint3 = new Joint(mass3, mass4);
        var joint4 = new Joint(mass4, mass1);
        var joint5 = new Joint(mass1, mass5);
        var joint6 = new Joint(mass2, mass5);
        var joint7 = new Joint(mass3, mass5);
        var joint8 = new Joint(mass4, mass5);

        var direction = new Direction(0, -1, 0);
        var frontLeftWheel = new Wheel(mass1, direction);
        var frontRightWheel = new Wheel(mass2, direction);
        var rearLeftWheel = new Wheel(mass4, direction);
        var rearRightWheel = new Wheel(mass3, direction);

        var car = new CarBuilder()
                .addJoint(joint1)
                .addJoint(joint2)
                .addJoint(joint3)
                .addJoint(joint4)
                .addJoint(joint5)
                .addJoint(joint6)
                .addJoint(joint7)
                .addJoint(joint8)
                .setWheels(frontLeftWheel, frontRightWheel, rearLeftWheel, rearRightWheel)
                .setFrontBackPower(100, 200)
                .setFrontBackBrakePower(150, 50)
                .build();

        new Frame(car);
    }
}
