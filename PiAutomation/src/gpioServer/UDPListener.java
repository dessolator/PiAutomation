package gpioServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import com.pi4j.io.gpio.PinState;
import static gpioCommon.NetConstants.FLIP;
import static gpioCommon.NetConstants.STATUS;
import static gpioCommon.NetConstants.HIGH;
import static gpioCommon.NetConstants.LOW;
import static gpioCommon.NetConstants.CLIENTUDP;
import static gpioCommon.NetConstants.SERVERUDP;
//TODO SWITCH TO TCP
public class UDPListener extends Thread {
	DatagramSocket mySocket;//the socket for this class
	byte[] receiveData= new byte[1024];//the receive buffer
	byte[] sendData= new byte[1024];//the send buffer
	
	
	/**
	 * Constructor for UDPListener opens a UDP(?) socket on port 55000
	 */
	public UDPListener() {
		super();
		try {
			mySocket = new DatagramSocket(SERVERUDP);//open me a socket
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		 while(true){//TODO well, basically run untill interrupted, should be handled a little more gracefully
//			 receiveData = new byte[1024];//TODO might need to be reallocated each time make a new receive buffer
			 DatagramPacket receivePacket=new DatagramPacket(receiveData,receiveData.length);//set the packet to be received
			 try {
				mySocket.receive(receivePacket);//listen for packet, blocking until one arrives
			} catch (IOException e) {
				e.printStackTrace();
			}
			 String sentence= new String(receivePacket.getData());//parse message
			 System.out.println(sentence);
			 if(sentence.trim().equals(FLIP)){//if the pin is to be flipped
				 Server.synchronizedToggle(Server.pin1);//flip the pin//TODO concurrency is horrid
			 }
			 if(sentence.trim().equals(STATUS)){//if the status was queried
//				 sendData=new byte[1024];//TODO might need to be reallocated each time make a new send buffer
				 if(Server.pin1.getState().equals(PinState.HIGH)){//if the pin status is high
					sendData=HIGH.getBytes();//make appropriate message
				 }
				 else{
					 sendData=LOW.getBytes();//make appropriate message
				 }
				 DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receivePacket.getAddress(), CLIENTUDP);//craft packet
				 try {
					mySocket.send(sendPacket);//send appropriate packet
				} catch (IOException e) {
					e.printStackTrace();
				}
				 
			 }
		 }
			 
		 
	}
	
}
