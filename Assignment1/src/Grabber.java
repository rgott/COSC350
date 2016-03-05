
/*
 *
 * A free Java sample program 
 * to GET content from any URL using SOCKET connection
 *
 * @author William Alexander
 * free for use as long as this comment is included 
 * in the program as it is
 * 
 * More Free Java programs available for download 
 * at http://www.java-samples.com
 *
 */
import java.net.*; 
import java.io.*; 
import java.util.*;
public class Grabber { 

public static void main(String[] args) throws Exception { 
try { 

Socket socket = new Socket("136.160.171.19",80); 

PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))); 
out.println("GET / HTTP/1.0"); 
out.println(); 
out.flush(); 

BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 

String inputLine; 
int count = 0; 

while ((inputLine = in.readLine()) != null) { 
count++; 
System.out.println(count); 
System.out.println(inputLine); 
} 

in.close(); 
System.out.println("PRINTING HERE!!!"); 
} catch (Exception e) { 
e.printStackTrace(); 
} 
} 
}