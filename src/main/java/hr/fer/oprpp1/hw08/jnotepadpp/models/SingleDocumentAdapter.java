package hr.fer.oprpp1.hw08.jnotepadpp.models;

public abstract class SingleDocumentAdapter implements SingleDocumentListener {

	@Override
	public void documentModifyStatusUpdated(SingleDocumentModel model) {}

	@Override
	public void documentFilePathUpdated(SingleDocumentModel model) {}

}
