package org.jishnu.v2.ui;

import org.jishnu.v2.cars.Car;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

public class Frame extends JFrame {

    private static final long serialVersionUID = 1228193394106659788L;
    private Logger logger = Logger.getLogger(Frame.class.getName());

    public Frame(Car[] cars) {
        super();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(0, 0, 1920, 1080);
        setCars(cars);
        setVisible(true);
        addMouseListener(new UserInputHandler());
        new Thread(() -> {
            while(true) {
                this.repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    logger.severe(e.getLocalizedMessage());
                }
            }
        }).start();
    }

    public void setCars(Car[] cars) {
        Board.setCars(cars);
    }

    @Override
    public void paint(Graphics g) {
        Board.drawBoard(g);
    }

}
