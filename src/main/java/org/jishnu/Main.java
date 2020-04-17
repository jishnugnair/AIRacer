package org.jishnu;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.driver.Driver;
import org.jishnu.v2.nn.NeuralNetwork;
import org.jishnu.v2.ui.UIConstants;
import org.jishnu.v2.ui.Frame;

import java.awt.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        var carCount = 1000;
        var cars = new Car[carCount];
        var networks = new NeuralNetwork[carCount];
        var drivers = new Driver[carCount];
        var random = new Random();
        var startTimestamp = System.currentTimeMillis();

        for (var i = 0; i < cars.length; i++) {
            cars[i] = new Car(500,800,0,
                    new Color(random.nextInt(256), random.nextInt(256),
                            random.nextInt(256)), UIConstants.trackLayout, startTimestamp);
            networks[i] = new NeuralNetwork(4,4,1,10);
            drivers[i] = new Driver(cars[i], networks[i]);
        }

        new Frame(cars);

        while (true) {
            for (int i = 0; i < carCount; i++) {
                    drivers[i].drive();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
