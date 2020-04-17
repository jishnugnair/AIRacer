package org.jishnu.v1.ui;

import org.jishnu.v1.car.Car;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    private static final long serialVersionUID = 1228193394106659788L;

    public Frame(Car car) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setResizable(false);
        setBounds(100, 10, 600, 800);
        Board.setCar(car);
        setVisible(true);
        addMouseListener(new UserInputHandler());
    }

    public void paint(Graphics g) {
        Board.drawBoard(g);
    }

}
