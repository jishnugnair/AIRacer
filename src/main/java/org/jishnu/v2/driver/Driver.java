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
    private CountDownLatch countDownLatch;

    /**
     * Instantiates <code>Driver</code> class which will drive one car using one neural network
     * @param neuralNetwork instance of <code>NeuralNetwork</code> which will drive the car
     * @param car instance of <code>Car</code> which will be driven
     * @param countDownLatch to track individual <code>Driver</code> thread
     */
    public Driver(NeuralNetwork neuralNetwork, Car car, CountDownLatch countDownLatch) {
        this.car = car;
        this.neuralNetwork = neuralNetwork;
        this.countDownLatch = countDownLatch;
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
        countDownLatch.countDown();
    }
}
