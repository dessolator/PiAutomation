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
	public static GpioPinDigitalOutput pin1;
	public static GpioPinDigitalInput myButton;
	static ServerSocket mySocket;
	
	/**
	 * Function used to trigger an output pin in a synchronized manner.
	 * @param i The pin to trigger.
	 */
	public static synchronized void synchronizedToggle(int i){
		getDigitalOutputPinByNumber(i).toggle();//toggle the switch in a synchronized manner
		System.out.println("toggling");
	}
	
	
	
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
			mySocket=new ServerSocket(SERVERTCP);
			while(true){
				Socket accepted=mySocket.accept();
				NetListener myNetListener= new NetListener(accepted);//attach a listener to socket
				myNetListener.start();//start the socket listener
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			try {
				mySocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
