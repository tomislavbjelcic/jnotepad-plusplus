package hr.fer.oprpp1.hw08.jnotepadpp.models;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import javax.swing.Action;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;

import hr.fer.oprpp1.hw08.jnotepadpp.JNotepadPP;
import hr.fer.oprpp1.hw08.jnotepadpp.actions.CloseTabAction;
import hr.fer.oprpp1.hw08.jnotepadpp.components.TabComponent;
import hr.fer.oprpp1.hw08.jnotepadpp.local.ILocalizationProvider;
import hr.fer.oprpp1.hw08.jnotepadpp.util.LocalizedUtils;

public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel {
	
	private JNotepadPP parent;	// za close akcije
	private ILocalizationProvider provider; // za close akcije
	private List<SingleDocumentModel> docs = new ArrayList<>();
	private List<MultipleDocumentListener> listeners = new LinkedList<>();
	private SingleDocumentModel currentDocument = null;
	private int currentDocumentIndex = -1;
	/**
	 * Metoda ovog promatrača se aktivira svaki put kada se indeks aktivnog taba promijeni.<br>
	 * On je zadužen za pozivanje metode {@code notifyCurrentDocumentChanged} unutar ovog razreda.
	 */
	private final ChangeListener activeTabIndexChangeListener = e -> {
		currentDocumentIndex = this.getSelectedIndex();
		if (currentDocumentIndex == -1) {	// maknut je jedini tab
			SingleDocumentModel prev = currentDocument;
			currentDocument = null;
			this.notifyCurrentDocumentChanged(prev, currentDocument);
			return;
		}
		
		SingleDocumentModel activeIndexDoc = docs.get(currentDocumentIndex);
		if (activeIndexDoc == currentDocument)
			return;
		
		SingleDocumentModel prev = currentDocument;
		currentDocument = activeIndexDoc;
		this.notifyCurrentDocumentChanged(prev, currentDocument);
	};
	
	public DefaultMultipleDocumentModel(JNotepadPP parent, ILocalizationProvider provider) {
		this.addChangeListener(activeTabIndexChangeListener);
		this.provider = Objects.requireNonNull(provider);
		this.parent = Objects.requireNonNull(parent);
	}
	
	private void notifyCurrentDocumentChanged(SingleDocumentModel prev, 
			SingleDocumentModel curr) {
		listeners.forEach(l -> l.currentDocumentChanged(prev, curr));
	}
	
	private void notifyDocumentAdded(SingleDocumentModel model) {
		listeners.forEach(l -> l.documentAdded(model));
	}
	
	private void notifyDocumentRemoved(SingleDocumentModel model) {
		listeners.forEach(l -> l.documentRemoved(model));
	}
	
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return docs.iterator();
	}

	@Override
	public SingleDocumentModel createNewDocument() {
		return this.newDoc(null, "");
	}
	
	private SingleDocumentModel newDoc(Path path, String content) {
		boolean exist = path != null;
		SingleDocumentModel newDoc = new DefaultSingleDocumentModel(path, content);

		docs.add(newDoc);
		this.notifyDocumentAdded(newDoc);
		String docName = "dummy";
		this.addTab(docName, new JScrollPane(newDoc.getTextComponent()));

		int lastIndex = this.getNumberOfDocuments() - 1;
		Action ac = new CloseTabAction(parent, provider, this, newDoc);
		TabComponent tc = new TabComponent(this, newDoc, ac);
		this.setTabComponentAt(lastIndex, tc);
		this.setSelectedIndex(lastIndex);	// ovo će aktivirati ChangeListener koji će onda ažurirati trenutni dokument
		return newDoc;
	}

	@Override
	public SingleDocumentModel getCurrentDocument() {
		return currentDocument;
	}

	@Override
	public SingleDocumentModel loadDocument(Path path) {
		path = Objects.requireNonNull(path).toAbsolutePath().normalize();
		int possibleDocIndex = documentIndexForPath(path);
		if (possibleDocIndex != -1) {
			this.setSelectedIndex(possibleDocIndex);
			return currentDocument;
		}
		
		String docStr = LocalizedUtils.readString(path);
		return this.newDoc(path, docStr);
	}
	
	private int documentIndexForPath(Path path) {
		for (int i=0, len=this.getNumberOfDocuments(); i<len; i++) {
			SingleDocumentModel d = docs.get(i);
			Path docPath = d.getFilePath();
			if (path.equals(docPath))
				return i;
		}
		return -1;
	}
	
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		Path docPath = Objects.requireNonNull(model).getFilePath();
		Path savePath = newPath == null ? docPath
				: newPath.toAbsolutePath().normalize();
		Objects.requireNonNull(savePath);
		boolean docPathChanged = !savePath.equals(docPath);
		
		String content = model.getTextComponent().getText();
		LocalizedUtils.writeString(savePath, content);
		
		if (docPathChanged)
			model.setFilePath(savePath);
		model.setModified(false);
	}

	@Override
	public void closeDocument(SingleDocumentModel model) {
		int idx = docs.indexOf(Objects.requireNonNull(model));
		if (idx == -1)
			throw new IllegalArgumentException("Nepostojeći dokument");
		
		docs.remove(idx);
		this.notifyDocumentRemoved(model);
		
		this.removeTabAt(idx); // ako je došlo do promjene trenutnog dokumenta, aktivirati će se activeTabIndexChangeListener
	}

	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(Objects.requireNonNull(l));
	}

	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(Objects.requireNonNull(l));
	}

	@Override
	public int getNumberOfDocuments() {
		return docs.size();
	}

	@Override
	public SingleDocumentModel getDocument(int index) {
		return docs.get(index);
	}
	
	
	
}
