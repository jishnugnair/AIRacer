package org.jishnu.v2.cars;

public class CarBuilder {
    private int length;
    private int breadth;
    private int mass;
    private float dragCoefficient;
    private float rollingFriction;
    private int power;

    public CarBuilder setPower(int power) {
        this.power = power;
        return this;
    }

    public CarBuilder setLength(int length) {
        this.length = length;
        return this;
    }

    public CarBuilder setBreadth(int breadth) {
        this.breadth = breadth;
        return this;
    }

    public CarBuilder setMass(int mass) {
        this.mass = mass;
        return this;
    }

    public CarBuilder setDragCoefficient(float dragCoefficient) {
        this.dragCoefficient = dragCoefficient;
        return this;
    }

    public CarBuilder setRollingFriction(float rollingFriction) {
        this.rollingFriction = rollingFriction;
        return this;
    }

    public Car build() {
        //return new Car();
        return null;
    }

}
