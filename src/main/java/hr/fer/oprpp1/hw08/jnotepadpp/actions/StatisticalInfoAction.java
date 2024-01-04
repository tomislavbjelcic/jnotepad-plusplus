package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class StatisticalInfoAction extends JNotepadPPAction {
	
	private static class StatInfo {
		String content;
		JTextArea textComponent;
		long charCount = 0L;
		long nonBlankCharCount = 0L;
		long lineCount = 0L;
		
		StatInfo(JTextArea textComponent) {
			this.textComponent = textComponent;
			content = textComponent.getText();
			calculate();
		}
		
		void calculate() {
			calculateChars();
			calculateLines();
		}
		
		void calculateChars() {
			char[] contentArr = content.toCharArray();
			charCount = contentArr.length;
			for (char c : contentArr) {
				if (!Character.isWhitespace(c))
					nonBlankCharCount++;
			}
		}
		void calculateLines() {
			//lineCount = content.lines().count();
			lineCount = textComponent.getLineCount();
		}
	}

	public StatisticalInfoAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		super(frame, provider, multiDocModel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SingleDocumentModel currentDoc = multiDocModel.getCurrentDocument();
		JTextArea textComponent = currentDoc.getTextComponent();
		
		StatInfo info = new StatInfo(textComponent);
		String msgInfo = String.format("%s %d %s, %d %s %d %s.", 
				provider.getString("stat_info_msg_part1"),
				info.charCount,
				provider.getString("stat_info_msg_part2"),
				info.nonBlankCharCount,
				provider.getString("stat_info_msg_part3"),
				info.lineCount,
				provider.getString("stat_info_msg_part4"));
		
		JOptionPane.showMessageDialog(frame, msgInfo, ((String) this.getValue(Action.NAME)), JOptionPane.INFORMATION_MESSAGE);
		
	}

	@Override
	protected void initAction() {
		this.putLocalizedValue(Action.NAME, "ac_name_stat_info");
		this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_stat_info");
		this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control I"));
		this.setEnabled(false);
		
		multiDocModel.addMultipleDocumentListener(new MultipleDocumentAdapter() {
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				setEnabled(currentModel != null);
			}
		});
	}

}
