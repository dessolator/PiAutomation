package gpioServer;

import gpioCommon.PinLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class PinParser {
	public static PinLayout parseFile(String filePath){
		File handle=new File(filePath);
		PinLayout pins=new PinLayout();
		try {
			Scanner scanner=new Scanner(handle);
			while(scanner.hasNext()){
				String pinDef=scanner.nextLine();
				String[] parts=pinDef.split(" ");
				if(parts[0].equals("IN")){
					pins.addInputPin(Integer.parseInt(parts[1]),Integer.parseInt(parts[2]));
				}
				if(parts[0].equals("OUT")){
					pins.addOutputPin(Integer.parseInt(parts[1]));
				}
			}
			scanner.close();
			return pins;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	

}
