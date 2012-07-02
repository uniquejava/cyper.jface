package hello.layout.aqua.sqlwindow.editor;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;

public class MyDocument extends Document implements IDocumentListener {
	private String fileName;
	private boolean dirty;
	
	public MyDocument() {
		this.addDocumentListener(this);
	}
	
	public void save(){
		if (fileName == null) {
			throw new IllegalStateException("file name cannot be null");
		}
		
		
	}
	
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	//the following 2 are for IDocumentListener
	public void documentAboutToBeChanged(DocumentEvent event) {
//		System.out.println("document about to change:" + get());
	}
	public void documentChanged(DocumentEvent event) {
//		System.out.println("document changed:" + get());
		setDirty(true);
	}

}
