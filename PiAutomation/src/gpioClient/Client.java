package gpioClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import static gpioCommon.NetConstants.FLIP;
import static gpioCommon.NetConstants.SERVERUDP;
import static gpioCommon.NetConstants.CLIENTUDP;
//TODO SWITCH TO TCP
public class Client {

	public static void main(String[] args) {
		DatagramSocket mySocket;//the socket for this class
		byte[] sendData= new byte[1024];//the send buffer
		InetAddress sendAddress;
		try {
			sendData=FLIP.getBytes();
			mySocket = new DatagramSocket(CLIENTUDP);//open me a socket
			sendAddress = InetAddress.getByName("192.168.1.3");//TODO DO NOT HARDCODE STUFF
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, sendAddress, SERVERUDP);//craft packet
			mySocket.send(sendPacket);//send appropriate packet
		} catch (UnknownHostException e) {
			e.printStackTrace();//no host found
		} catch (SocketException e) {
			e.printStackTrace();//socket broke
		} catch (IOException e) {
			e.printStackTrace();//send broke
		}
	}

}
