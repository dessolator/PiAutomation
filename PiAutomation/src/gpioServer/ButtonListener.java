package gpioServer;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class ButtonListener implements GpioPinListenerDigital {

	int myPin;
	
	public ButtonListener(int myPin) {
		super();
		this.myPin = myPin;
	}

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		Server.synchronizedToggle(myPin);//TODO concurrency is horrid
	}

}
