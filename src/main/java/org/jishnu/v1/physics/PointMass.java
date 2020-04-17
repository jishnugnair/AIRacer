package org.jishnu.v1.physics;

import java.util.ArrayList;
import java.util.List;

public class PointMass {
    private int x;
    private int y;
    private int mass;
    List<Joint> joints = new ArrayList<>();

    public PointMass(int x, int y, int mass) {
        this.x = x;
        this.y = y;
        this.mass = mass;
    }

    public void addJoint(Joint joint) {
        joints.add(joint);
    }

    public int getMass() {
        return mass;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
