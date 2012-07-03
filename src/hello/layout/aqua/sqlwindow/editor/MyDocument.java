package hello.layout.aqua.sqlwindow.editor;

import hello.layout.aqua.sqlwindow.SQLWindow;

import java.io.File;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.kitten.core.util.FileUtil;

public class MyDocument extends Document implements IDocumentListener {
	private String fileName;
	private boolean dirty;
	private SQLWindow sqlWindow;

	public MyDocument(SQLWindow sqlWindow) {
		this.sqlWindow = sqlWindow;
		this.addDocumentListener(this);
	}

	public void open(String fileName) {
		try {
			this.fileName = fileName;
			set(FileUtil.getFileContent(new File(fileName), "UTF-8"));
			setDirty(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void save() {
		if (!dirty) {
			return;
		}
		if (fileName==null) {
			FileDialog dialog = new FileDialog(sqlWindow.getShell(), SWT.SAVE);
			dialog.setFilterExtensions(new String[] { "*.sql", "*.*" });
			fileName = dialog.open();
			if (fileName == null || fileName.length() == 0) {
				return;
			}
		}
		
		try {
			FileUtil.setFileContent(new File(fileName), get(), "UTF-8");
			setDirty(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getFileName() {
		return fileName;
	}

//	public void setFileName(String fileName) {
//		this.fileName = fileName;
//	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
		String text = sqlWindow.getShell().getText();
		if (dirty) {
			if (!text.endsWith("*")) {
				sqlWindow.getShell().setText(text + "*");
			}
		} else {
			if (text.endsWith("*")) {
				sqlWindow.getShell().setText(
						text.substring(0, text.length() - 1));
			}
		}
	}

	// the following 2 are for IDocumentListener
	public void documentAboutToBeChanged(DocumentEvent event) {
		// System.out.println("document about to change:" + get());
	}

	public void documentChanged(DocumentEvent event) {
		// System.out.println("document changed:" + get());
		setDirty(true);
	}

}
