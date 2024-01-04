package hr.fer.oprpp1.hw08.jnotepadpp.local;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider {
	
	private List<ILocalizationListener> listeners = new LinkedList<>();
	
	@Override
	public void addLocalizationListener(ILocalizationListener listener) {
		if (listener != null)
			listeners.add(listener);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener listener) {
		if (listener == null)
			return;
		listeners.remove(listener);
	}

	protected void fire() {
		listeners.forEach(ILocalizationListener::localizationChanged);
	}

}
