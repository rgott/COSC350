import java.util.LinkedList;
import java.util.Scanner;

import java.awt.Point;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class RyanFongheiserServer1 
{
	public static void main(String[] args) throws IOException 
	{
		// get file
		Scanner console = new Scanner(System.in);
		File startFile = getStartupFile(console);
		
		// parse start file
		Scanner fileReader = new Scanner(startFile);
		
		
		// create starting node
		Point ParentPoint = getPointFromString(fileReader.nextLine(), " ");
		if(ParentPoint == null) 
		{
			System.out.println("Read Error check input file");
			return;
		}
		
		Graph<Integer> graph = new Graph<>(ParentPoint.x);
		Point childPoint;
		while(fileReader.hasNextLine())
		{
			childPoint = getPointFromString(fileReader.nextLine(), " ");
			
			graph.insert(ParentPoint.x, childPoint.x, childPoint.y);
		}
		
		
		
		// dijsktra's
		LinkedList<GraphNode<Integer>.Connector> routingList = graph.Dijsktra();
		
		
		// print table
		for (GraphNode<Integer>.Connector connector : routingList) {
			System.out.println(connector.next.data + "    " + connector.length);
		}
		System.out.println();
				
		// wait for input from client
		String clientSentence;
		String capitalizedSentence;
		ServerSocket clientSocket = new ServerSocket(6789);
		while (true) 
		{
			Socket connectionSocket = clientSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			//DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			// get statement from client
			clientSentence = inFromClient.readLine();

			// server only accepts client requests and updates table
			LinkedList<Point> newPoints = parseSentence(clientSentence);
			
			
			Integer senderNode = newPoints.get(0).x;
			GraphNode<Integer> senderGraphRoot = graph.find(senderNode); // first point is always sending nodes ID // finds node in servers graph
			
			Point indexPt;
			for (int i = 1; i < newPoints.size() - 1; i++) // already made the first node in the list the root node thus i = 1 not 0
			{
				indexPt = newPoints.get(i);
				senderGraphRoot.addConnectionList(new GraphNode<Integer>(indexPt.x), indexPt.y); // either inserts or updates value
			}
			
			LinkedList<GraphNode<Integer>.Connector> list = graph.Dijsktra();
			
			outputList(list); // print the list
			// TODO: update text file
		}
	}
	
	private static void outputList(LinkedList<GraphNode<Integer>.Connector> list)
	{
		for (GraphNode<Integer>.Connector item : list) 
		{
			System.out.printf("%i\t%i\t%i",item.prev.data,item.next.data,item.length);
		}
	}
	
	
	private static LinkedList<Point> parseSentence(String clientSentence) 
	{
		clientSentence.substring(1,clientSentence.length() - 1); // remove first and last character '(' and ')'
		String[] points = clientSentence.split("),("); // middle of point
		LinkedList<Point> point = new LinkedList<>();
		for (String pointItem : points) 
		{
			point.add(getPointFromString(pointItem, ","));
		}
		return point;
	}

	public static File getStartupFile(Scanner console)
	{
		System.out.print("Enter the startup file: ");
		String fileName = console.next();
		
		File file = new File(fileName);
		
		while(!file.exists())
		{
			System.out.println("Error: file does not exist");
			fileName = console.next();
			file = new File(fileName);
		}
		return file;
	}
	
	public static Point getPointFromString(String line, String delimiter)
	{
		if(line == null)
			return null;
		
		Point p = new Point();
		String[] lineArry = line.split(delimiter);
		
		if(lineArry.length >= 2)
		{
			try
			{
				p.x = Integer.parseInt(lineArry[0]);
				p.y = Integer.parseInt(lineArry[1]);
			}
			catch(NumberFormatException e)
			{
				return null;
			}
		}
		return p;
	}
	
}
