package hr.fer.oprpp1.hw08.jnotepadpp.components;

import java.util.Objects;

import javax.swing.JLabel;

public class CounterJLabel extends JLabel {
	
	private String leftText;
	private int count = 0;
	
	public CounterJLabel(String leftText) {
		this.leftText = Objects.requireNonNull(leftText);
		update();
	}
	
	private void update() {
		String label = leftText + " : " + count;
		this.setText(label);
	}
	
	public void setCount(int count) {
		this.count = count;
		update();
	}
	
	public void setLeftText(String leftText) {
		this.leftText = Objects.requireNonNull(leftText);
		update();
	}

}
