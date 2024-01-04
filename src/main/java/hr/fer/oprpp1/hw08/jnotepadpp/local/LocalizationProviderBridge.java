package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.Objects;

public class LocalizationProviderBridge extends AbstractLocalizationProvider {
	
	private ILocalizationProvider provider;
	private ILocalizationListener singleListener = () -> {
		lastKnownLanguage = provider.getCurrentLanguage();
		fire();
	};
	private String lastKnownLanguage;
	private boolean connected;
	
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = Objects.requireNonNull(provider);
	}

	public void connect() {
		if (connected)
			return;
		connected = true;
		provider.addLocalizationListener(singleListener);
		//System.out.println("connected");
		String providerLang = provider.getCurrentLanguage();
		String oldLang = lastKnownLanguage;
		lastKnownLanguage = providerLang;
		if (!Objects.equals(providerLang, oldLang))
			fire();
	}
	
	public void disconnect() {
		if (!connected)
			return;
		connected = false;
		//System.out.println("disconnected");
		provider.removeLocalizationListener(singleListener);
	}

	@Override
	public String getString(String key) {
		return provider.getString(key);
	}

	@Override
	public String getCurrentLanguage() {
		return lastKnownLanguage;
	}
	
	
	
}
