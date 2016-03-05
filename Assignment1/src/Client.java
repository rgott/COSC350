import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.ObjectInputStream.GetField;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class Client 
{
	private static final int _port = 80;
	private static final String delimeter = "\r\n";
	private static final String httpBase = "GET / HTTP/1.0"; // using http protocol 1.0
	public static void main(String[] args) 
	{
		Scanner console = new Scanner(System.in);
		System.out.print("Enter a Web server name: ");
		String input = console.nextLine();			
		console.close();
		
		
		getData(hostInputMapper(input), 80);
	}
	
	private static String hostInputMapper(String unmappedHost)
	{
		String mappedHost;
		switch(unmappedHost)
		{
			case "W":
				mappedHost = "www.towson.edu";
				break;
			default:
				mappedHost = unmappedHost;
				break;
		}
		return mappedHost;
	}
	
	
	private static List<String> getData(String host,int port,String... headers)
	{
		List<String> textFromServer = new LinkedList<String>();
		
		try
		{
			Socket clientSocket = new Socket(host,port);
			PrintStream outToServer = new PrintStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			
			// http request
			String httpRequest = httpBase + delimeter;
			httpRequest += "Host: "+ host + delimeter;

			for (int i = 0; i < headers.length; i++)
			{ // add other header requirements
				httpRequest += headers[0] + delimeter;
			}
			httpRequest += delimeter;
			outToServer.print(httpRequest);
				
		    // save the streamed lines to return to caller
			String line;
		    while ((line = (inFromServer.readLine())) != null) 
		    	textFromServer.add(line);

		    inFromServer.close();
		    clientSocket.close();
		    return textFromServer;
		} catch (Exception e) {e.printStackTrace();}
		
		return null; // there was an error
	}
	//System.out.printf("%-4d  %s\n",numLines,line);
}
