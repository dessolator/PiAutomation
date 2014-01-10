package gpioServer;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class SwitchListener implements GpioPinListenerDigital {

	int myPin;//the number of the pin the switch is connected to.
	
	/**
	 * Constructor for the SwitchListener class.
	 * @param myPin The pin to be triggered.
	 */
	public SwitchListener(int myPin) {
		super();
		this.myPin = myPin;//set pin number.
	}

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		Server.synchronizedToggle(myPin);
	}

}
