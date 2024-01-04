package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.swing.AbstractAction;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;

public abstract class LocalizableAction extends AbstractAction {
	
	protected ILocalizationProvider provider;
	private Map<String, String> actionKeysToTranslationKeysMap = new HashMap<>();
	private ILocalizationListener localizationListener = this::update;
	private boolean trackingLocalizationChanges = false;
	
	public LocalizableAction(ILocalizationProvider provider) {
		this.provider = Objects.requireNonNull(provider);
		this.trackLocalizationChanges(true);
	}
	
	public void trackLocalizationChanges(boolean track) {
		if (trackingLocalizationChanges == track)
			return;
		this.trackingLocalizationChanges = track;
		if (track)
			provider.addLocalizationListener(localizationListener);
		else
			provider.removeLocalizationListener(localizationListener);
	}
	
	private void update() {
		this.actionKeysToTranslationKeysMap.forEach((k, v) -> {
			String translation = provider.getString(v);
			this.putValue(k, translation);
		});
	}
	
	public void putLocalizedValue(String key, String translationKey) {
		Objects.requireNonNull(key);
		Objects.requireNonNull(translationKey);
		String translation = provider.getString(translationKey);
		this.putValue(key, translation);
		this.actionKeysToTranslationKeysMap.put(key, translationKey);
	}
	
}
