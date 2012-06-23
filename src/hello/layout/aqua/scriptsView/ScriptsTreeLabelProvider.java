package hello.layout.aqua.scriptsView;

import java.io.File;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

//
	public class ScriptsTreeLabelProvider implements ILabelProvider {
		public void removeListener(ILabelProviderListener listener) {
		}
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}
		public void dispose() {
		}
		public void addListener(ILabelProviderListener listener) {
		}
		public String getText(Object element) {
			File file = (File)element;
			String name = file.getName();
			//root disk has no name
			if (name!=null && name.length()>0) {
				return name;
			}else{
				return file.getPath();
			}
		}
		public Image getImage(Object element) {
			return null;
		}
	}