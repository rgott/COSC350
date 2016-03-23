import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket; 

public class Server {
	
	private static final int _PORT = 21252;
	public static void main(String[] args) throws IOException{
		int c2, c3, c4; 
		
		ServerSocket serverSocket =new ServerSocket(_PORT); 
		Socket clientConnection = serverSocket.accept();
		//Scanner input = new Scanner(ss.getInputStream()); 
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
		
		
		try // try to parse the integer received as string
		{
			c2 = Integer.parseInt(inFromClient.readLine().split("=")[1]);
			c3 = Integer.parseInt(inFromClient.readLine().split("=")[1]);  
			c4 = Integer.parseInt(inFromClient.readLine().split("=")[1]);			
		
			System.out.println("port=" + _PORT);// changed after wireshark test
			System.out.println("c.2=" + c2);
			System.out.println(); 
			System.out.println("c.3=" + c3);
			System.out.println(); 
			System.out.println("c.4=" + c4);
			
		}catch(NumberFormatException e)
		{
			System.out.println("Error: one or more of the inputs recieved has been corrupted or not an integer.");
			e.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e)
		{
			System.out.println("Error: bad input from client");
			e.printStackTrace();
		}
		inFromClient.close();
		serverSocket.close();
	}
}