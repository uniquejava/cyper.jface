package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;

public class AboutAction extends Action {
	public AboutAction() {
		setText("About Me");
	}
	@Override
	public void run() {
		MessageDialog.openInformation(CyperDataStudio.getStudio().getShell(), "About me", "Cyper.Yin(尹松柏)\n345343747@qq.com\nCyper Data Studio(2012) All rights reserved. :)");
	}
}
