package gpioClient;


import gpioCommon.RelayStatus;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import static gpioCommon.NetConstants.HIGH;
import static gpioCommon.NetConstants.STATUS;
import static gpioCommon.NetConstants.FLIP;
import static gpioCommon.NetConstants.SERVERTCP;


public class Client {
	static InetAddress sendAddress;
	static Socket mySocket;//open me a socket;//the socket for this class
	static DataOutputStream outToServer;
	static DataInputStream inFromServer;
	static{
		try {
			sendAddress= InetAddress.getByName("192.168.1.3");//TODO HARDCODED
			mySocket= new Socket(sendAddress,SERVERTCP);
			outToServer = new DataOutputStream(mySocket.getOutputStream());
			inFromServer = new DataInputStream(mySocket.getInputStream());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			if(query(1)==RelayStatus.ON){
				System.out.println("it's on");
			}
			else{
				System.out.println("it's off");
			}
			flip(1);
			if(query(1)==RelayStatus.ON){
				System.out.println("it's on");
			}
			else{
				System.out.println("it's off");
			}
			mySocket.close();
		} catch (IOException e) {
			e.printStackTrace();//send broke
		}
	}
	
	
	private static void flip(int i) throws IOException{
		outToServer.writeUTF(FLIP+'_'+i+'\n');
	}
	
	private static RelayStatus query(int i) throws IOException{
		outToServer.writeUTF(STATUS+'_'+i+'\n');
		outToServer.flush();
		String sentence = inFromServer.readUTF();
		if(sentence.trim().equals(HIGH)){//if the status was queried
			 return RelayStatus.OFF;//RELAY LOGIC IS INVERTED
		}
		return RelayStatus.ON;
		
	}
	
	

}
