package org.jishnu.v1.ui;

import org.jishnu.v1.car.Car;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Board {

    private static BufferedImage board = new BufferedImage(600, 800, BufferedImage.TYPE_INT_RGB);
    private static Graphics2D gimg = board.createGraphics();
    private static Car car;

    static {
        gimg.setRenderingHints(
                new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
    }

    public static void setCar(Car car) {
        Board.car = car;
    }

    public static void drawBoard(Graphics g) {
        gimg.setColor(Color.WHITE);
        gimg.fillRect(0, 0, 600, 800);
        gimg.setColor(Color.BLACK);
        car.getJoints().forEach(joint -> gimg.drawLine(
                joint.getMass1().getX(),
                joint.getMass1().getY(),
                joint.getMass2().getX(),
                joint.getMass2().getY()
        ));
        g.drawImage(board, 0, 22, null);
    }

}
