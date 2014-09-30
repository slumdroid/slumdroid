package it.slumdroid.utilities.module;

import java.io.BufferedReader;
import java.io.FileReader;

public class TrasformActivity {

	public void traslate(String path, String output) {

		String xmlHead = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		StringBuilder builder = new StringBuilder();
		builder.append(xmlHead + "<ACTIVITY_STATES>");
		try{
			BufferedReader inputStream1 = new BufferedReader (new FileReader (path));
			for (;;) {
				String line = inputStream1.readLine();
				if (line==null){
					builder.append("</ACTIVITY_STATES>");
					inputStream1.close();
					new Tools().xmlWriter(path, builder, output);
					return;
				}
				else 
					builder.append(line.replace(xmlHead, "")
							.replace("START_STATE", "ACTIVITY_STATE")
							.replace("FINAL_STATE", "ACTIVITY_STATE"));	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
