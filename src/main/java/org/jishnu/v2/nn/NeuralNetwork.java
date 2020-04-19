package org.jishnu.v2.nn;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NeuralNetwork {
    /* Three dimensional array for holding hidden and output layer's node weights.
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
    private static AtomicInteger nnIdCounter = new AtomicInteger(0);
    private Random random = new Random(System.currentTimeMillis() + nnIdCounter.get());

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
                    /* if(layer == 0)
                        System.out.println(nodeWeights[layer][node][inputNodeCount]);
                    */
                }
            }
        }
    }

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

    public int returnOutput(double[] inputValues) {
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

    public NeuralNetwork getMutatedNetwork() {
        return new NeuralNetwork(inputCount, outputCount, hiddenLayers, nodeCount, nodeWeights);
    }

    private int getOutputIndex(double[] outputNodes) {
        double max = Double.MIN_VALUE;
        int outputIndex = 0;
        int oneCount = 0;
        for (int i = 0; i < outputNodes.length; i++) {
            if(outputNodes[i] == 1)
                oneCount++;
            if (outputNodes[i] > max) {
                max = outputNodes[i];
                outputIndex = i;
            }
        }
        if(oneCount == 4)
            System.out.println("All outputs are one");

        return outputIndex;
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
