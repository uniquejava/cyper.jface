package hello.layout.aqua.serverView;

import hello.layout.aqua.util.Node;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

//
	public class ServerTreeLabelProvider implements ILabelProvider {
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
			Node node = (Node)element;
			return node.getName();
		}
		public Image getImage(Object element) {
			Node node = (Node)element;
			return node.getImage();
		}
	}