package gpioServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import com.pi4j.io.gpio.PinState;
import static gpioCommon.NetConstants.FLIP;
import static gpioCommon.NetConstants.STATUS;
import static gpioCommon.NetConstants.HIGH;
import static gpioCommon.NetConstants.LOW;


public class NetListener extends Thread {
	Socket mySocket;//the socket for this class
	String sendData;
	
	
	/**
	 * Constructor for UDPListener opens a UDP(?) socket on port 55000
	 * @param accepted 
	 */
	public NetListener(Socket accepted) {
		super();
		mySocket = accepted;
	}
	
	@Override
	public void run() {
//		 while(true){//TODO well, basically run untill interrupted, should be handled a little more gracefully
			try {
				 BufferedReader inFromClient =new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
				 DataOutputStream outToClient = new DataOutputStream(mySocket.getOutputStream());
				 String sentence;
				 sentence = inFromClient.readLine();
				 System.out.println(sentence);
				 if(sentence.trim().equals(FLIP)){//if the pin is to be flipped
					 Server.synchronizedToggle(Server.pin1);//flip the pin//TODO concurrency is horrid
				 }
				 if(sentence.trim().equals(STATUS)){//if the status was queried
					 if(Server.pin1.getState().equals(PinState.HIGH)){//if the pin status is high
						sendData=HIGH;//make appropriate message
					 }
					 else{
						 sendData=LOW;//make appropriate message
					 }
					 outToClient.writeBytes(sendData+'\n');
				 }
				 mySocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		 }
			 
		 
//	}
	
}
