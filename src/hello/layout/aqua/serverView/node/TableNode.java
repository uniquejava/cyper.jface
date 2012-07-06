package hello.layout.aqua.serverView.node;

import hello.layout.aqua.ImageFactory;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class TableNode extends AbstractNode{
	private Node parent;
	private String name;
	
	public TableNode(Node parent,String name) {
		this.parent = parent;
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
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
				ImageFactory.TABLE);
	}

}
