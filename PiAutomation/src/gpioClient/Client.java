package gpioClient;


import gpioCommon.RelayStatus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;

import static gpioCommon.NetConstants.HIGH;
import static gpioCommon.NetConstants.STATUS;
import static gpioCommon.NetConstants.FLIP;
import static gpioCommon.NetConstants.SERVERTCP;





//TODO implement broadcaster.
//TODO implement house layout read.
//TODO implement some means of authentication.
public class Client {
	static InetAddress sendAddress;//Host IP address
	static Socket mySocket;//TCP Socket for communication with the host
	static DataOutputStream outToServer;//output stream to server
	static DataInputStream inFromServer;//input stream from server
	static JFrame frame;//UIFrame for the client
	static{
		try {
			sendAddress= InetAddress.getByName("192.168.1.101");//TODO HARDCODED initialize the server address
			mySocket= new Socket(sendAddress,SERVERTCP);//initialize the server socket
			outToServer = new DataOutputStream(mySocket.getOutputStream());//initialize output stream to server
			inFromServer = new DataInputStream(mySocket.getInputStream());//initialize input stream from server
		} catch (UnknownHostException e) {//if the host is not reachable
			e.printStackTrace();
		} catch (IOException e) {//if socket is closed or otherwise broken
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
			makeUI();//TODO HARDCODED start the measly GraphicUserInterface 
	}
	
	
	/**
	 * Function used to flip a DigitalOutputPin on the server.
	 * @param i The pin number to be flipped.
	 * @throws IOException Thrown if the socket streams were closed or broken.
	 */
	static void flip(int i) throws IOException{
		outToServer.writeUTF(FLIP+'_'+i);//send the FLIP command to the server
		outToServer.flush();//flush the stream
	}
	
	/**
	 * Function used to query the status of a DigitalOutputPin on the server.
	 * @param i The pin number for the status check.
	 * @return The status of the relay attached to the given pin.
	 * @throws IOException Thrown if the socket streams were closed or broken.
	 */
	static RelayStatus query(int i) throws IOException{
		outToServer.writeUTF(STATUS+'_'+i);//send the STATUS command to the server
		outToServer.flush();//flush the stream
		String sentence = inFromServer.readUTF();//read status response from the server
		if(sentence.trim().equals(HIGH)){//if the return value was HIGH
			 return RelayStatus.OFF;//NOTE:RELAY LOGIC IS INVERTED
		}
		return RelayStatus.ON;
		
	}
	/**
	 * Function used to generate the pathetic pin control UI.
	 */
	private static void makeUI(){
		frame = new JFrame("Switch Control");//name the window
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//if x is hit kill the window
		UIButtonPanel p=new UIButtonPanel();//make a new button panel
		p.setOpaque(true);//set the button panel to be visible
		frame.setContentPane(p);//add the button panel to the frame
		frame.pack();//pack the button panel into the frame
		frame.setVisible(true);//set the frame to be visible
	}


	/**
	 * Function used to clean up the UI and the associated socket.
	 */
	public static void quit() {
		try {
			mySocket.close();//close the socket
			
		} catch (IOException e) {//if the socket was already closed or broken in some other way
			e.printStackTrace();
		}
		frame.dispose();//dispose of the frame
		
	}
	
	

}
