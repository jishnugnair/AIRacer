package org.jishnu.v2.manager;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.nn.NeuralNetwork;

import java.util.Arrays;

/**
 *Sorts instances of <code>NeuralNetwork</code> based on performance of the associated <code>Car</code>
 */
public class NetworkSorter {

    private NetworkSorter() {
    }

    /**
     * Sorts instances of <code>NeuralNetwork</code> based on performance of the associated <code>Car</code>
     * and return the top N neural networks based on value of parameter <b>returnCount</b>.
     * @param networks array of all networks to be evaluated
     * @param cars array of all cars driven be networks
     * @param returnCount number of top neural networks to be returned out of entire set
     * @return returns the top <b>N</b> neural networks based on value of parameter <b>returnCount</b>
     */
    public static NeuralNetwork[] getBestNetworks(NeuralNetwork[] networks, Car[] cars, int returnCount) {
        var bestNetworks = new NeuralNetwork[returnCount];
        var sortables = new Sortable[networks.length];

        for (int i = 0; i < networks.length; i++)
            sortables[i] = new Sortable(networks[i], cars[i]);

        /*
         * Sorting neural networks based on performance.
         * A neural network is better if it's car travelled greater distance.
         * In case of tie in distance, the neural network with faster car is better.
         */
        Arrays.sort(sortables, (a, b) -> {
            if (a.getCar().getTravelDistance() > b.getCar().getTravelDistance())
                return 1;
            else if (a.getCar().getTravelDistance() == b.getCar().getTravelDistance())
                return Float.compare(a.getCar().getAverageSpeed(), b.getCar().getAverageSpeed());
            else
                return -1;
        });

        System.out.printf("Best distance: %f best speed %f & best network %d %n",
                sortables[returnCount * 2 - 1].getCar().getTravelDistance(),
                sortables[returnCount * 2 - 1].getCar().getAverageSpeed(),
                sortables[returnCount * 2 - 1].getNetwork().getNnId());

        for(int i = 0; i < returnCount; i++) {
            bestNetworks[i] = sortables[returnCount + i].getNetwork();
        }

        return bestNetworks;
    }

    /**
     * This class exists only to couple instances of related instances of <code>Car</code> and
     * <code>NeuralNetwork</code>. This coupling helps in sorting for best neural network based
     * on best performing car.
     */
    static class Sortable {
        NeuralNetwork network;
        Car car;

        public Sortable(NeuralNetwork network, Car car) {
            this.car = car;
            this.network = network;
        }

        public Car getCar() {
            return car;
        }

        public NeuralNetwork getNetwork() {
            return network;
        }
    }
}
