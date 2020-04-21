package org.jishnu.v2.nn;

import static org.junit.jupiter.api.Assertions.*;

class NeuralNetworkTest {

    @org.junit.jupiter.api.Test
    void getOutputIndex() {
        var network = new NeuralNetwork(3, 4, 2, 4);

        //Scenario 1: All values are below threshold of 0.5
        double[] testData1 = {0.1, 0.49, 0.3, 0.33};
        boolean[] expected1 = {false, false, false, false};
        assertArrayEquals(expected1, network.getOutputIndex(testData1));

        //Scenario 2: One value is above threshold of 0.5
        double[] testData2 = {0.1, 0.55, 0.48, 0.22};
        boolean[] expected2 = {false, true, false, false};
        assertArrayEquals(expected2, network.getOutputIndex(testData2));

        //Scenario 3: More than one values are above threshold of 0.5
        double[] testData3 = {0.1, 0.55, 0.58, 0.22};
        boolean[] expected3 = {false, false, true, false};
        assertArrayEquals(expected3, network.getOutputIndex(testData3));
    }
}