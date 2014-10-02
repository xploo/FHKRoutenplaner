package de.damianbuecker.fhkroutenplaner.controller;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;
import android.util.Log;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.model.Model;

public class Controller {
	
	
	private DatabaseHelper mDatabaseHelper;
	private Context context;
	
	private static final Logger logger = LoggerFactory.getLogger(Model.class);
	
	public Controller(){}
	
	public Controller(Context context) {
		this.context = context;
		this.mDatabaseHelper = OpenHelperManager.getHelper(this.context, DatabaseHelper.class);
	}
	
	public DatabaseHelper getDatabaseHelper(Context context) {
		if(this.mDatabaseHelper == null) {
			this.mDatabaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		}
		return this.mDatabaseHelper;
	}
	
	public long getTime(){
		
		long time = System.currentTimeMillis();
		return time;
	}
	
	public String getRuntime(long startTime, long endTime){
		
		long runtime = endTime-startTime;
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
        //System.out.println(sdf.format(runtime));              
        return sdf.format(runtime).toString();
	}
	
	
	
	public Context getContext() {
		return this.context;
	}	
	
	/**
	 * Log.
	 *
	 * @param message the message
	 */
	public void log(String message) {
		logger.info(message);
	}
	
	
}
