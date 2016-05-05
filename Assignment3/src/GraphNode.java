import java.util.LinkedList;
public class GraphNode<T>
{
	T data;
	LinkedList<Connector> connections;
	
	Color color = Color.White;
	
	enum Color
	{
		White,
		gray,
		black
	}
	
	public GraphNode(T data)
	{
		connections = new LinkedList<>();
		this.data = data;
	}
	
	public static <V> GraphNode<V>.Connector findConnection(LinkedList<GraphNode<V>.Connector> connection,V data)
	{
		for (GraphNode<V>.Connector connector : connection) {
			if(connector.next.data.equals(data))
				return connector;
		}
		return null;
	}
	
	public GraphNode<T>.Connector findConnection(GraphNode<T> node)
	{
		for (Connector connector : connections) {
			if(connector.prev == node)
			{
				return connector;
			}
		}
		return null;
	}
	
	public void addConnectionList(GraphNode<T> node,Integer length)
	{
		Connector tmpCnnct = new Connector(this,node, length);
		int index = -1;
		if((index = connections.indexOf(tmpCnnct)) != -1)
		{
			connections.add(new Connector(this,node, length));
		}
		else // if connector exists update length
		{
			connections.get(index).length = length;
		}
	}
	
	public GraphNode<T> addConnection(GraphNode<T> root,GraphNode<T> node,Integer length)
	{
		GraphNode<T> newNode = null;
		if(root != null)
			newNode = root.find(node);
		if(newNode != null)
			node = newNode;
		connections.add(new Connector(this,node, length));
		return node;
	}
	
	public GraphNode<T> addConnection(GraphNode<T> root, T data,Integer length)
	{
		GraphNode<T> newNode = root.find(data);
		if(newNode == null)
			newNode = new GraphNode<T>(data);
		connections.add(new Connector(this,newNode, length));
		return newNode;
	}
	
	public GraphNode<T> find(T data)
	{
		for (Connector item : connections)
		{
			System.out.println(item.next.data + " == " + data);
			if(item.prev.data.equals(data))
				return item.prev;
			else
				return item.next.find(item.next);
		}
		return null;
	}
	
	
	public GraphNode<T> find(GraphNode<T> node)
	{
		for (Connector item : connections)
		{
			if(item.next == node)
				return item.next;
			else
				return item.next.find(item.next);
		}
		return null;
	}
	
	public GraphNode<T>.Connector lowestValue(LinkedList<GraphNode<T>.Connector> list)
	{
		if(list.size() == 0)
		{
			return null;
		}
		GraphNode<T>.Connector min = (GraphNode<T>.Connector) list.getFirst();
		
		for (GraphNode<T>.Connector item : list)
		{
			if(min.length > item.length && min.next.color != Color.black)
				min = item;
		}
		return min;
	}
	
	public class Connector implements Comparable
	{
		GraphNode<T> prev;
		GraphNode<T> next;
		Integer length;
		
		public Connector(GraphNode<T> prev,GraphNode<T> node, Integer length)
		{
			this.prev = prev;
			this.next = node;
			this.length = length;
		}

		@Override
		public int compareTo(Object arg0)
		{
			Integer obj = ((Connector)arg0).length;
			if(this.length == null)
			{
				return 1;
			}
			if(obj == null)
			{
				return -1;
			}
			if(obj == this.length)
			{
				return 0;
			}
			if(obj < this.length)
			{
				return -1;
			}
			if(obj > this.length)
			{
				return 1;
			}
			
			return 0;
		}
		
	}
}
