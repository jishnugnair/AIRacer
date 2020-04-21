package org.jishnu.v2.driver;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.cars.CarConstants;
import org.jishnu.v2.nn.NeuralNetwork;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Driver implements Runnable {
    private Car car;
    private NeuralNetwork neuralNetwork;
    private Logger logger = Logger.getLogger(Driver.class.getName());
    private CountDownLatch downLatch;

    public Driver(NeuralNetwork neuralNetwork, Car car, CountDownLatch downLatch) {
        this.car = car;
        this.neuralNetwork = neuralNetwork;
        this.downLatch = downLatch;
    }

    @Override
    public void run() {
        while (car.isRunning()) {
            boolean[] driveInputs = neuralNetwork.returnOutput(car.getStats());
            for (int i = 0; i < driveInputs.length; i++) {
                if (driveInputs[i]) {
                    CarConstants.carControls[i].control(car, 10);
                    break;
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(35);
            } catch (InterruptedException e) {
                logger.severe(e.getLocalizedMessage());
            }
        }
        downLatch.countDown();
    }
}
