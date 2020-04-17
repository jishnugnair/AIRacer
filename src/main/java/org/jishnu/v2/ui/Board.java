package org.jishnu.v2.ui;

import org.jishnu.v2.cars.Car;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Board {

    private static BufferedImage board = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
    private static BufferedImage track = UIConstants.track;
    private static Graphics2D imageGraphics = board.createGraphics();
    private static Car[] cars;

    static {
        imageGraphics.setRenderingHints(
                new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
    }

    public static void setCars(Car[] cars) {
        Board.cars = cars;
    }

    public static void drawBoard(Graphics g) {
        long timestamp = System.currentTimeMillis();
        imageGraphics.drawImage(track, 0, 0, null);

        for (int i = 0; i < cars.length; i++) {
            imageGraphics.setColor(cars[i].getColor());
            imageGraphics.fillPolygon(cars[i].getPolygon(timestamp));
        }
        g.drawImage(board, 0, 22, null);
    }

}
