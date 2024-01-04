package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentListener;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class SaveDocumentAction extends SaveAsDocumentAction {
	
	
	private SingleDocumentListener modListener = new SingleDocumentAdapter() {
		@Override
		public void documentModifyStatusUpdated(SingleDocumentModel model) {
			boolean mod = model != null && model.isModified();
			SaveDocumentAction.this.setEnabled(mod);
		}
	};

	public SaveDocumentAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		super(frame, provider, multiDocModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SingleDocumentModel currentDoc = multiDocModel.getCurrentDocument();
		if (currentDoc.getFilePath() == null) {
			super.actionPerformed(e);
			return;
		}
		
		try {
			multiDocModel.saveDocument(currentDoc, null);
		} catch (RuntimeException ex) {
			String errMsg = ex.getMessage();
			JOptionPane.showMessageDialog(
						frame,
						errMsg,
						provider.getString("err"),
						JOptionPane.ERROR_MESSAGE
					);
			return;
		}
	}
	
	private void docUpdate(SingleDocumentModel prev, SingleDocumentModel curr) {
		if (prev != null)
			prev.removeSingleDocumentListener(modListener);
		
		if (curr != null)
			curr.addSingleDocumentListener(modListener);
		
		modListener.documentModifyStatusUpdated(curr);
	} 

	@Override
	protected void initAction() {
		this.putLocalizedValue(Action.NAME, "ac_name_save");
		this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_save");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
		this.setEnabled(false);
		
		multiDocModel.addMultipleDocumentListener(new MultipleDocumentAdapter() {

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				docUpdate(previousModel, currentModel);
			}
			
		});
	}

}
