package hello.layout.aqua.dialog;


import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FindAndReplace extends Dialog {

	private SQLWindow sqlWindow;
	private Button btFind;
	private Button btReplace;
	private Button btnReplaceAndFind;
	private Button btClose;
	private FindReplaceDocumentAdapter findAdapter;

	public FindAndReplace(SQLWindow sqlWindow, Shell parentShell) {
		super(parentShell);
		this.sqlWindow = sqlWindow;
		findAdapter = new FindReplaceDocumentAdapter(sqlWindow.getDocument());
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Find/Replace");
		newShell.setLocation(getParentShell().getLocation());
		newShell.setSize(300, 270);
	}

	@Override
	protected int getShellStyle() {
		// 设置成非模态对话框
		return SWT.TITLE|SWT.CLOSE|SWT.BORDER|SWT.MODELESS;
	}

	@Override
	protected Control createContents(Composite parent) {
		parent.setLayout(new GridLayout(2, false));
		// find
		new Label(parent, SWT.LEFT).setText("Find:");
		final Text findText = new Text(parent, SWT.BORDER);
		findText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// replace with
		new Label(parent, SWT.LEFT).setText("Replace with:");
		final Text replaceText = new Text(parent, SWT.BORDER);
		replaceText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// direction
		Group group = new Group(parent, SWT.None);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		group.setLayoutData(data);
		group.setText("Direction");
		group.setLayout(new GridLayout(2, true));

		final Button forwardButton = new Button(group, SWT.RADIO);
		forwardButton.setText("Forward");

		final Button backButton = new Button(group, SWT.RADIO);
		backButton.setText("Backward");

		// options
		group = new Group(parent, SWT.None);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		group.setLayoutData(data);
		group.setText("Options");
		group.setLayout(new GridLayout(2, true));

		final Button match = new Button(group, SWT.CHECK);
		match.setText("Case sensative");

		final Button wholeWord = new Button(group, SWT.CHECK);
		wholeWord.setText("Whole word");

		final Button regexp = new Button(group, SWT.CHECK);
		regexp.setText("Regular expression");

		// buttons
		Composite composite = new Composite(parent, SWT.None);
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		composite.setLayoutData(data);
		composite.setLayout(new GridLayout(2, true));

		btFind = new Button(composite, SWT.PUSH);
		btFind.setText("Find");
		btFind.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		btReplace = new Button(composite, SWT.PUSH);
		btReplace.setText("Replace");
		btReplace.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		btnReplaceAndFind = new Button(composite, SWT.PUSH);
		btnReplaceAndFind.setText("Replace/Find");
		btnReplaceAndFind.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		btClose = new Button(composite, SWT.PUSH);
		btClose.setText("Close");
		btClose.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		regexp.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (regexp.getSelection()) {
					wholeWord.setEnabled(false);
				} else {
					wholeWord.setEnabled(true);
				}
			}
		});
		btFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean b = sqlWindow.eventManager.howAboutFind(findAdapter,
						findText.getText(), forwardButton.getSelection(),
						match.getSelection(), wholeWord.getSelection(),
						regexp.getSelection());
				enableReplaceButtions(b);
			}
		});
		btReplace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sqlWindow.eventManager.doReplace(findAdapter,
						replaceText.getText(), regexp.getSelection());
				enableReplaceButtions(false);
			}
		});
		btnReplaceAndFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				sqlWindow.eventManager.doReplace(findAdapter,
						replaceText.getText(), regexp.getSelection());
				boolean b = sqlWindow.eventManager.howAboutFind(findAdapter,
						findText.getText(), forwardButton.getSelection(),
						match.getSelection(), wholeWord.getSelection(),
						regexp.getSelection());
				enableReplaceButtions(b);
			}
		});
		btClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				getShell().close();
			}
		});

		forwardButton.setSelection(true);
		enableReplaceButtions(false);
		findText.setText(sqlWindow.getSourceViewer().getTextWidget().getSelectionText());
		findText.setFocus();

		return parent;
	}

	private void enableReplaceButtions(boolean enabled) {
		btReplace.setEnabled(enabled);
		btnReplaceAndFind.setEnabled(enabled);
	}
}
