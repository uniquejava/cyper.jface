package hello.layout.aqua.serverView.node;

import java.util.List;

import org.eclipse.swt.graphics.Image;

public abstract class AbstractNode implements Node {

	public Image getExpandedImage() {
		return getImage();
	};

	@Override
	public boolean hasChildren() {
		List<Node> children = getChildren();
		return children != null && children.size() > 0;
	}
}
