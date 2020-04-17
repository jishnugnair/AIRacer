package org.jishnu.v2.ui;

import org.jishnu.v2.cars.Car;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private static final long serialVersionUID = 1228193394106659788L;

    public Frame(Car[] car) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);
        setBounds(0, 0, 1920, 1080);
        Board.setCars(car);
        setVisible(true);
        addMouseListener(new UserInputHandler());
        new Thread(() -> {
            while(true) {
                this.repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void paint(Graphics g) {
        Board.drawBoard(g);
    }

}
