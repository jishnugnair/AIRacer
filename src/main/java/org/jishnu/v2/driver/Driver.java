package org.jishnu.v2.driver;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.cars.CarConstants;
import org.jishnu.v2.nn.NeuralNetwork;

public class Driver {
    private Car car;
    private NeuralNetwork neuralNetwork;

    public Driver(Car car, NeuralNetwork neuralNetwork) {
        this.car = car;
        this.neuralNetwork = neuralNetwork;
    }

    public void drive() {
        boolean[] driveInput = neuralNetwork.returnOutput(car.getStats());
        for(int i = 0; i < driveInput.length; i++) {
            if(driveInput[i]) {
                CarConstants.carControls[i].control(car, 10);
                break;
           }
        }
    }
}
