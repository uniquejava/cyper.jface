package hello.layout.aqua.serverView.node;


import java.util.ArrayList;
import java.util.List;

public class NodeFactory {
	public static List<Node> createNodes(String rootNodeName){
		List<Node> list = new ArrayList<Node>();
		Node root = new RootNode(rootNodeName);
		
		list.add(root);
		
		return list;
	}
}
