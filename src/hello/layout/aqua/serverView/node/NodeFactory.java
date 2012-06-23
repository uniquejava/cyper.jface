package hello.layout.aqua.serverView.node;

import hello.layout.aqua.util.Node;

import java.util.ArrayList;
import java.util.List;

public class NodeFactory {
	public static List<Node> createNodes(){
		List<Node> list = new ArrayList<Node>();
		Node root = new RootNode();
		
		list.add(root);
		
		return list;
	}
}
