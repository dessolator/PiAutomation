package gpioServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static gpioCommon.NetConstants.SERVERTCP;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinMode;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Server {
	public static GpioController gpio;//GPIO allocator
	public static GpioPinDigitalOutput pin1;//TODO need to change to arraylist
	public static GpioPinDigitalInput myButton;//TODO need to change to arraylist
	static ServerSocket mySocket;//acceptor socket
	
	/**
	 * Function used to trigger an output pin in a synchronized manner.
	 * @param i The pin to trigger.
	 */
	public static synchronized void synchronizedToggle(int i){//TODO need to make this more concurrent.
		getDigitalOutputPinByNumber(i).toggle();//toggle the switch in a synchronized manner
	}
	
	
	
	/**
	 * Function used to get the DigitalOutputPin by it's number.
	 * @param i Number of the pin to get.
	 * @return The DigitalOutputPin object.
	 */
	private static GpioPinDigitalOutput getDigitalOutputPinByNumber(int i) {
		for(GpioPin p : gpio.getProvisionedPins()){//for each pin in the provisioned pins
			if(p.getName().equals("PIN_"+i)){//check if it's name matches
				if(p.isMode(PinMode.DIGITAL_OUTPUT))//and if it's a digital output pin
					return (GpioPinDigitalOutput) p;//if so return it
			}
		}
		return null;
	}



	public static void main(String[] args) {
		gpio = GpioFactory.getInstance();
		//TODO read pins from file
		//TODO map switch pins to relay pins and attach sensible listeners
		pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "PIN_1", PinState.HIGH);//TODO HARDCODED make pin 1 an output pin and set it to HIGH
		myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);//TODO HARDCODED make pin2 an input pin and enable pulldown resistor to avoid floating value pin
		myButton.addListener(new ButtonListener(1));//attach listener to button
		
		try {
			mySocket=new ServerSocket(SERVERTCP);//open the acceptor socket
			while(true){
				Socket accepted=mySocket.accept();//open a new socket for the new connection
				NetListener myNetListener= new NetListener(accepted);//attach a listener to the new socket
				myNetListener.start();//start the socket listener
			}
			
		} catch (IOException e) {
			e.printStackTrace();//if opening a server socket fails or the socket gets broken.
			try {
				mySocket.close();//attempt to close the socket.
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
