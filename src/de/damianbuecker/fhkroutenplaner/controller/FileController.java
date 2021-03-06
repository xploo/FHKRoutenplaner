package de.damianbuecker.fhkroutenplaner.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;

// TODO: Auto-generated Javadoc
/**
 * The Class FileController.
 */
public class FileController extends Controller {

	/**
	 * Instantiates a new file controller.
	 * 
	 * @param context
	 *            the context
	 */
	public FileController(Context context) {
		super(context);
	}

	/**
	 * Creates the html file.
	 *
	 * @param startFloor            the start floor
	 * @param endFloor            the end floor
	 * @param startID the start id
	 * @param endID the end id
	 * @return the file
	 */
	public File createHTMLFile(Integer startFloor, Integer endFloor, Integer startID, Integer endID) {
		StringBuffer buf = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.getContext().getAssets().open("index.html")));
			String line;

			while ((line = br.readLine()) != null) {
				if (line.contains("###Marker1")) {
					line = line.replace("###Marker1", "file:///" + Environment.getExternalStorageDirectory() + "/FMS/TestIMG-" + startFloor
							+ startID + ".png");
				} else if (line.contains("###Etage1marker")) {
					line = line.replace("###Etage1marker", "Startetage : " + startFloor + "");

				} else if (line.contains("###Marker2")) {
					line = line.replace("###Marker2", "file:///" + Environment.getExternalStorageDirectory() + "/FMS/TestIMG-" + endFloor
							+ endID + ".png");
				} else if (line.contains("###Etage2marker")) {
					line = line.replace("###Etage2marker", "Zieletage : " + endFloor + "");
				}
				buf.append(line);
				buf.append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		File directory = Environment.getExternalStorageDirectory();
		File file = new File(directory, "index.html");
		try {
			if (file.exists()) {
				file.delete();
				file.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileOutputStream outputStream;

		try {
			outputStream = new FileOutputStream(file);
			byte[] data = buf.toString().getBytes();
			outputStream.write(data);
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
}
