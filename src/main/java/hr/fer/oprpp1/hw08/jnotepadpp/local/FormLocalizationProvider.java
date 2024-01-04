package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Objects;

import javax.swing.JFrame;

public class FormLocalizationProvider extends LocalizationProviderBridge {
	
	public FormLocalizationProvider(ILocalizationProvider provider, JFrame frame) {
		super(provider);
		Objects.requireNonNull(frame);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				connect();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				disconnect();
			}
		});
	}
	
	
}
