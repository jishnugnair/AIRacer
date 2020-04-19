package org.jishnu.v2.manager;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.nn.NeuralNetwork;

import java.util.Arrays;

public class ScoreSorter {
    Sortable[] sortables;
    NeuralNetwork[] networks;

    public ScoreSorter(NeuralNetwork[] networks, Car[] cars) {
        this.networks = networks;
        sortables = new Sortable[networks.length];

        for (int i = 0; i < networks.length; i++)
            sortables[i] = new Sortable(networks[i], cars[i]);
    }

    public NeuralNetwork[] getBestNetworks(int returnCount) {
        var bestNetworks = new NeuralNetwork[returnCount];
        Arrays.sort(sortables, (a, b) -> {
            if (a.getCar().getTravelDistance() > b.getCar().getTravelDistance())
                return 1;
            else if (a.getCar().getTravelDistance() == b.getCar().getTravelDistance())
                return Float.compare(a.getCar().getAverageSpeed(), b.getCar().getAverageSpeed());
            else
                return -1;
        });

        System.out.printf("Best distance: %f & best network %d \n",
                sortables[returnCount * 2 - 1].getCar().getTravelDistance(),
                sortables[returnCount * 2 - 1].getNetwork().getNnId());
        for(int i = 0; i < returnCount; i++) {
            bestNetworks[i] = sortables[returnCount + i].getNetwork();
            //System.out.println(sortables[returnCount + i].getCar().getTravelDistance());
        }

        return bestNetworks;
    }

    class Sortable {
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
