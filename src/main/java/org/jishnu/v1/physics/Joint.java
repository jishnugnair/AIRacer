package org.jishnu.v1.physics;

public class Joint {

    private PointMass mass1, mass2;
    private float force1, force2;

    public Joint(PointMass mass1, PointMass mass2) {
        this.mass1 = mass1;
        this.mass2 = mass2;
    }

    public PointMass getMass1() {return mass1;}

    public PointMass getMass2() {return mass2;}
}
