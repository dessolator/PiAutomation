package gpioServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import com.pi4j.io.gpio.Pin;
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
	static ServerSocket mySocket;//acceptor socket
	static Pin [] pins={
			RaspiPin.GPIO_00,RaspiPin.GPIO_01,
			RaspiPin.GPIO_02,RaspiPin.GPIO_03,
			RaspiPin.GPIO_04,RaspiPin.GPIO_05,
			RaspiPin.GPIO_06,RaspiPin.GPIO_07,
			RaspiPin.GPIO_08,RaspiPin.GPIO_09,
			RaspiPin.GPIO_10,RaspiPin.GPIO_11,
			RaspiPin.GPIO_12,RaspiPin.GPIO_13,
			RaspiPin.GPIO_14,RaspiPin.GPIO_15,
			RaspiPin.GPIO_16,RaspiPin.GPIO_17,
			RaspiPin.GPIO_18,RaspiPin.GPIO_19,
			RaspiPin.GPIO_20};
	
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
	public static GpioPinDigitalOutput getDigitalOutputPinByNumber(int i) {
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
		PinParser.parseFile("SwitchingLayout.ini").provision();//TODO read pins from file
		//TODO map switch pins to relay pins and attach sensible listeners
//		provisionOutputPin(1);
//		GpioPinDigitalInput tempButton =provisionInputPin(2);
//		tempButton.addListener(new ButtonListener(1));//attach listener to button
		
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



	public static GpioPinDigitalInput provisionInputPin(int i) {
//		System.out.println("provisioning "+i+" for input");
		return gpio.provisionDigitalInputPin(pins[i],("PIN_"+i), PinPullResistance.PULL_DOWN);//provision an input pin and enable pulldown resistor to avoid floating value pin
	}



	public static GpioPinDigitalOutput provisionOutputPin(int i) {
//		System.out.println("provisioning "+i+" for output");
		return gpio.provisionDigitalOutputPin(pins[i], ("PIN_"+i), PinState.HIGH);//provision an output pin and set it to HIGH
		
	}

}
