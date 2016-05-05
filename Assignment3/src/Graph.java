import java.util.Collections;
import java.util.LinkedList;



public class Graph<T>{
	GraphNode<T> root;
	LinkedList<GraphNode<T>> nodes;
	public Graph(GraphNode<T> root)
	{
		this.root = root;
		nodes = new LinkedList<>();
		nodes = new LinkedList<>();
		nodes.add(root);
	}
	
	public Graph(T data)
	{
		this.root = new GraphNode<T>(data);
		nodes = new LinkedList<>();
		nodes.add(root);
	}

	public void insert(T parent,T conTo, int length)
	{
		GraphNode<T> node = findInList(conTo);
		if(node == null)
		{
			node = new GraphNode<T>(conTo);
			nodes.add(node);
		}
		findInList(parent).addConnectionList(node, length);
	}
	
	private GraphNode<T> findInList(T data)
	{
		for (GraphNode<T> node : nodes)
		{
			if(node.data.equals(data))
				return node;
		}
		return null;
	}
	public GraphNode<T> find(T data)
	{
		for (GraphNode<T> node : nodes) {
			if(node.data.equals(data))
			{
				return node;
			}
		}
		return null;
	}
	private GraphNode<T>.Connector findConnectionSpecific(GraphNode<T> parent,GraphNode<T> node)
	{
		for (GraphNode<T>.Connector connector : parent.connections) {
			if(connector.next == node)
			{
				return connector;
			}
		}
		return null;
	}
	public LinkedList<GraphNode<T>.Connector> Dijsktra()
	{
		LinkedList<GraphNode<T>.Connector> baseList = new LinkedList<>();
		GraphNode<T> list = new GraphNode<T>(null);
		for (GraphNode<T> graphNode : nodes) 
		{
			list.addConnection(null, graphNode, null);
		}
		
		Dijsktra(baseList, list.connections);
		return list.connections;
	}
	public void Dijsktra(LinkedList<GraphNode<T>.Connector> baseList, LinkedList<GraphNode<T>.Connector> nodes)
	{
		GraphNode<T>.Connector node;
		
		// color all to white
		for (GraphNode<T>.Connector connector : nodes)
			connector.next.color = GraphNode.Color.White;
		
		baseList.addAll(root.connections);
 		root.color = GraphNode.Color.black;
		nodes.get(findIndex(nodes, root)).length = 0;
		
		while(baseList.size() != 0)
		{
			Collections.sort(baseList);
			
//			for (GraphNode<T>.Connector connector : baseList) {
//				System.out.println(connector.next.data + "    " + connector.length);
//			}
//			System.out.println();
			
			node = baseList.getLast();
			baseList.removeLast();
			
			node.next.color = GraphNode.Color.black;
			Integer len = nodes.get(findIndex(nodes,node.prev)).length;
			int currIndex = findIndex(nodes, node.next);
			if((nodes.get(currIndex).length == null && len != null) || nodes.get(currIndex).length > len)
			{
				nodes.get(currIndex).length = len + node.length;
			}
			baseList.addAll(node.next.connections); 
			removeBlackNodes(baseList);
		}
	}
	public LinkedList<GraphNode<T>.Connector> BellManFord()
	{
		LinkedList<GraphNode<T>.Connector> baseList = new LinkedList<>();
		GraphNode<T> list = new GraphNode<T>(null);
		for (GraphNode<T> graphNode : nodes) 
		{
			list.addConnection(null, graphNode, null);
		}
		
		BellManFord(baseList, list.connections);
		return list.connections;
	}
	private void BellManFord(LinkedList<GraphNode<T>.Connector> baseList, LinkedList<GraphNode<T>.Connector> nodes)
	{
		GraphNode<T>.Connector node;
		
		// color all to white
		for (GraphNode<T>.Connector connector : nodes)
			connector.next.color = GraphNode.Color.White;
		
		baseList.addAll(root.connections);
 		root.color = GraphNode.Color.black;
		nodes.get(findIndex(nodes, root)).length = 0;
		
		while(baseList.size() != 0)
		{
			Collections.sort(baseList);
			
//			for (GraphNode<T>.Connector connector : baseList) {
//				System.out.println(connector.next.data + "    " + connector.length);
//			}
//			System.out.println();
			
			node = baseList.getLast();
			baseList.removeLast();
			
			node.next.color = GraphNode.Color.black;
			Integer len = nodes.get(findIndex(nodes,node.prev)).length;
			int currIndex = findIndex(nodes, node.next);
			if((nodes.get(currIndex).length == null && len != null) || nodes.get(currIndex).length > len)
			{
				nodes.get(currIndex).length = len + node.length;
			}
			baseList.addAll(node.next.connections); 
		}
		
	}
	public int findIndex(LinkedList<GraphNode<T>.Connector> nodes, GraphNode<T> node)
	{
		for (int i = 0; i < nodes.size(); i++) {
			if(nodes.get(i).next == node)
				return i;
		}
		return -1;
	}
	
	
	public LinkedList<GraphNode<T>.Connector> removeBlackNodes(LinkedList<GraphNode<T>.Connector> nodes)
	{
		for (int i = 0; i < nodes.size(); i++) {
			if(nodes.get(i).next.color != GraphNode.Color.White)
			{
				nodes.remove(i);
			}
		}
		return nodes;
	}
	
	
	public Integer relax(GraphNode<T>.Connector conNode,GraphNode<T> fakeList)
	{
		GraphNode<T>.Connector parent = findConnectionSpecific(fakeList,conNode.prev);
		Integer len = parent.length + conNode.length;
		GraphNode<T>.Connector current = findConnectionSpecific(fakeList,conNode.next);
		
		if(current.length == null || current.length > len)
			return len;
		else
			return current.length;
	}
	
	public GraphNode<T>.Connector getLowest(LinkedList<GraphNode<T>.Connector> con)
	{
		GraphNode<T>.Connector min = con.getFirst();
		for (int i = 1; i < con.size(); i++) {
			if(min.length > con.get(i).length)
			{
				min = con.get(i);
			}
		}
		return min;
	}
}