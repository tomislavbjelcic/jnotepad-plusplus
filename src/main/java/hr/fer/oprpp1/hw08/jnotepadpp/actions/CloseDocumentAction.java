package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class CloseDocumentAction extends CloseTabAction {

	public CloseDocumentAction(JNotepadPP frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		super(frame, provider, multiDocModel, null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		this.associatedSingleDocModel = multiDocModel.getCurrentDocument();
		actionPerformed(e, true);
	}

	@Override
	public void initAction() {
		this.putLocalizedValue(Action.NAME, "ac_name_close");
		this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_close");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control W"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_W);
		this.setEnabled(false);
		
		multiDocModel.addMultipleDocumentListener(new MultipleDocumentAdapter() {

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				CloseDocumentAction.this.setEnabled(currentModel != null);
			}
			
		});
	}

}
