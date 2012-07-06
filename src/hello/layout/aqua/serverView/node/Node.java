package hello.layout.aqua.serverView.node;

import java.util.List;

import org.eclipse.swt.graphics.Image;

public interface Node {
	public String getName();

	public Image getImage();

	public Image getExpandedImage();

	public Node getParent();

	public boolean hasChildren();

	public List<Node> getChildren();
}
