import java.net.*;
import java.io.*;
import java.util.Scanner; 

public class Server {
	

	public static void main(String[] args) throws IOException{
		int c1, c2, c3; 
		
		ServerSocket s1=new ServerSocket(21252); 
		Socket ss=s1.accept();
		//Scanner input = new Scanner(ss.getInputStream()); 
		BufferedReader in=new BufferedReader(new InputStreamReader(ss.getInputStream()));
		
		
		c1=in.read();
		c2=in.read();  
		c3=in.read();			
		in.close();
		
		//PrintStream p=new PrintStream(ss.getOutputStream()); 
		System.out.println("Port connected to is"+ss.getPort());
		System.out.println("C.1= "+c1);
		System.out.println(); 
		System.out.println("C.2= "+c2);
		System.out.println(); 
		System.out.println("C.3= "+c3);
			
		s1.close();
			
		
			 
	}
}
