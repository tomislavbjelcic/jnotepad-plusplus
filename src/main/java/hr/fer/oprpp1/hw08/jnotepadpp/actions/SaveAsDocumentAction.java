package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class SaveAsDocumentAction extends JNotepadPPAction {

	public SaveAsDocumentAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		super(frame, provider, multiDocModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SingleDocumentModel currentDoc = multiDocModel.getCurrentDocument();
		JFileChooser jfc = new JFileChooser();
		String dialogTitle = (String) this.getValue(Action.SHORT_DESCRIPTION);
		jfc.setDialogTitle(dialogTitle);
		jfc.setSelectedFile(new File(currentDoc.getFileName()));
		
		int jfcResult = jfc.showSaveDialog(frame);
		if (jfcResult != JFileChooser.APPROVE_OPTION)
			return;
		
		Path selectedPath = jfc.getSelectedFile().toPath();
		if (Files.exists(selectedPath)) {
			String fileName = selectedPath.getFileName().toString();
			String msg = String.format("%s \"%s\" %s.\n%s?",
					provider.getString("prompt_overwrite_part1"),
					fileName,
					provider.getString("prompt_overwrite_part2"),
					provider.getString("prompt_overwrite_part3")
					);
			Object[] options = new Object[] {
					provider.getString("yes"),
					provider.getString("no")
			};
			int d = JOptionPane.showOptionDialog(
					frame, 
					msg, 
					"", 
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null, options, options[1]);
			if (d == JOptionPane.NO_OPTION || d == JOptionPane.CLOSED_OPTION)
				return;
		}
		
		try {
			multiDocModel.saveDocument(currentDoc, selectedPath);
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
		this.putLocalizedValue(Action.NAME, "ac_name_save_as");
		this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_save_as");
		this.setEnabled(false);
		
		multiDocModel.addMultipleDocumentListener(new MultipleDocumentAdapter() {

			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				SaveAsDocumentAction.this.setEnabled(currentModel != null);
			}
			
		});
	}

}
