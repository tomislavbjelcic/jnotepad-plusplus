package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.util.Objects;

import javax.swing.Action;
import javax.swing.JOptionPane;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class CloseTabAction extends JNotepadPPAction {
	
	protected SingleDocumentModel associatedSingleDocModel;
	
	private JNotepadPP frame;
	
	public CloseTabAction(JNotepadPP frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel, 
			SingleDocumentModel associatedSingleDocModel) {
		super(frame, provider, multiDocModel);
		this.frame = Objects.requireNonNull(frame);
		this.associatedSingleDocModel = associatedSingleDocModel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		actionPerformed(e, false);
	}
	
	protected void actionPerformed(ActionEvent e, boolean permanent) {
		boolean saved = !associatedSingleDocModel.isModified();
		if (!saved) {
			for (int i=0, cnt=multiDocModel.getNumberOfDocuments(); i<cnt; i++) {
				var d = multiDocModel.getDocument(i);
				if (d == associatedSingleDocModel) {
					frame.getJTabbedPane().setSelectedIndex(i);
					break;
				}
			}
			String fileName = associatedSingleDocModel.getFileName();
			String msg = String.format("%s \"%s\" %s.\n%s?",
					provider.getString("prompt_save_part1"),
					fileName,
					provider.getString("prompt_save_part2"),
					provider.getString("prompt_save_part3")
					);
			Object[] options = new Object[] {
					provider.getString("prompt_save_yes"),
					provider.getString("prompt_save_no"),
					provider.getString("cancel")
			};
			int d = JOptionPane.showOptionDialog(
					frame, 
					msg, 
					"", 
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null, options, options[2]);
			if (d == JOptionPane.CANCEL_OPTION || d == JOptionPane.CLOSED_OPTION)
				return;
			if (d == JOptionPane.YES_OPTION) {
				Action saveAction = frame.getSaveAction();
				saveAction.actionPerformed(e);
			}
		}
		multiDocModel.closeDocument(associatedSingleDocModel);
		if (!permanent)
			this.trackLocalizationChanges(false);
	}

	@Override
	protected void initAction() {
		
	}
	
}
