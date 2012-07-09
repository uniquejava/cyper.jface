package hello.layout.aqua.serverView.node;


import hello.cache.TableCache;
import hello.filter.TableFilter;
import hello.layout.aqua.dialog.LogonDialog;

import java.util.ArrayList;
import java.util.List;

public class NodeFactory {
	public static List<Node> createNodes(String rootNodeName, TableFilter tableFilter){
		List<Node> list = new ArrayList<Node>();
		
		TableCache cache = TableCache.getInstance();
		boolean useCache = false;
		if (cache.get(LogonDialog.currentConnectionName)!=null) {
			useCache = true;
		}
		
		Node root = new RootNode(rootNodeName, tableFilter, useCache);
		
		list.add(root);
		
		return list;
	}
}
