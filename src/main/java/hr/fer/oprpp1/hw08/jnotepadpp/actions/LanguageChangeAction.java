package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.Action;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.local.LocalizationProvider;

public class LanguageChangeAction extends LocalizableAction {

	private LocalizationProvider changer;
	private String lang;

	public LanguageChangeAction(ILocalizationProvider provider, LocalizationProvider changer, String lang) {
		super(provider);
		this.changer = Objects.requireNonNull(changer);
		this.lang = Objects.requireNonNull(lang);
		this.putLocalizedValue(Action.NAME, lang);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		changer.setLanguage(lang);
	}
	

}
