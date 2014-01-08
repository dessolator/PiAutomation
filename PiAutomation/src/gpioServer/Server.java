package gpioServer;

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
		UDPListener myUDPListener= new UDPListener();//attach a listener to socket
		myUDPListener.start();//start the socket listener
		while(true);//TODO implement a proper kill-switch //basically keep the program running forever
		/*
		what needs to happen here 
		is to wait for the trigger 
		value to change then kill 
		all the related threads and
		close down the daemon.
		*/
	}

}
