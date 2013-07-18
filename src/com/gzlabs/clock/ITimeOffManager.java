package com.gzlabs.clock;

import java.util.ArrayList;

import com.gzlabs.gzroster.data.TimeOff;

public interface ITimeOffManager {
	
	public ArrayList<Object> getTimeSpans();

	public void addTimeOffRequest(String string, String string2, String m_name);

	public ArrayList<TimeOff> getTimeOffs(String m_name);

}
