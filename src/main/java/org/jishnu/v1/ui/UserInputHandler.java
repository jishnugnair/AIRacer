package org.jishnu.v1.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UserInputHandler implements MouseListener {

	private final float cellSize = 620f / 15f;
	private static Frame frame = null;
	private short dice = 0;

	public static void setFrame(Frame f) {
		frame = f;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		int x = e.getX();
		int y = e.getY();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
