package hr.fer.oprpp1.hw08.jnotepadpp.components;

import java.util.Objects;

import javax.swing.JToolBar;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

public class LocalizableJToolBar extends JToolBar {
	
	private String titleKey;
	private ILocalizationProvider provider;
	private ILocalizationListener listener = () -> {
		String newName = provider.getString(titleKey);
		setName(newName);
	};
	
	public LocalizableJToolBar(ILocalizationProvider provider, String titleKey) {
		this.provider = Objects.requireNonNull(provider);
		this.titleKey = Objects.requireNonNull(titleKey);
		
		provider.addLocalizationListener(listener);
		listener.localizationChanged();
	}
	
	

}
