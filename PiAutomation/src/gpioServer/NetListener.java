package gpioServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import static gpioCommon.NetConstants.FLIP;
import static gpioCommon.NetConstants.STATUS;
import static gpioCommon.NetConstants.HIGH;
import static gpioCommon.NetConstants.LOW;


public class NetListener extends Thread {
	Socket mySocket;//the socket for this class
	String sendData;//string used to send the query response
	DataInputStream inFromClient;
	DataOutputStream outToClient;
	
	
	/**
	 * Constructor for NetListener attaches the passed socket.
	 * @param accepted 
	 */
	public NetListener(Socket accepted) {
		super();
		mySocket = accepted;//link the socket.
		try {
			inFromClient =new DataInputStream(mySocket.getInputStream());//open input stream
			outToClient = new DataOutputStream(mySocket.getOutputStream());//open output stream
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		 while(true){
			try {
				 String sentence = inFromClient.readUTF();
				 sentence=sentence.trim();//rip off the leading and trailing '\0'
				 String [] parts=sentence.split("_");//split the command and the pin number
//				 System.out.println(parts[0]);//print out the received command
				 
				 if(parts[0].trim().equals(STATUS)){//if the status was queried
					 if(Server.getDigitalOutputPinByNumber(Integer.parseInt(parts[1])).getState().equals(PinState.HIGH)){//if the pin status is high
						sendData=HIGH;//TODO make appropriate message
					 }
					 else{
						 sendData=LOW;//TODO make appropriate message
					 }
					 outToClient.writeUTF(sendData);//send the appropriate response 
					 outToClient.flush();//flush the stream.
				 }
				 if(parts[0].trim().equals(FLIP)){//if the pin is to be flipped
					 Server.synchronizedToggle(Integer.parseInt(parts[1]));//flip the pin
				 }
				 if (parts[0].trim().equals("FULLSTATUS")) {
					 	System.out.println("RECEIVED REQUEST");
						sendData = "FULLSTATUSREPLY";
						for (int i = 0; i < Server.pins.length; i++) {
							GpioPinDigitalOutput mPin=Server.getDigitalOutputPinByNumber(i);
							if(mPin!=null){
							sendData = sendData + "_" + i + ',';
								if (mPin.getState().equals(PinState.HIGH)) {
									sendData = sendData + "1";
								} else {
									sendData = sendData + "0";
								}
							}

						}

						outToClient.writeUTF(sendData);
						outToClient.flush();
						System.out.println("REPLIED TO REQUEST");
						System.out.println(sendData);
				}
			} catch (IOException e) {
				break;
			}//read the command
		 }
			 
		 
	}

	
	/**
	 * The function sends the client a message of form "FLIPPED_#pinnumber_#pinstate
	 * @param i The pin whose status was changed
	 */
	public void notifyOfChange(int i) {
		String sendData="FLIPPED_"+i+"_"+Server.getPinStatusString(i);//TODO return STATUS NOT FLIP
		try {
			outToClient.writeUTF(sendData);//send the appropriate response
			outToClient.flush();//flush the stream.
		} catch (IOException e) {
			e.printStackTrace();
		} 
		 
		
	}
	
}
