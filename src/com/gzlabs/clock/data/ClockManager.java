package com.gzlabs.clock.data;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.gzlabs.clock.ActionWidget;
import com.gzlabs.clock.ILoginManager;
import com.gzlabs.clock.LoginWidget;
import com.gzlabs.clock.RealTimeClockWidget;
import com.gzlabs.gzroster.gui.IDisplayStatus;

/**
 * Provides various management utiliies for the widgets
 * @author apavlune
 *
 */
public class ClockManager implements IDisplayStatus, ILoginManager
{

	private Label statusLabel;
	private DataManager dman;
	private RealTimeClockWidget rtclock;
	private LoginWidget loginWidget;
	private ActionWidget actionWidget;
	private Shell shell;
	private Display display;
	
	
	/**
	 * Default constructor, initializes member variables
	 * @param statusLabel Status display widget
	 */
	public ClockManager() {
		
		display = Display.getDefault();
		shell=new Shell(display);
		shell.setSize(525, 510);
		shell.setText("GZ Clock");
		
		statusLabel = new Label(shell, SWT.NONE);
		statusLabel.setBounds(10, 452, 504, 18);
		statusLabel.setText("Not connected to the database ...");		
		
		dman=new DataManager(this);
		rtclock = new RealTimeClockWidget(shell, SWT.NONE);
		rtclock.setBounds(370, 0, 150, 70);
		
		
		loginWidget = new LoginWidget(shell, SWT.NONE, this);
		loginWidget.setBounds(10, 0, 308, 441);
		
		actionWidget = new ActionWidget(shell, SWT.NONE, this);
		actionWidget.setSize(510, 389);
		actionWidget.setVisible(false);
		
		Button btnExit = new Button(shell, SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(loginWidget.isVisible())
				{
					System.exit(0);
				}
				else
				{
					loginWidget.setVisible(true);
					actionWidget.setVisible(false);
				}
			}
		});
		btnExit.setBounds(412, 410, 88, 30);
		btnExit.setText("Exit");
		
	}

	@Override
	public void DisplayStatus(String status) {
		if(statusLabel!=null)
		{
			statusLabel.setText(status);
		}
		
	}

	@Override
	public void ShowErrorBox(String error) {
		
		MessageDialog.openError(null, "Error!", error);
	}

	@Override
	public void processLogIn(String login, String text2) {
		if(login!=null && login.length()>0)
		{
			loginWidget.setVisible(false);
			loginWidget.clearSelections();
			actionWidget.setNameLabel(login);
			actionWidget.setVisible(true);
			actionWidget.populateTodaysSchedule(dman.getTodayShifts(login));
			actionWidget.setActionButton(dman.isClockedIn(login));
			double scheduled=dman.getTotalScheduledHours(login);
			actionWidget.setScheduledHours(String.format("%.2f", scheduled));
			double worked=dman.getWeeklyWorkedHours(login);
			actionWidget.setWorkedHours(String.format("%.2f", worked));
		}
		
	}

	@Override
	public ArrayList<String> getNames() {
		ArrayList<String> retval=null;
		if(dman!=null)
		{
			retval=dman.getEmployees();
		}
		return retval;
	}

	public void init() {
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
	}

	@Override
	public void processClockEvent(String name, boolean isClockIn) {
		boolean retval=false;
		if(dman!=null)
		{
			retval=dman.insertClockEvent(name, isClockIn);
			double worked=dman.getWeeklyWorkedHours(name);
			actionWidget.setWorkedHours(String.format("%.2f", worked));
		}
		
		if(retval && actionWidget!=null)
		{
			actionWidget.toggleActionButton();
		}
	}
	
}