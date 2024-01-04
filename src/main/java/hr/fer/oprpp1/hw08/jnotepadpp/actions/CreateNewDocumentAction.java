package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;

public class CreateNewDocumentAction extends JNotepadPPAction {

	public CreateNewDocumentAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		super(frame, provider, multiDocModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		multiDocModel.createNewDocument();
	}

	@Override
	protected void initAction() {
		this.putLocalizedValue(Action.NAME, "ac_name_create_new_doc");
		this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_create_new_doc");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control N"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_N);
	}
	
	
	
}
