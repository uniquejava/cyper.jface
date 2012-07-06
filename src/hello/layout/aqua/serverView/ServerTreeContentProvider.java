package hello.layout.aqua.serverView;

import hello.layout.aqua.serverView.node.Node;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

//content provider provide data array.
public class ServerTreeContentProvider implements ITreeContentProvider {
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	// getElements provides first level data.
	public Object[] getElements(Object inputElement) {
		List<Node> nodeList = (List<Node>) inputElement;
		return nodeList.toArray();
	}

	public Object[] getChildren(Object parentElement) {
		Node parent = (Node) parentElement;
		List<Node> children = parent.getChildren();
		return children.toArray();
	}

	public Object getParent(Object element) {
		return ((Node) element).getParent();
	}

	public boolean hasChildren(Object element) {
		return ((Node) element).hasChildren();
	}
}
