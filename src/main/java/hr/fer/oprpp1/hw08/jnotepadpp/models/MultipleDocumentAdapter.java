package hr.fer.oprpp1.hw08.jnotepadpp.models;

public abstract class MultipleDocumentAdapter implements MultipleDocumentListener {

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {}

	@Override
	public void documentAdded(SingleDocumentModel model) {}

	@Override
	public void documentRemoved(SingleDocumentModel model) {}
	
}
