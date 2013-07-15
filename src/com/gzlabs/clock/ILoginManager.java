package com.gzlabs.clock;

import java.util.ArrayList;

/**
 * Provides management interface for the login form
 * @author apavlune
 *
 */
public interface ILoginManager {

	/**
	 * Processes login event
	 * @param username Username
	 * @param password Password
	 */
	void processLogIn(String username, String password);
	
	/**
	 * Pulls a list of employees
	 * @return Employees list
	 */
	ArrayList<String> getNames();
	
	void processClockEvent(String name, boolean isClockIn);

}
