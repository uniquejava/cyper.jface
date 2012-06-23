package hello.layout.aqua.scriptsView;

import java.io.File;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

//content provider provide data array.
public class ScriptsTreeContentProvider implements ITreeContentProvider {
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	// getElements provides first level data.
	public Object[] getElements(Object inputElement) {
		File[] roots = File.listRoots();
		return roots;
	}

	public Object[] getChildren(Object parentElement) {
		File parent = (File) parentElement;
		return parent.listFiles();
	}

	public Object getParent(Object element) {
		return ((File) element).getParentFile();
	}

	public boolean hasChildren(Object element) {
		Object[] obj = getChildren(element);
		return obj != null && obj.length > 0;
	}
}
