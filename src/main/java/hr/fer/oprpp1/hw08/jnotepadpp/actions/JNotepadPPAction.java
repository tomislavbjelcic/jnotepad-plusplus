package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.util.Objects;

import javax.swing.JFrame;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;

public abstract class JNotepadPPAction extends LocalizableAction {
	
	protected MultipleDocumentModel multiDocModel;
	protected JFrame frame; // za prikazivanje dijaloga
	
	public JNotepadPPAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		super(provider);
		this.multiDocModel = Objects.requireNonNull(multiDocModel);
		this.frame = frame;
		initAction();
	}
	
	protected abstract void initAction();
}
