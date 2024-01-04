package hr.fer.oprpp1.hw08.jnotepadpp.components;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationListener;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentAdapter;
import hr.fer.oprpp1.hw08.jnotepadpp.models.MultipleDocumentModel;
import hr.fer.oprpp1.hw08.jnotepadpp.models.SingleDocumentModel;

public class StatusBar extends JPanel {
	
	private CounterJLabel lengthLabel;
	private CounterJLabel lnLabel;
	private CounterJLabel colLabel;
	private CounterJLabel selLabel;
	private CurrentTimeJLabel clock;
	private ILocalizationProvider provider;
	private MultipleDocumentModel model;
	
	private CaretListener cl = e -> {
		JTextComponent tc = model.getCurrentDocument().getTextComponent();
		Document doc = tc.getDocument();
		Element root = doc.getDefaultRootElement();
		Caret caret = tc.getCaret();
		
		int dot = caret.getDot();
		int mark = caret.getMark();
		int len = doc.getLength();
		int sel = Math.abs(dot - mark);
		int ln = root.getElementIndex(dot);
		int col = dot - root.getElement(ln).getStartOffset();
		updateStatus(len, ln, col, sel);
	};
	
	public StatusBar(ILocalizationProvider provider, MultipleDocumentModel model) {
		this.provider = Objects.requireNonNull(provider);
		this.model = Objects.requireNonNull(model);
		
		lengthLabel = new CounterJLabel("");
		lnLabel = new CounterJLabel("Ln");
		colLabel = new CounterJLabel("Col");
		selLabel = new CounterJLabel("Sel");
		clock = new CurrentTimeJLabel(1000);
		initLayout();
		
		ILocalizationListener ll = () -> lengthLabel.setLeftText(provider.getString("length"));
		provider.addLocalizationListener(ll);
		ll.localizationChanged();
		
		model.addMultipleDocumentListener(new MultipleDocumentAdapter() {
			@Override
			public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
				updateStatus(currentModel);
				if (previousModel != null)
					previousModel.getTextComponent().removeCaretListener(cl);
				if (currentModel != null)
					currentModel.getTextComponent().addCaretListener(cl);
				
			}
			
		});
	}
	
	private void initLayout() {
		this.setLayout(new GridLayout(1, 3));
		Border b = BorderFactory.createMatteBorder(3, 1, 0, 1, Color.GRAY);
		this.setBorder(b);
		
		lengthLabel.setHorizontalAlignment(SwingConstants.LEFT);
		this.add(lengthLabel);
		
		JPanel lnColSelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 2));
		lnColSelPanel.add(lnLabel);
		lnColSelPanel.add(colLabel);
		lnColSelPanel.add(selLabel);
		Border middleCompBorder = BorderFactory.createMatteBorder(0, 1, 0, 1, Color.LIGHT_GRAY);
		lnColSelPanel.setBorder(middleCompBorder);
		this.add(lnColSelPanel);
		
		clock.setHorizontalAlignment(SwingConstants.RIGHT);
		this.add(clock);
	}
	
	private void updateStatus(int len, int ln, int col, int sel) {
		lengthLabel.setCount(len);
		lnLabel.setCount(ln + 1);
		colLabel.setCount(col + 1);
		selLabel.setCount(sel);
	}
	
	private void updateStatus(SingleDocumentModel m) {
		if (m == null)
			updateStatus(0, 0, 0, 0);
		else
			cl.caretUpdate(null);
			
	}
	
	public void startClock() {
		clock.startTimer();
	}
	
	public void stopClock() {
		clock.stopTimer();
	}

}
