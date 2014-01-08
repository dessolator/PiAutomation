package gpioClient;


import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import static gpioCommon.NetConstants.FLIP;
import static gpioCommon.NetConstants.SERVERTCP;


public class Client {

	public static void main(String[] args) {
		try {
			InetAddress sendAddress;
			sendAddress = InetAddress.getByName("192.168.1.3");//TODO DO NOT HARDCODE STUFF
			Socket mySocket= new Socket(sendAddress,SERVERTCP);//open me a socket;//the socket for this class
			DataOutputStream outToServer = new DataOutputStream(mySocket.getOutputStream());
//			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			outToServer.writeBytes(FLIP+'\n');
			mySocket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();//no host found
		} catch (SocketException e) {
			e.printStackTrace();//socket broke
		} catch (IOException e) {
			e.printStackTrace();//send broke
		}
	}

}
