package org.jishnu.v2.ui;

import org.jishnu.v2.cars.Car;
import org.jishnu.v2.nn.NeuralNetwork;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Board {

    private static BufferedImage board = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage track = UIConfigs.track;
    private static Graphics2D imageGraphics = board.createGraphics();
    private static Car[] cars;
    private static NeuralNetwork network;
    private static boolean netWorkDrawn = false;

    static {
        imageGraphics.setRenderingHints(
                new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
    }

    public static void setCars(Car[] cars) {
        Board.cars = cars;
    }

    public static void drawBoard(Graphics g) {
        imageGraphics.drawImage(track, 0, 0, null);

        for (int i = 0; i < cars.length; i++) {
            imageGraphics.setColor(cars[i].getColor());
            imageGraphics.fillPolygon(cars[i].getPolygon());
            Line2D[] lines = cars[i].getLineOfSight();
            imageGraphics.setColor(Color.BLACK);
            for(int line = 0; line < lines.length; line++)
                imageGraphics.draw(lines[line]);
            if (netWorkDrawn)
                drawNN();
        }
        g.drawImage(board, 0, 22, null);
    }

    public static void drawNetwork(NeuralNetwork network) {
        Board.network = network;
        netWorkDrawn = true;
    }

    private static void drawNN() {
        int gap = 90;
        int diameter = 20;
        int cornerClearance = 30;
        int linkOffset = cornerClearance + diameter / 2;
        int minWidth = 1;
        int layers = network.getHiddenLayers() + 2;
        int[] allNodes = new int[layers];
        allNodes[0] = network.getInputCount();
        double[][][] nodeWeights = network.getNodeWeights();
        for (int x = 0; x < layers; x++) {
            if (x > 0)
                allNodes[x] = nodeWeights[x - 1].length;
            for (int y = 0; y < allNodes[x]; y++) {
                imageGraphics.setColor(Color.BLUE);
                imageGraphics.fillOval(x * gap + cornerClearance, y * gap + cornerClearance, diameter, diameter);
                if (x > 0)
                    for (int nodeInputs = 0; nodeInputs < nodeWeights[x - 1][y].length; nodeInputs++) {
                        double nodeWeight = nodeWeights[x - 1][y][nodeInputs];
                        imageGraphics.setColor(nodeWeight < 0 ? Color.RED : Color.ORANGE);
                        int[] xCoordinates = {x * gap + linkOffset, x * gap + linkOffset,
                                (x - 1) * gap + linkOffset, (x - 1) * gap + linkOffset};

                        int[] yCoordinates = {y * gap + linkOffset, y * gap + linkOffset + minWidth + 2 * (int) Math.round(Math.abs(nodeWeight)),
                                nodeInputs * gap + linkOffset + minWidth + 2 * (int) Math.round(Math.abs(nodeWeight)), nodeInputs * gap + linkOffset};
                        imageGraphics.fillPolygon(xCoordinates, yCoordinates, 4);

                        //Drawing weight amount
                        /*int xPos = (xCoordinates[0] + xCoordinates[2]) / 3 * 2 - 10;
                        int yPos = (yCoordinates[0] + yCoordinates[2]) / 3 * 2;
                        imageGraphics.setColor(Color.BLACK);
                        imageGraphics.drawString(Double.toString(nodeWeight).substring(0, 4), xPos, yPos);*/
                    }
            }
        }
    }

}
