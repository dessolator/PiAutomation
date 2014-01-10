package gpioServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import com.pi4j.io.gpio.PinState;
import static gpioCommon.NetConstants.FLIP;
import static gpioCommon.NetConstants.STATUS;
import static gpioCommon.NetConstants.HIGH;
import static gpioCommon.NetConstants.LOW;


public class NetListener extends Thread {
	Socket mySocket;//the socket for this class
	String sendData;//string used to send the query response
	
	
	/**
	 * Constructor for NetListener attaches the passed socket.
	 * @param accepted 
	 */
	public NetListener(Socket accepted) {
		super();
		mySocket = accepted;//link the socket.
	}
	
	@Override
	public void run() {
		 while(true){
			try {
				 DataInputStream inFromClient =new DataInputStream(mySocket.getInputStream());//open input stream
				 DataOutputStream outToClient = new DataOutputStream(mySocket.getOutputStream());//open output stream
				 String sentence = inFromClient.readUTF();//read the command
				 sentence=sentence.trim();//rip off the leading and trailing '\0'
				 String [] parts=sentence.split("_");//split the command and the pin number
//				 System.out.println(parts[0]);//print out the received command
				 
				 if(parts[0].trim().equals(STATUS)){//if the status was queried
					 if(Server.getDigitalOutputPinByNumber(Integer.parseInt(parts[1])).getState().equals(PinState.HIGH)){//if the pin status is high
						sendData=HIGH;//make appropriate message
					 }
					 else{
						 sendData=LOW;//make appropriate message
					 }
					 outToClient.writeUTF(sendData);//send the appropriate response 
					 outToClient.flush();//flush the stream.
				 }
				 if(parts[0].equals(FLIP)){//if the pin is to be flipped
					 Server.synchronizedToggle(Integer.parseInt(parts[1]));//flip the pin
				 }
			} catch (IOException e1) {
				break;//if the client closes the connection somewhat gracefully close the thread
			}
		 }
			 
		 
	}
	
}
