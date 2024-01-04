package hr.fer.oprpp1.hw08.jnotepadpp.models;

import java.awt.Font;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DefaultSingleDocumentModel implements SingleDocumentModel {
	
	private Path filePath;
	private JTextArea textComponent;
	private boolean modified = false;
	private List<SingleDocumentListener> listeners = new LinkedList<>();
	
	public DefaultSingleDocumentModel() {
		this(null, "");
	}
	
	private void initTextComponent(String content) {
		textComponent = new JTextArea();
		Font orig = textComponent.getFont();
		Font mono = new Font(Font.MONOSPACED, orig.getStyle(), orig.getSize())
				.deriveFont(1.5f * orig.getSize());
		textComponent.setFont(mono);
		textComponent.setText(Objects.requireNonNull(content));
		textComponent.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {setModified(true);}

			@Override
			public void removeUpdate(DocumentEvent e) {setModified(true);}

			@Override
			public void changedUpdate(DocumentEvent e) {}
			
		});
	}
	
	public DefaultSingleDocumentModel(Path filePath, String content) {
		this.setFilePath(filePath);
		initTextComponent(content);
	}
	
	private void notifyModified() {
		listeners.forEach(l -> l.documentModifyStatusUpdated(this));
	}
	
	private void notifyPathChange() {
		listeners.forEach(l -> l.documentFilePathUpdated(this));
	} 
	
	@Override
	public JTextArea getTextComponent() {
		return textComponent;
	}

	@Override
	public Path getFilePath() {
		return filePath;
	}

	@Override
	public void setFilePath(Path path) {
		this.filePath = path == null ? path : 
					path.toAbsolutePath().normalize();
		notifyPathChange();
	}

	@Override
	public boolean isModified() {
		return modified;
	}

	@Override
	public void setModified(boolean modified) {
		this.modified = modified;
		notifyModified();
	}

	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(Objects.requireNonNull(l));
	}
	
	
	
}
