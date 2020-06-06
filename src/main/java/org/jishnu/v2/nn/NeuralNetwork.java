package org.jishnu.v2.nn;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class NeuralNetwork {
    /** Three dimensional array for holding hidden and output layer's node weights.
       The dimensions are layer, node, inputNode
     */
    private double[][][] nodeWeights;
    private double[][] nodeInputs;
    private static final Activation activation = NNConstants.SIGMOID;
    private int totalLayers;
    private int inputCount;
    private int outputCount;
    private int nodeCount;
    private int hiddenLayers;
    private int nnId;
    /**
     * A counter to uniquely identify a Neural Network variation
     */
    private static AtomicInteger nnIdCounter = new AtomicInteger(0);
    private static final float threshold = NNConstants.threshold;
    private Random random = new Random(System.currentTimeMillis() + nnIdCounter.get());

    /**
     * Takes node weights of an existing neural network, mutates it to create an new <code>NeuralNetwork</code>
     * @param inputCount takes the number of input nodes for neural network
     * @param outputCount takes the number of output nodes for the neural network
     * @param hiddenLayers takes the number of hidden layers for the neural network
     * @param nodeCount takes the number of nodes in each hidden layer
     * @param baseNodeWeights node weights of the parent neural network
     */
    private NeuralNetwork(int inputCount, int outputCount, int hiddenLayers, int nodeCount, double[][][] baseNodeWeights) {
        if (hiddenLayers < 1 || nodeCount < 1)
            throw new IllegalArgumentException("At least 1 hidden Layer and nodeCount is needed");
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.nodeCount = nodeCount;
        this.hiddenLayers = hiddenLayers;
        int divisor = 5;

        nnId = nnIdCounter.addAndGet(1);
        totalLayers = hiddenLayers + 1;
        nodeInputs = new double[totalLayers][];
        nodeWeights = new double[totalLayers][][];

        //Initializing all hidden nodes with random weights
        for (int layer = 0; layer < nodeWeights.length; layer++) {
            if (layer < hiddenLayers) {
                nodeWeights[layer] = new double[nodeCount][];
                nodeInputs[layer] = new double[nodeCount];
            } else {
                //Initializing output layer node weights
                nodeWeights[layer] = new double[outputCount][];
                nodeInputs[layer] = new double[outputCount];
            }

            for (int node = 0; node < nodeWeights[layer].length; node++) {
                if (layer > 0) {
                    nodeWeights[layer][node] = new double[nodeCount];
                } else {
                    //Initializing first hidden layer input weights
                    nodeWeights[layer][node] = new double[inputCount];
                }

                for (int inputNodeCount = 0; inputNodeCount < nodeWeights[layer][node].length; inputNodeCount++) {
                    nodeWeights[layer][node][inputNodeCount] = baseNodeWeights[layer][node][inputNodeCount] + (random.nextBoolean() ? -1 : 1) * random.nextDouble() / divisor;
                }
            }
        }
    }

    /**
     * Initializes a new instance of neural network with specified input nodes, output nodes,
     * hidden layers and number of nodes in each hidden layer. Currently hidden layers with
     * different node counts is not supported.
     * @param inputCount takes the number of input nodes for neural network
     * @param outputCount takes the number of output nodes for the neural network
     * @param hiddenLayers takes the number of hidden layers for the neural network
     * @param nodeCount takes the number of nodes in each hidden layer
     */
    public NeuralNetwork(int inputCount, int outputCount, int hiddenLayers, int nodeCount) {
        if (hiddenLayers < 1 || nodeCount < 1)
            throw new IllegalArgumentException("At least 1 hidden Layer and nodeCount is needed");
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.nodeCount = nodeCount;
        this.hiddenLayers = hiddenLayers;
        int divisor = 1;

        nnId = nnIdCounter.addAndGet(1);
        totalLayers = hiddenLayers + 1;
        nodeInputs = new double[totalLayers][];
        nodeWeights = new double[totalLayers][][];

        //Initializing all hidden nodes with random weights
        for (int layer = 0; layer < nodeWeights.length; layer++) {
            if (layer < hiddenLayers) {
                nodeWeights[layer] = new double[nodeCount][];
                nodeInputs[layer] = new double[nodeCount];
            } else {
                //Initializing output layer node weights
                nodeWeights[layer] = new double[outputCount][];
                nodeInputs[layer] = new double[outputCount];
            }

            for (int node = 0; node < nodeWeights[layer].length; node++) {
                if (layer > 0) {
                    nodeWeights[layer][node] = new double[nodeCount];
                } else {
                    //Initializing first hidden layer input weights
                    nodeWeights[layer][node] = new double[inputCount];
                }

                for (int inputNodeCount = 0; inputNodeCount < nodeWeights[layer][node].length; inputNodeCount++) {
                    nodeWeights[layer][node][inputNodeCount] = (random.nextBoolean() ? -1 : 1) * random.nextDouble() / divisor;
                }
            }
        }
    }

    /**
     *
     * @param inputValues array of looking distances in all direction
     * @return boolean array with only one <b>true</b> value indicating output of neural network
     */
    public boolean[] returnOutput(double[] inputValues) {
        for (int layer = 0; layer < nodeWeights.length; layer++) {
            double[] previousNode;
            if (layer > 0) {
                previousNode = nodeInputs[layer - 1];
            } else {
                previousNode = inputValues;
            }
            for (int node = 0; node < nodeWeights[layer].length; node++) {
                double sum = 0;
                for (int inputNodeCount = 0; inputNodeCount < previousNode.length; inputNodeCount++) {
                    sum += previousNode[inputNodeCount] * nodeWeights[layer][node][inputNodeCount];
                }
                nodeInputs[layer][node] = activation.getOutput(sum);
            }
        }
        return getOutputIndex(nodeInputs[totalLayers - 1]);
    }

    /**
     * Mutates an existing network randomly
     * @return new instance of mutated copy of an existing instance
     */
    public NeuralNetwork getMutatedNetwork() {
        return new NeuralNetwork(inputCount, outputCount, hiddenLayers, nodeCount, nodeWeights);
    }

    protected boolean[] getOutputIndex(double[] outputNodes) {
        double max = Double.MIN_VALUE;
        boolean[] output = new boolean[outputCount];
        int currentMaxIndex = 0;
        for (int i = 0; i < outputNodes.length; i++) {
            if (outputNodes[i] > threshold && outputNodes[i] > max) {
                max = outputNodes[i];
                output[currentMaxIndex] = false;
                output[i] = true;
                currentMaxIndex = i;
            }
        }
        return output;
    }

    public double[][][] getNodeWeights() {return nodeWeights;}

    public int getNnId() {
        return nnId;
    }

    public int getInputCount() {
        return inputCount;
    }

    public int getOutputCount() {
        return outputCount;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getHiddenLayers() {
        return hiddenLayers;
    }

    public String toString() {
        return inputCount +
                Integer.toString(outputCount) +
                hiddenLayers +
                nodeCount +
                nnId;
    }
}
