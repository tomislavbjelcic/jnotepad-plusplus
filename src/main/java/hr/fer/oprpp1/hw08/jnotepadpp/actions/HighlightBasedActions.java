package hr.fer.oprpp1.hw08.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class HighlightBasedActions {
	
	@FunctionalInterface
	private interface DocumentAction {
		void perform(int off, int len, Document doc, UnaryOperator<String> selectedTextChanger);
	}
	
	private String clipboard = null;
	private JFrame frame;
	private ILocalizationProvider provider;
	private MultipleDocumentModel multiDocModel;
	
	private Action cutAction;
	private Action copyAction;
	private Action pasteAction;
	private Action changeCaseAction;
	private Action toLowerCaseAction;
	private Action toUpperCaseAction;
	private Action invertCaseAction;
	private Action sortLinesAction;
	private Action sortLinesAscendingAction;
	private Action sortLinesDescendingAction;
	private Action uniqueLinesAction;
	
	private Action[] highlightDependentActions;
	
	private abstract class ChangeDocumentAction extends JNotepadPPAction {
		
		DocumentAction da;
		UnaryOperator<String> selectedTextChanger;
		
		public ChangeDocumentAction(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel, 
				DocumentAction da, UnaryOperator<String> selectedTextChanger) {
			super(frame, provider, multiDocModel);
			this.da = da;
			this.selectedTextChanger = selectedTextChanger;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Caret caret = caret();
			Document document = document();
			int dot = caret.getDot();
			int mark = caret.getMark();
			int len = Math.abs(dot - mark);
			int off = Math.min(dot, mark);
			da.perform(off, len, document, selectedTextChanger);
		}
	}
	
	private UnaryOperator<String> transformToLowercase
		= s -> s.toLowerCase(new Locale(provider.getCurrentLanguage()));
	private UnaryOperator<String> transformToUppercase
		= s -> s.toUpperCase(new Locale(provider.getCurrentLanguage()));
	private UnaryOperator<String> transformToInvertCase = s -> {
		char[] chars = s.toCharArray();
		int clen = chars.length;
		StringBuilder sb = new StringBuilder(clen);
		Locale l = new Locale(provider.getCurrentLanguage());
		for (int i=0; i<clen; i++) {
			char ch = chars[i];
			String chstr = String.valueOf(ch);
			String chstrinv = null;
			if (Character.isLowerCase(ch))
				chstrinv = chstr.toUpperCase(l);
			else if (Character.isUpperCase(ch))
				chstrinv = chstr.toLowerCase(l);
			else
				chstrinv = chstr;
			chars[i] = chstrinv.charAt(0);
		}
		return new String(chars);
	};
	private UnaryOperator<String> ascOrderSort = s -> {
		Locale l = new Locale(provider.getCurrentLanguage());
		Comparator<Object> comp = Collator.getInstance(l);
		return s.lines().sorted(comp).collect(Collectors.joining("\n", "", "\n"));
	};
	private UnaryOperator<String> descOrderSort = s -> {
		Locale l = new Locale(provider.getCurrentLanguage());
		Comparator<Object> comp = Collator.getInstance(l).reversed();
		return s.lines().sorted(comp).collect(Collectors.joining("\n", "", "\n"));
	};
	private UnaryOperator<String> unique = s -> {
		return s.lines().distinct().collect(Collectors.joining("\n", "", "\n"));
	};
		
	private CaretListener cl = e -> {
		Caret caret = caret();
		int dot = caret.getDot();
		int mark = caret.getMark();
		int len = Math.abs(dot - mark);
		boolean enable = len > 0;
		toggleHighlightDependentAction(enable);
	};
	
	private DocumentAction cut = (off, len, doc, selectedTextChanger) -> {
		try {
			clipboard = doc.getText(off, len);
			doc.remove(off, len);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		pasteAction.setEnabled(true);
	};
	private DocumentAction copy = (off, len, doc, selectedTextChanger) -> {
		try {
			clipboard = doc.getText(off, len);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		pasteAction.setEnabled(true);
	};
	private DocumentAction paste = (off, len, doc, selectedTextChanger) -> {
		try {
			doc.remove(off, len);
			doc.insertString(off, clipboard, null);
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
	};
	private DocumentAction changeCase = (off, len, doc, selectedTextChanger) -> {
		try {
			String highlighted = doc.getText(off, len);
			String lang = provider.getCurrentLanguage();
			Locale l = new Locale(lang);
			String caseChangedStr = selectedTextChanger.apply(highlighted);
			doc.remove(off, len);
			doc.insertString(off, caseChangedStr, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	};
	private DocumentAction changeLines = (off, len, doc, selectedTextChanger) -> {
		Element root = doc.getDefaultRootElement();
		int startRow = root.getElementIndex(off);
		int endRow = root.getElementIndex(off + len);
		if (endRow == root.getElementCount() - 1) {
			try {
				doc.insertString(doc.getLength(), "\n", null);
			} catch (BadLocationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int startRowOffset = root.getElement(startRow).getStartOffset();
		int endRowOffset = root.getElement(endRow).getEndOffset();
		int linesLen = endRowOffset - startRowOffset;
		String linesStr = null;
		try {
			linesStr = doc.getText(startRowOffset, linesLen);
			doc.remove(startRowOffset, linesLen);
			String sortedLinesStr = selectedTextChanger.apply(linesStr);
			doc.insertString(startRowOffset, sortedLinesStr, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
	

	public HighlightBasedActions(JFrame frame, ILocalizationProvider provider, MultipleDocumentModel multiDocModel) {
		this.frame = Objects.requireNonNull(frame);
		this.provider = Objects.requireNonNull(provider);
		this.multiDocModel = Objects.requireNonNull(multiDocModel);
		initActions();
	}
	
	private void updateActions(SingleDocumentModel currentModel) {
		if (currentModel == null) {
			toggleHighlightDependentAction(false);
			pasteAction.setEnabled(false);
			return;
		}
		
		pasteAction.setEnabled(clipboard != null);
		cl.caretUpdate(null);
	}
	
	private Caret caret() {
		return textComponent().getCaret();
	}
	
	private JTextComponent textComponent() {
		return multiDocModel.getCurrentDocument().getTextComponent();
	}
	
	private Document document() {
		return textComponent().getDocument();
	}
	
	private void toggleHighlightDependentAction(boolean enable) {
		for (Action ac : highlightDependentActions)
			ac.setEnabled(enable);
	}

	private void initActions() {
		cutAction = new ChangeDocumentAction(frame, provider, multiDocModel, cut, null) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_cut");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_cut");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_X);
				this.setEnabled(false);
			}
		};
		
		copyAction = new ChangeDocumentAction(frame, provider, multiDocModel, copy, null) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_copy");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_copy");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_C);
				this.setEnabled(false);
			}
		};
		
		pasteAction = new ChangeDocumentAction(frame, provider, multiDocModel, paste, null) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_paste");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_paste");
				this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_V);
				this.setEnabled(false);
			}
		};
		
		changeCaseAction = new ChangeDocumentAction(frame, provider, multiDocModel, null, null) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "change_case");
				this.setEnabled(false);
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {}
		};
		
		toLowerCaseAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeCase, transformToLowercase) {
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_tolowercase");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_tolowercase");
				this.setEnabled(false);
			}
		};
		
		toUpperCaseAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeCase, transformToUppercase) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_touppercase");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_touppercase");
				this.setEnabled(false);
			}
		};
		
		invertCaseAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeCase, transformToInvertCase) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_invertcase");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_invertcase");
				this.setEnabled(false);
			}
		};
		
		sortLinesAction = new ChangeDocumentAction(frame, provider, multiDocModel, null, null) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "sort_lines");
				this.setEnabled(false);
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {}
		};
		
		sortLinesAscendingAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeLines, ascOrderSort) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_sort_lines_asc");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_sort_lines_asc");
				this.setEnabled(false);
			}
			
		};

		sortLinesDescendingAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeLines, descOrderSort) {

			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_sort_lines_desc");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_sort_lines_desc");
				this.setEnabled(false);
			}

		};
		
		uniqueLinesAction = new ChangeDocumentAction(frame, provider, multiDocModel, changeLines, unique) {
			
			@Override
			protected void initAction() {
				this.putLocalizedValue(Action.NAME, "ac_name_unique_lines");
				this.putLocalizedValue(Action.SHORT_DESCRIPTION, "ac_desc_unique_lines");
				this.setEnabled(false);
			}
			
		};
		
		highlightDependentActions = new Action[]
				{cutAction, copyAction, changeCaseAction, 
						toLowerCaseAction, toUpperCaseAction, invertCaseAction, sortLinesAction, sortLinesAction,
						sortLinesAscendingAction, sortLinesDescendingAction, uniqueLinesAction};
		
		multiDocModel.addMultipleDocumentListener(new MultipleDocumentAdapter() {
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				updateActions(currentModel);
				if (previousModel != null)
					previousModel.getTextComponent().removeCaretListener(cl);
				if (currentModel != null)
					currentModel.getTextComponent().addCaretListener(cl);
			}
		});
	}

	public Action getCutAction() {
		return cutAction;
	}

	public Action getCopyAction() {
		return copyAction;
	}

	public Action getPasteAction() {
		return pasteAction;
	}
	
	public Action getToLowerCaseAction() {
		return toLowerCaseAction;
	}

	public Action getChangeCaseAction() {
		return changeCaseAction;
	}
	
	public Action getToUpperCaseAction() {
		return toUpperCaseAction;
	}
	
	public Action getInvertCaseAction() {
		return invertCaseAction;
	}
	
	public Action getSortLinesAction() {
		return sortLinesAction;
	}
	
	public Action getSortLinesAscendingAction() {
		return sortLinesAscendingAction;
	}
	
	public Action getSortLinesDescendingAction() {
		return sortLinesDescendingAction;
	}
	
	public Action getUniqueLinesAction() {
		return uniqueLinesAction;
	}

}
