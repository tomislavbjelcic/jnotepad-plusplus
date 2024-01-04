package hr.fer.oprpp1.hw08.jnotepadpp.models;

import java.nio.file.Path;

import javax.swing.JTextArea;

public interface SingleDocumentModel {
	
	String UNNAMED_DOCNAME = "(unnamed)";
	
	JTextArea getTextComponent();
	Path getFilePath();
	void setFilePath(Path path);
	boolean isModified();
	void setModified(boolean modified);
	void addSingleDocumentListener(SingleDocumentListener l);
	void removeSingleDocumentListener(SingleDocumentListener l);
	default String getFileName() {
		Path p = getFilePath();
		return p == null ? UNNAMED_DOCNAME : p.getFileName().toString();
	}
	default String getFullPathString() {
		Path p = getFilePath();
		return p == null ? UNNAMED_DOCNAME : p.toString();
	}
}
