package gpioServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static gpioCommon.NetConstants.SERVERTCP;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class Server {
	public static final GpioController gpio = GpioFactory.getInstance();//GPIO allocator
	public static final GpioPinDigitalOutput pin1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "MyRelay", PinState.HIGH);//make pin 1 an output pin and set it to HIGH
	public static final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);//make pin2 an input pin and enable pulldown resistor to avoid floating value pin
	static ServerSocket mySocket;
	
	/**
	 * Function used to trigger an output pin in a synchronized manner.
	 * @param pin The pin to trigger.
	 */
	public static synchronized void synchronizedToggle(GpioPinDigitalOutput pin){
		pin.toggle();//toggle the switch in a synchronized manner
		System.out.println("toggling");
	}
	
	
	
	public static void main(String[] args) {
		myButton.addListener(new ButtonListener());//attach listener to button
		
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
		
		/*
		what needs to happen here 
		is to wait for the trigger 
		value to change then kill 
		all the related threads and
		close down the daemon.
		*/
	}

}
