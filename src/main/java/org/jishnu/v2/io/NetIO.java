package org.jishnu.v2.io;

import netscape.javascript.JSObject;
import org.jishnu.v2.nn.NeuralNetwork;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Logger;


public class NetIO {
    private static Logger logger = Logger.getLogger(NetIO.class.getName());

    /**
     * Writes the instance of <code>NeuralNetwork</code> to file
     * @param network instance of a <code>NeuralNetwork</code>
     * @param outPath path to file where the neural network is to be written
     * @param index unique index of the network
     */
    public static void writeFile(NeuralNetwork network, String outPath, int index) {
        var json = createJson(network);
        try(var writer = new BufferedWriter(new FileWriter(outPath + network.toString() + index))) {
            writer.write(json);
        } catch (IOException e) {
            logger.severe(e.getLocalizedMessage());
        }
    }

    /**
     * Creates a JSON representation of <code>NeuralNetwork</code> instance.
     * @param network which needs to represented as JSON
     * @return JSON string with state of the network
     */
    private static String createJson(NeuralNetwork network) {
        JSONObject base = new JSONObject();
        base.append("Input-Count", network.getInputCount())
        .append("Output-Count", network.getOutputCount())
        .append("Hidden-Layers", network.getHiddenLayers())
        .append("Node-Count", network.getNodeCount());

        double[][][] nodeWeights = network.getNodeWeights();

        JSONArray layers = new JSONArray();
        base.append("Input-Weights", layers);

        for (int layer = 0; layer < nodeWeights.length; layer++) {
            JSONArray nodes = new JSONArray();
            layers.put(nodes);
            for (int node = 0; node < nodeWeights[layer].length; node++) {
                JSONArray inputWeights = new JSONArray();
                nodes.put(inputWeights);
                for (int inputNodeCount = 0; inputNodeCount < nodeWeights[layer][node].length; inputNodeCount++) {
                    nodes.put(nodeWeights[layer][node][inputNodeCount]);
                }
            }
        }

        return base.toString();
    }

}
