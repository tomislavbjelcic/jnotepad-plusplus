package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Action;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;

public class QuitAction extends JNotepadPPAction {
	
	public QuitAction(JNotepadPP frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		super(frame, provider, multiDocModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		List<Action> actions = new LinkedList<>();
		for (var doc : multiDocModel) {
			Action a = new CloseTabAction((JNotepadPP)frame, provider, multiDocModel, doc);
			actions.add(a);
		}
		actions.forEach(a -> a.actionPerformed(e));
		int count = multiDocModel.getNumberOfDocuments();
		if (count == 0)
			frame.dispose();
	}

	@Override
	protected void initAction() {
		this.putLocalizedValue(Action.NAME, "ac_name_quit");
		this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_quit");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_Q);
	}

	

}
