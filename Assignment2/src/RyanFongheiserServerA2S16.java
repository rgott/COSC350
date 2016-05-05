import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class RyanFongheiserServerA2S16
{
	public static void main(String args[]) throws Exception
	{
		long TS1;
		DatagramSocket serverSocket = new DatagramSocket(9876);
		byte[] receiveData = new byte[1024];
		byte[] sendData;
		
		while(true) // wait for many clients
		{
			serverSocket.setSoTimeout(0);
			DatagramPacket receiveHeadPacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receiveHeadPacket); // wait infinite amount of time for first packet to arrive
			TS1 = System.currentTimeMillis();// packet received
			
			serverSocket.setSoTimeout(1000); // wait x ms for all subsequent packets
			while(true)
			{
				try
				{
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					serverSocket.receive(receivePacket);
				}catch(SocketTimeoutException e)
				{
					break; // if no more packets send back message
				}
				
			}
			long TS2 = System.currentTimeMillis();
			Long D = TS2 - TS1;
			
			
			sendData = Long.toString(D).getBytes();
			DatagramPacket senderPacket = new DatagramPacket(sendData, sendData.length, receiveHeadPacket.getAddress(), receiveHeadPacket.getPort());
			serverSocket.send(senderPacket);
		}
		
	}
}
