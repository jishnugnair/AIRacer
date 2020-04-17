package org.jishnu.v2.nn;

import java.util.Random;

public class NeuralNetwork {
    /* Three dimensional array for holding hidden and output layer's node weights.
       The dimensions are layer, node, inputNode
     */
    private double[][][] nodeWeights;
    private double[][] nodeInputs;
    private Random random = new Random();
    private final Activation activation = NNConstants.SIGMOID;
    private int totalLayers;
    private int inputCount, outputCount, nodeCount;
    private int hiddenLayers;
    public static enum activation {SIGMOID, RELU};

    private NeuralNetwork(int inputCount, int outputCount, int hiddenLayers, int nodeCount, double[][][] nodeWeights) {
        if(hiddenLayers < 1 || nodeCount < 1)
            throw new IllegalArgumentException("At least 1 hidden Layer and nodeCount is needed");
        this.inputCount = inputCount;
        this.outputCount = outputCount;
        this.nodeCount = nodeCount;
        this.hiddenLayers = hiddenLayers;
        int divisor = 0;

        if(nodeWeights == null) {
            divisor = 100;
            nodeWeights = new double[totalLayers][][];
        } else
            divisor = 1000;

        totalLayers = hiddenLayers + 1;
        nodeInputs = new double[totalLayers][];

        //Initializing all hidden nodes with random weights
        for(int layer = 0; layer < nodeWeights.length; layer++) {
            if(layer < hiddenLayers) {
                nodeWeights[layer] = new double[nodeCount][];
                nodeInputs[layer] = new double[nodeCount];
            } else {
                //Initializing output layer node weights
                nodeWeights[layer] = new double[outputCount][];
                nodeInputs[layer] = new double[outputCount];
            }

            for(int node = 0; node < nodeWeights[layer].length; node++) {
                if(layer > 0) {
                    nodeWeights[layer][node] = new double[nodeCount];
                } else {
                    //Initializing first hidden layer input weights
                    nodeWeights[layer][node] = new double[inputCount];
                }

                for(int inputNodeCount = 0; inputNodeCount < nodeWeights[layer][node].length; inputNodeCount++) {
                    nodeWeights[layer][node][inputNodeCount] += (random.nextBoolean()? -1: 1) * random.nextDouble() / divisor;
                }
            }
        }
    }

    public NeuralNetwork(int inputCount, int outputCount, int hiddenLayers, int nodeCount) {
        this(inputCount, outputCount, hiddenLayers, nodeCount, null);
    }

    public boolean[] returnOutput(double[] inputValues) {
        for(int layer = 0; layer < nodeWeights.length; layer++) {
            double[] previousNode = null;
            if(layer > 0) {
                previousNode = nodeInputs[layer - 1];
            } else {
                previousNode = inputValues;
            }
            for(int node = 0; node < nodeWeights[layer].length; node++) {
                double sum = 0;
                for(int inputNodeCount = 0; inputNodeCount < previousNode.length; inputNodeCount++) {
                    sum += previousNode[inputNodeCount] * nodeWeights[layer][node][inputNodeCount];
                }
                nodeInputs[layer][node] = activation.getOutput(sum);
            }
        }
        return getBooleanOutput(nodeInputs[totalLayers - 1]);
    }

    public NeuralNetwork mutate() {
        return new NeuralNetwork(inputCount, outputCount, hiddenLayers, nodeCount, nodeWeights);
    }

    private boolean[] getBooleanOutput(double[] outputNodes) {
        double max = Double.MIN_VALUE;
        boolean[] output = new boolean[outputNodes.length];
        for(int i = 0; i < outputNodes.length; i++) {
            if(outputNodes[i] > max)
                max = outputNodes[i];
        }
        for(int i = 0; i < outputNodes.length; i++) {
            if(outputNodes[i] == max) {
                output[i] = true;
                break;
            }
        }
        return output;
    }
}
