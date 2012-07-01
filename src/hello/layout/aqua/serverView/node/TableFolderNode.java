package hello.layout.aqua.serverView.node;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import hello.layout.aqua.ImageFactory;
import hello.layout.aqua.util.Node;

public class TableFolderNode implements Node {
	private Node parent;
	private String name;
	
	public TableFolderNode(Node parent,String name) {
		this.parent = parent;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public List<Node> getChildren() {
		return null;
	}

	@Override
	public Node getParent() {
		return parent;
	}

	@Override
	public Image getImage() {
		return ImageFactory.loadImage(Display.getCurrent(),
				ImageFactory.NODE_TABLE);
	}

}
