package hello.layout.aqua.action;

import hello.layout.aqua.CyperDataStudio;
import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.sqlwindow.SQLWindow;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.kitten.core.util.ErrorUtil;

public class FillPlaceHolderAction extends Action {
	private CyperDataStudio studio;

	public FillPlaceHolderAction(CyperDataStudio studio) {
		this.studio = studio;
		setText("Fill Place Holder");
		setToolTipText("Fill Place Holder");
		setImageDescriptor(ImageDescriptor.createFromImage(ImageFactory
				.loadImage(ImageFactory.QUESTION)));
	}

	@Override
	public void run() {
		SQLWindow sw = CyperDataStudio.getStudio().getSqlWindow();
		TextViewer tv = sw.getSourceViewer();
		if (tv != null) {
			StyledText text = tv.getTextWidget();
			// show result
			CTabItem item = sw.getSelection();
			if (item != null) {
				// 尝试使用用户选中的文本
				try {
					String paramLine = text.getLine(0);
					if (paramLine.indexOf("[")!=-1) {
						paramLine = paramLine.substring(paramLine.indexOf("[") + 1);
						paramLine = paramLine.substring(0, paramLine.indexOf("]"));
					}else{
						//取--符后面的部分
						paramLine = paramLine.trim().substring("--".length());
					}

					String[] params = paramLine.split(",");

					String content = text.getText();
					for (int i = 0; i < params.length; i++) {
						String param =  params[i].trim();
						content = content.replaceFirst("\\?", "'" + param + "'");
					}
					
					text.setText(content);
				} catch (Exception e) {
					MessageDialog.openError(studio.getShell(), "Oops",ErrorUtil.getError(e));
				}
			}
		}
	}
}
