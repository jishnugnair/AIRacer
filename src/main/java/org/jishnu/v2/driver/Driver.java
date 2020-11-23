package org.jishnu.v2.driver;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.cars.CarConfigs;
import org.jishnu.v2.nn.NeuralNetwork;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * This class connects the driver <code>NeuralNetwork</code> to the driven <code>Car</code>
 */
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

    /**
     * Runs a single thread of driver <code>NeuralNetwork</code> and <code>Car</code>
     */
    @Override
    public void run() {
        while (car.isRunning()) {
            boolean[] driveInputs = neuralNetwork.returnOutput(car.getStats());
            for (int i = 0; i < driveInputs.length; i++) {
                if (driveInputs[i]) {
                    CarConfigs.carControls[i].control(car, 10);
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
