package gpioCommon;

public class InputPinProto {
	int inputPinNumber;//number of the switch pin
	int switchingPinNumber;//number of the output pin
	
	/**
	 * Constructor for the InputPinProto class.
	 * @param inputPinNumber The number of the switch pin.
	 * @param switchingPinNumber The number of the output pin.
	 */
	public InputPinProto(int inputPinNumber, int switchingPinNumber) {
		super();
		this.inputPinNumber = inputPinNumber;
		this.switchingPinNumber = switchingPinNumber;
	}
	

}
