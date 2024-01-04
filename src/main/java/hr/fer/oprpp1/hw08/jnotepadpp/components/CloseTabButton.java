package hr.fer.oprpp1.hw08.jnotepadpp.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;

public class CloseTabButton extends JButton {
	
	private static final Dimension PREF_SIZE = new Dimension(13, 13);
	private static final Color BG = new Color(255, 157, 150);
	private final MouseListener mouseListener = new MouseAdapter() {
		CloseTabButton ctb = CloseTabButton.this;
		@Override
		public void mouseEntered(MouseEvent e) {
			ctb.setOpaque(true);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			ctb.setOpaque(false);
		}
	};
	
	public CloseTabButton(Action ac) {
		super(ac);
		initComponent();
	}
	
	private void initComponent() {
		this.setText("X");
		this.setPreferredSize(PREF_SIZE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.setBorderPainted(true);
		this.setFocusable(false);
		this.setOpaque(false);
		this.setBackground(BG);
		this.addMouseListener(mouseListener);
	}
	
}
