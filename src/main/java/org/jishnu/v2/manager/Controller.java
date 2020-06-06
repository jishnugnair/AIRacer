package org.jishnu.v2.manager;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.driver.Driver;
import org.jishnu.v2.io.NetIO;
import org.jishnu.v2.nn.NeuralNetwork;
import org.jishnu.v2.ui.Board;
import org.jishnu.v2.ui.Frame;
import org.jishnu.v2.ui.UIConstants;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * This class controls all events of this application.
 * Since only one instance is required the constructor has been set to <code>private</code>
 */
public class Controller {
    private static Logger logger = Logger.getLogger(Controller.class.getName());

    private Controller() {
    }

    /**
     * Creates instances of cars and neural networks and triggers the first epoch.
     */
    public static void start() {
        var carCount = Configs.CAR_COUNT;
        var cars = new Car[carCount];
        var networks = new NeuralNetwork[carCount];
        var random = new Random();
        var startTimestamp = System.currentTimeMillis();

        for (var i = 0; i < carCount; i++) {
            cars[i] = new Car(300, 780, 0,
                    new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)),
                    UIConstants.trackLayout, startTimestamp);

            networks[i] = new NeuralNetwork(5, 4, 1, 4);
        }

        new Frame(cars);

        runEpochs(networks, cars, 0);

    }

    /**
     * Executes one epoch and calls itself recursively to execute further epochs.
     * @param networks array of neural networks which will control the car's movements
     * @param cars array of all cars which will be driven by neural networks
     * @param epochCount number of epochs for which the process of evolution will be executed
     */
    public static void runEpochs(NeuralNetwork[] networks, Car[] cars, int epochCount) {

        var carCount = Configs.CAR_COUNT;
        var doneSignal = new CountDownLatch(carCount);
        var drivers = new Driver[carCount];
        var executor = Executors.newFixedThreadPool(carCount);

        logger.info("Epoch number: " + epochCount);
        for (int i = 0; i < carCount; i++) {
            drivers[i] = new Driver(networks[i], cars[i], doneSignal);
            executor.submit(drivers[i]);
        }

        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            logger.severe(e.getLocalizedMessage());
        }

        var halfCount = carCount / 2;
        var bestNetworks = NetworkSorter.getBestNetworks(networks, cars, halfCount);
        Board.drawNetwork(bestNetworks[halfCount - 1]);

        if (epochCount == Configs.EPOCH_COUNT) {
            for (int i = 0; i < halfCount; i++)
                NetIO.writeFile(bestNetworks[i], Configs.OUTPUT_PATH, i);

            executor.shutdown();
            return;
        }

        var nextGenNetworks = new NeuralNetwork[carCount];

        for (int i = 0; i < halfCount; i++) {
            nextGenNetworks[i] = bestNetworks[i];
            nextGenNetworks[i + halfCount] = bestNetworks[i].getMutatedNetwork();
        }

        for (int i = 0; i < carCount; i++) {
            cars[i].resetCar();
        }

        runEpochs(nextGenNetworks, cars, ++epochCount);
    }
}
