package hello.layout.aqua.serverView.node;


import hello.filter.TableFilter;

import java.util.ArrayList;
import java.util.List;

public class NodeFactory {
	public static List<Node> createNodes(String rootNodeName, TableFilter tableFilter){
		List<Node> list = new ArrayList<Node>();
		Node root = new RootNode(rootNodeName, tableFilter);
		
		list.add(root);
		
		return list;
	}
}
