package gpioCommon;

import gpioServer.ButtonListener;
import gpioServer.Server;

import java.util.ArrayList;

public class PinLayout {
	ArrayList<InputPinProto> inPins=new ArrayList<InputPinProto>();
	ArrayList<OutputPinProto> outPins=new ArrayList<OutputPinProto>();

	public void addInputPin(int inputPinNumber, int switchingPinNumber) {
		inPins.add(new InputPinProto(inputPinNumber,switchingPinNumber));
		
	}

	public void addOutputPin(int outputPinNumber) {
		outPins.add(new OutputPinProto(outputPinNumber));
		
	}

	public void provision() {
		for(OutputPinProto o:outPins){
			Server.provisionOutputPin(o.pinNumber);
		}
		for(InputPinProto i:inPins){
			Server.provisionInputPin(i.inputPinNumber).addListener(new ButtonListener(i.switchingPinNumber));
		}
		
	}

}
