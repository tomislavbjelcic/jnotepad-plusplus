package hr.fer.oprpp1.hw08.jnotepadpp.components;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JLabel;
import javax.swing.Timer;

public class CurrentTimeJLabel extends JLabel{
	
	private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	private Timer timer;
	
	public CurrentTimeJLabel(int delay) {
		timer = new Timer(delay, e -> {
			String time = dtf.format(LocalDateTime.now());
			this.setText(time);
		});
	}
	
	public void stopTimer() {
		timer.stop();
	}
	
	public void startTimer() {
		timer.start();
	}

}
