import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client 
{
	private static final int _MAX_PORT = 65535;
	
	public static void main(String[] args) 
	{
		Scanner console = new Scanner(System.in);
		System.out.print("Enter a Web server name: ");
		String rawHost = console.nextLine();			
		
		System.out.print("On Port: ");
		int port = getRangedInteger(console, "Please enter a number (0 - " + _MAX_PORT + "): ", "Please enter a number (0 - " + _MAX_PORT + "): ", 0, _MAX_PORT);
		
		console.close();
		
		List<String> responseLines = getHttpResponseLines(hostInputMapper(rawHost), port);
		if(responseLines != null)
		{
			List<Integer> responseData = processResponse(console, responseLines);
		
			//e connect to server on local host
			sendData("localhost", 21252, responseData);
		}
		
		
	}
	
	private static void sendData(String host,int port, List<Integer> responseData)
	{
		try
		{
			Socket clientSocket = new Socket(host,port);
			PrintStream outToServer = new PrintStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
			outToServer.println("c.2=" + responseData.get(0));
			outToServer.println("c.3=" + responseData.get(1));
			outToServer.println("c.4=" + responseData.get(2));
			
			outToServer.close();
		    inFromServer.close();
		    clientSocket.close();
		}
		catch (ConnectException e)
		{
			System.out.println("Local server is down");
			e.printStackTrace();
		}
		catch (Exception e) {e.printStackTrace();}
	}
	
	private static List<Integer> processResponse(Scanner console, List<String> responseLines)
	{
		// variables
		//1
		List<String> hrefMatches = new ArrayList<String>();
		
		//2
		int hrefLines = 0;		
		
		//3
		boolean isHeadDone = false;
		boolean inHead = false;
		int headLines = 0;
		
		//4
		// total lines is: response.size();
		
		
		// processing
		String responseline;
		for (int i = 0; i < responseLines.size(); i++)
		{
			responseline = responseLines.get(i);
			
			//1 
			if(responseline.indexOf("href=") != -1)
			{
				 Matcher hrefMatcher = Pattern.compile("href=\"([^\"]*)\"?").matcher(responseline); // regex get contents in href tag and put in group 1
				 while (hrefMatcher.find()) 
					 hrefMatches.add(hrefMatcher.group(1)); 
			}
			
			//2 href lines
			if(responseline.contains("href=")) // count only 1 per line
			{
				hrefLines++;
			}
			
			//3 between head
			if(!isHeadDone) // allows for less processing once the header is completed
			{
				if(inHead)
				{
					headLines++;
					if(responseline.toLowerCase().contains("</head>"))
					{
						//inHead = false; // not in head but this is not needed
						isHeadDone = true;
					}
				}
				else if(responseline.toLowerCase().contains("<head>"))
				{
					inHead = true;
				}
			}
		}
		
		//1 print processed results
		System.out.println("c.1=");
		for (String match : hrefMatches)
			System.out.printf("    %s\n", match);
		System.out.println();
		
		//2
		System.out.printf("c.2=%d\n", hrefLines);
		System.out.println();
		
		//3
		System.out.printf("c.3=%d\n", headLines);
		System.out.println();
		
		//4
		System.out.printf("c.4=%d\n", responseLines.size());
		System.out.println();
		
		// load return
		return Arrays.asList(hrefLines, headLines, responseLines.size());
	}
	
	public static int getRangedInteger(Scanner console,String userEnteredString, String userNotInRange, int min,int max)
	{
		int number; boolean rangeLoop;
		do
		{
			rangeLoop = true;
			number = getInteger(console, userEnteredString);
			if(number > min && number < max)
				rangeLoop = false;
			else
				System.out.print(userNotInRange);
		}while(rangeLoop);// if port is not between 0 - _MAX_PORT retry
		return number;
	}
	
	public static int getInteger(Scanner console,String userEnteredString)
	{
		while(!console.hasNextInt()) // integer input validation
		{
			console.nextLine(); // flush current
			System.out.print(userEnteredString);
		}
		return console.nextInt();
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
	
	public static List<String> getHttpResponseLines(String host,int port,String... headers)
	{
		final String httpBase = "GET / HTTP/1.0"; // using http protocol 1.0
		final String delimeter = "\r\n";
		
		List<String> textFromServer = new ArrayList<String>();
		
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

		    outToServer.close();
		    inFromServer.close();
		    clientSocket.close();
		    return textFromServer;
		}
		catch (Exception e) {e.printStackTrace();}
		
		return null; // there was an error
	}
}
