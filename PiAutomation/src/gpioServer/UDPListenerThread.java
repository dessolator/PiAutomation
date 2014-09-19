package gpioServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UDPListenerThread extends Thread {
	private static final String SENDDATA = "SERVER_SEARCH_REPLY_";
	private static final int RECEIVEPORT = 55001;
	private static final int SENDPORT = 55001;
	private volatile boolean kill;
	private DatagramSocket mDatagramSocket;
	
	@Override
	public void run() {
		super.run();
		try {
			mDatagramSocket=new DatagramSocket(SENDPORT);
			while(!kill){
				DatagramPacket receivePacket;
				byte[] buf = new byte[1024];
				receivePacket = new DatagramPacket(buf, buf.length);
				mDatagramSocket.receive(receivePacket);
				String data = new String(buf);
				String[] splitData = data.split("_");
				System.out.println(data);
				System.out.println(splitData[0]);
				System.out.println(splitData[1]);
				boolean cond = (splitData[0].equals("SERVER") && splitData[1].equals("SEARCH"));
				if (cond) {
					DatagramPacket sendPacket = new DatagramPacket((SENDDATA+55000+"_").getBytes(), (SENDDATA+55000+"_").length(),
							receivePacket.getAddress(), RECEIVEPORT);
					System.out.println((SENDDATA+55000+"_"));
					mDatagramSocket.send(sendPacket);
				}
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
