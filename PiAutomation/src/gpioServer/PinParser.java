package gpioServer;

import gpioCommon.PinLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PinParser {
	
	/**
	 * Function used to parse a PinLayout from a file.
	 * @param filePath The path to the file containing the pin layout.
	 * @return The PinLayout object.
	 */
	public static PinLayout parseFile(String filePath){
		PinLayout pins=new PinLayout();//storage for read pins
		try {
			Scanner scanner=new Scanner(new File(filePath));
			while(scanner.hasNext()){//while not EOF
				String pinDef=scanner.nextLine();//grab a line
				String[] parts=pinDef.split(" ");//split it by spaces
				if(parts[0].equals("IN")){//if an input pin was parsed
					pins.addInputPin(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));//add it to pins
				}
				if(parts[0].equals("OUT")){//if an output pin was parsed
					pins.addOutputPin(Integer.parseInt(parts[1]));//add it to pins
				}
			}
			scanner.close();//close the file
			return pins;//return pins
		} catch (FileNotFoundException e) {
			e.printStackTrace();//if there was a problem opening the file
		}
		return null;
	}
	

}
