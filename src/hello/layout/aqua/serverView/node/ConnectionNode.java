package hello.layout.aqua.serverView.node;

import java.util.List;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import hello.layout.aqua.ImageFactory;

public class ConnectionNode extends AbstractNode {

	@Override
	public String getName() {
		return "Database Servers";
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
		return null;
	}

	@Override
	public Image getImage() {
		return ImageFactory.loadImage(Display.getCurrent(),
				ImageFactory.DATABASE_SERVER);
	}

}
