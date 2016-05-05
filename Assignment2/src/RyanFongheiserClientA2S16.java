import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;
/**
 * NtpClient - an NTP client for Java.  This program connects to an NTP server
 * and prints the response to the console.
 * 
 * The local clock offset calculation is implemented according to the SNTP
 * algorithm specified in RFC 2030.  
 * 
 * Note that on windows platforms, the curent time-of-day timestamp is limited
 * to an resolution of 10ms and adversely affects the accuracy of the results.
 * 
 * 
 * This code is copyright (c) Adam Buckley 2004
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.  A HTML version of the GNU General Public License can be
 * seen at http://www.gnu.org/licenses/gpl.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *  
 * @author Adam Buckley
 * 
 * 
 */
public class RyanFongheiserClientA2S16 
{
	
	public static void main(String[] args) throws IOException
	{
		String serverName = "";
		int serverPort = 9876;
		
		Scanner console = new Scanner(System.in);
		
		System.out.print("Please enter a message: ");
		String inputMessage = console.nextLine();
		
		System.out.print("Please enter the number of packets: ");
		while(!console.hasNextInt())
		{
			System.out.print("Error - Please enter the number of packets: ");
			console.next(); // discard string
		}
		int packetsToSend = console.nextInt();
		if(packetsToSend == 0) packetsToSend = 1;
		
		// make all packets equal except first packet
		int tailPacketsToSend = inputMessage.length() / packetsToSend; // /////////////////////////////////
		int headPacketToSend = tailPacketsToSend + inputMessage.length() % packetsToSend;
		String[] packetStrings = split(inputMessage, packetsToSend, headPacketToSend, tailPacketsToSend);
		
		
		// Send request
		DatagramSocket socket = new DatagramSocket();
		InetAddress address = InetAddress.getByName(serverName);
		
		
		byte[] buf = packetStrings[0].getBytes();
		DatagramPacket headPacket = new DatagramPacket(buf, buf.length, address, serverPort);
		//NtpMessage.encodeTimestamp(headPacket.getData(), 40, (System.currentTimeMillis()/1000.0) + 2208988800.0);
		socket.send(headPacket);
		long TC1 = System.currentTimeMillis();
		
		DatagramPacket tailPacket;
		for (int i = 1; i < packetStrings.length; i++) // send rest of packets
		{
			//buf = new NtpMessage().toByteArray();
			buf = packetStrings[i].getBytes();
			tailPacket = new DatagramPacket(buf, buf.length, address, serverPort);
			//NtpMessage.encodeTimestamp(tailPacket.getData(), 40, (System.currentTimeMillis()/1000.0) + 2208988800.0);
			socket.send(tailPacket);
		}
		long TC2 = System.currentTimeMillis();
		
		// Get response
		System.out.println("NTP request sent, waiting for response...\n");
		buf = new byte[Integer.SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		long TC3 = System.currentTimeMillis();
		buf = new byte[packet.getLength()];
		System.arraycopy(packet.getData(), 0, buf, 0, packet.getLength());

		//TODO:get from server
		int D = Integer.parseInt(new String(buf));
		// Display response
		System.out.print("Time to send N packets: TC2-TC1=");
		System.out.println(TC2 - TC1);
		
		System.out.print("Round trip time: R=TC3-TC1=");
		System.out.println(TC3 - TC1);
		
		System.out.print("Network delay: D-R=");
		System.out.println(D - (TC3 - TC1));
		
		System.out.println("Please enter the name of the NTP server");
		
		System.out.println("NTP server: " + serverName);
		
		socket.close();
	}
	

	public static String[] split(String message,int packetsToSend, int first, int rest)
	{
		String[] splitMessage = new String[packetsToSend];
		splitMessage[0] = message.substring(0,first);
		int lastPos = first;
		for (int i = 1; i < splitMessage.length; i++) 
		{
			splitMessage[i] = message.substring(lastPos, lastPos + rest);
			lastPos += rest;
		}
		return splitMessage;
	}
}
