package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;

public class OpenDocumentAction extends JNotepadPPAction {

	public OpenDocumentAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		super(frame, provider, multiDocModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		String dialogTitle = (String) this.getValue(Action.SHORT_DESCRIPTION);
		jfc.setDialogTitle(dialogTitle);
		
		int jfcResult = jfc.showOpenDialog(frame);
		if (jfcResult != JFileChooser.APPROVE_OPTION)
			return;
		
		Path selectedPath = jfc.getSelectedFile().toPath();
		try {
			multiDocModel.loadDocument(selectedPath);
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

	@Override
	protected void initAction() {
		this.putLocalizedValue(Action.NAME, "ac_name_open");
		this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_open");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
		this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_O);
	}
	
}
