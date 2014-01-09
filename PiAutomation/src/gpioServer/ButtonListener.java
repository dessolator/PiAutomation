package gpioServer;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class ButtonListener implements GpioPinListenerDigital {

	int myPin;//the number of the pin the switch is connected to.
	
	public ButtonListener(int myPin) {
		super();
		this.myPin = myPin;//set pin number.
	}

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		Server.synchronizedToggle(myPin);
	}

}
