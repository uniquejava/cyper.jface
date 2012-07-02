package hello.layout.aqua.sqlwindow.editor;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.swt.SWT;

public class MyContentAssistant extends ContentAssistant {
	class MyAuto extends AutoAssistListener{
		@Override
		public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
			if (e.character=='.' || (e.character =='/' && e.stateMask== SWT.ALT)) {
				System.out.println("cool");
				
				//拦截！把Alt+/转换成.，这样就会触发代码提示了.#_#
				e.stateMask = SWT.NONE;
				e.character = '.';
				super.keyPressed(e);
			}
		}
	}
	
	@Override
	protected AutoAssistListener createAutoAssistListener() {
		return new MyAuto();
	}
	
}

