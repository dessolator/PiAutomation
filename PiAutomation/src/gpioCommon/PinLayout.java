package gpioCommon;

import gpioServer.ButtonListener;
import gpioServer.Server;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class PinLayout implements Serializable{
	ArrayList<InputPinProto> inPins=new ArrayList<InputPinProto>();//ArrayList of input pins
	ArrayList<OutputPinProto> outPins=new ArrayList<OutputPinProto>();//ArrayList of output pins

	/**
	 * Function used to add input pins to the pin layout.
	 * @param inputPinNumber The number of the switch pin.
	 * @param switchingPinNumber The number of the pin to be triggered.
	 */
	public void addInputPin(int inputPinNumber, int switchingPinNumber) {
		inPins.add(new InputPinProto(inputPinNumber,switchingPinNumber));
		
	}

	/**
	 * Function used to add output pins to the pin layout.
	 * @param outputPinNumber The number of the output pin.
	 */
	public void addOutputPin(int outputPinNumber) {
		outPins.add(new OutputPinProto(outputPinNumber));
		
	}

	/**
	 * Function used to provision the added pins.
	 */
	public void provision() {
		for(OutputPinProto o:outPins){
			Server.provisionOutputPin(o.pinNumber);
		}
		for(InputPinProto i:inPins){
			Server.provisionInputPin(i.inputPinNumber).addListener(new ButtonListener(i.switchingPinNumber));
		}
		
	}

}
