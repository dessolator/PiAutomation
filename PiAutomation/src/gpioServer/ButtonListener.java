package gpioServer;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

public class ButtonListener implements GpioPinListenerDigital {

	@Override
	public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
		Server.synchronizedToggle(Server.pin1);//TODO concurrency is horrid
	}

}
