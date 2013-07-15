package com.gzlabs.clock.data;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import com.gzlabs.gzroster.data.DB_Object;
import com.gzlabs.gzroster.data.Duty;
import com.gzlabs.gzroster.gui.IDisplayStatus;
import com.gzlabs.gzroster.sql.DBManager;
import com.gzlabs.gzroster.sql.DB_Factory;
import com.gzlabs.gzroster.sql.DB_Factory.ObjectType;
import com.gzlabs.utils.DateUtils;

/**
 * Manages data.  Get data from the database and populates objects with it.
 * @author apavlune
 *
 */
public class DataManager {
	
	private static final String CONFIG_FILE_PATH = "GZClock.config";

	//Database manager.
	private DBManager dbman;
	
	//Application properties.
	private Properties prop = null;
	
	//Status display
	private IDisplayStatus ids = null;
	
	//DB Objects
	private ArrayList<DB_Object> db_persons;	
	
	private ArrayList<DB_Object> db_positions;
	/**
	 * Default constructor. Initializes member variables.
	 * @param pprop  Properties object.
	 * @param pids Status display
	 */
	public DataManager(IDisplayStatus pids) {

		
		ids = pids;
		getProp();
		
		if(prop != null && !prop.isEmpty())
		{
			safeDisplayStatus("Attempting to connect to the databse...");
	
			if (initDBMan()) {
				safeDisplayStatus("Connected to the Database!");				
				updateDBObjects();
				
			} else {
				safeDisplayStatus("Database connection failed. Exiting...");
				return;
			}
		}
		else
		{
			safeDisplayStatus("Empty config file. Unable to continiue...");
			return;
		}
	}
	
	/**
	 * Initialized database manager
	 * @return true if initialization was success, false otherwise
	 */
	private boolean initDBMan() {
		dbman = DBManager.getInstance(prop.getProperty("db_driver"),
				prop.getProperty("db_url"), prop.getProperty("db_username"),
				prop.getProperty("db_password"));
		return dbman.init();
	}
	
	/**
	 * Retrieves a list of employees stored in the database
	 * @return List of employees
	 */
	public ArrayList<String> getEmployees()
	{		
		return DB_Factory.getNames(db_persons);
	}
	

	/**
	 * Updates employee in the database.
	 * @param old_val Old values
	 * @param new_val New values
	 * @param position_boxes Positions list that this employee can take. 
	 */
	public void updateEmployee(ArrayList<String> old_val, ArrayList<String> new_val) {
		
		if(DB_Factory.updateRecord(db_persons, dbman, old_val, new_val))
		{			
			safeDisplayStatus("Record updated!");
			updateDBObjects();
		}
		else
		{
			safeDisplayStatus("Unable to update record!");
		}	
			
	}


	/**
	 * Fetches database data for local objects
	 */
	private void updateDBObjects()
	{
		db_positions=DB_Factory.getAllRecords(ObjectType.POSITION, dbman, false);	
		db_persons=DB_Factory.getAllRecords(ObjectType.PERSON, dbman, false);	
		
	}

	/**
	 * Creates a list of times between starting and ending times 
	 * from the configuration, using an interval from the configuration.
	 * @return List of time spans.
	 */
	public ArrayList<String> getTimeSpan()
	{
		return DateUtils.getTimeSpan
				(prop.getProperty("day_start"), prop.getProperty("day_end"), prop.getProperty("interval_minutes"));		
	}
	
	/**
	 * Loads properties file.
	 * 
	 * @param ids
	 *            Information display interface.
	 * @return null if something went wrong. Properties loaded from file
	 *         otherwise.
	 */
	public Properties getProp() {
		safeDisplayStatus("Loading configuration...");
		prop = null;
		try {
			prop = new Properties();
			prop.load(new FileInputStream(CONFIG_FILE_PATH));
		} catch (Exception e) {
			safeDisplayStatus("Unable to load configuration file!");
		}
		safeDisplayStatus("Done...");
		return prop;
	}

	/**
	 * Saves properties into a file.
	 * 
	 * @param prop
	 *            Properties to save.
	 * @param ids
	 *            Information display interface.
	 */
	public void saveProp(Properties pprop) {
		if(pprop == null)
		{
			safeDisplayStatus("Properties are empty. Not saving.");
			return;
		}
		prop = pprop;
		safeDisplayStatus("Saving configuration...");
		FileOutputStream fout = null;
		try {
			fout = new FileOutputStream(CONFIG_FILE_PATH);
			prop.store(fout, "Auto-Save "
					+ DateUtils.getCurrentDateString());
		} catch (IOException e) {
			safeDisplayStatus(e.getMessage());
		} finally {
			try {
				fout.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		safeDisplayStatus("Done...");
	}
	
	/**
	 * Safety function for status display
	 * @param status Message to display.
	 */
	private void safeDisplayStatus(String status)
	{
		if(ids!=null)
		{
			ids.DisplayStatus(status);
		}
	}
	
	public ArrayList<String> getTodayShifts(String name)
	{
		ArrayList<String> retval=new ArrayList<String>();
		ArrayList<DB_Object> duties=DB_Factory.getTodaysDuty(dbman, name, db_positions, db_persons);
		for(DB_Object d:duties)
		{
			if(d!=null)
			{
				Duty duty=(Duty)d;
				String start=DateUtils.DateToString(duty.getM_start());
				String end=DateUtils.DateToString(duty.getM_end());
				String position=duty.getM_position().getName();
				retval.add(start.substring(11, start.length()-5));
				retval.add(end.substring(11, end.length()-5));
				retval.add(position);
			}
				
		}
		return retval;
	}
	
	public boolean insertClockEvent(String name, boolean isClockIn)
	{
		if(DB_Factory.insertClockEvent(dbman, name, isClockIn))
		{
			safeDisplayStatus("Clock event registered.");
			return true;
		}
		else
		{
			safeDisplayStatus("Unable to register an event!");
			return false;
		}
	}
	
	public boolean isClockedIn(String name)
	{
		return DB_Factory.isClockedIn(dbman, name);
	}
	
	public double getTotalScheduledHours(String name)
	{
		double minutes=DB_Factory.getWeekScheduledHours(dbman, name);
		return minutes/60;
	}
	
	public double getWeeklyWorkedHours(String name)
	{
		double minutes=DB_Factory.getWeekWorkeddHours(dbman, name);
		return minutes/60;
	}

	
}
