package org.jishnu.v2.manager;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.driver.Driver;
import org.jishnu.v2.io.NetIO;
import org.jishnu.v2.nn.NeuralNetwork;
import org.jishnu.v2.ui.Frame;
import org.jishnu.v2.ui.UIConstants;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Controller {
    private static Logger logger = Logger.getLogger(Controller.class.getName());
    private Controller() {}

    public static void start() {
        var carCount = Configs.CAR_COUNT;
        var cars = new Car[carCount];
        var networks = new NeuralNetwork[carCount];
        var random = new Random();
        var startTimestamp = System.currentTimeMillis();


        for (var i = 0; i < carCount; i++) {
            cars[i] = new Car(470, 800, 0,
                    new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)),
                    UIConstants.trackLayout, startTimestamp);

            networks[i] = new NeuralNetwork(3, 4, 1, 4);
        }

        new Frame(cars);

        runEpochs(networks, cars, 0);

    }

    public static void runEpochs(NeuralNetwork[] networks, Car[] cars, int epochCount) {

        var carCount = Configs.CAR_COUNT;
        var doneSignal = new CountDownLatch(carCount);
        var sorter = new ScoreSorter(networks, cars);
        var drivers = new Driver[carCount];
        var executor = Executors.newFixedThreadPool(carCount);

        logger.info("Epoch number: " + epochCount);
        for (int i = 0; i < carCount; i++) {
            drivers[i] = new Driver(networks[i], cars[i], doneSignal);
            executor.submit(drivers[i]);
            //System.out.println("Network id: " + networks[i].getNnId());
        }

        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            logger.severe(e.getLocalizedMessage());
        }

        var halfCount = carCount / 2;
        var bestNetworks = sorter.getBestNetworks(halfCount);

        if (epochCount == Configs.EPOCH_COUNT) {
            for(int i = 0; i < halfCount; i++)
                NetIO.writeFile(bestNetworks[i], Configs.OUTPUT_PATH, i);

            executor.shutdown();
            return;
        }

        var nextGenNetworks = new NeuralNetwork[carCount];
        var timestamp = System.currentTimeMillis();

        for (int i = 0; i < halfCount; i ++) {
            nextGenNetworks[i] = bestNetworks[i];
            nextGenNetworks[i + halfCount] = bestNetworks[i].getMutatedNetwork();
        }

        //System.out.println("All car distances");
        for (int i = 0; i < carCount; i++) {
            //System.out.println(cars[i].getTravelDistance());
            cars[i].resetCar(timestamp);
        }

        runEpochs(nextGenNetworks, cars, ++epochCount);
    }
}
