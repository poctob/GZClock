package com.gzlabs.clock.data;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.gzlabs.clock.IEmployeeDataManager;
import com.gzlabs.clock.ILoginManager;
import com.gzlabs.clock.ITimeOffManager;
import com.gzlabs.clock.gui.ActionWidget;
import com.gzlabs.clock.gui.LoginWidget;
import com.gzlabs.clock.gui.RealTimeClockWidget;
import com.gzlabs.clock.gui.SupervisorApprovalDialog;
import com.gzlabs.gzroster.data.TimeOff;
import com.gzlabs.gzroster.gui.IConnectionStatus;
import com.gzlabs.gzroster.gui.IDisplayStatus;
import com.gzlabs.gzroster.gui.SplashShell;
import com.gzlabs.gzroster.sql.DBObjectType;
import com.gzlabs.gzroster.data.DataManager;
import com.gzlabs.gzroster.data.Person;
import com.gzlabs.gzroster.data.UploadManager;

/**
 * Provides various management utiliies for the widgets
 * @author apavlune
 *
 */
public class ClockManager implements IDisplayStatus, ILoginManager, ITimeOffManager, IEmployeeDataManager, IConnectionStatus
{

	private Label statusLabel;
	private DataManager dman;
	private RealTimeClockWidget rtclock;
	private LoginWidget loginWidget;
	private ActionWidget actionWidget;
	private Shell shell;
	private Display display;
	
	private volatile boolean isInitialized;
	private volatile boolean isError;
	
	
	/**
	 * Default constructor, initializes member variables
	 * @param statusLabel Status display widget
	 */
	public ClockManager() {
		
		Display display = Display.getDefault();
		isInitialized=false;	
		isError=false;
		
		dman = new DataManager(this, this, "GZClock.config");
		Thread worker=new Thread(dman);
		worker.setName("DataManager");
		worker.start();
		
		SplashShell splash=new SplashShell(display);
		splash.open();
		splash.layout();
		while (!isInitialized) {
			if (!display.readAndDispatch()) {

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		if(isError)
		{
			splash.activateError();
			while (!splash.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		}
		else
		{
			splash.close();		
			init_shell();
		}
		
		
	}
	
	private void init_shell()
	{
		display = Display.getDefault();
		shell=new Shell(display);
		shell.setSize(525, 510);
		shell.setText("GZ Clock");
		shell.setBackground(SWTResourceManager.getColor(153, 204, 153));
		
		statusLabel = new Label(shell, SWT.NONE);
		statusLabel.setBounds(10, 452, 504, 18);
		statusLabel.setBackground(SWTResourceManager.getColor(153, 204, 153));
		
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
			if(!dman.pinCorrect(login, text2))
			{
				MessageDialog.openError(shell, "Login Error", "Incorrect Pin!");
				loginWidget.clearSelections();
				return;
			}
			
			loginWidget.setVisible(false);
			loginWidget.clearSelections();
			actionWidget.setNameLabel(login, this);
			actionWidget.setVisible(true);
			actionWidget.populateTodaysSchedule(dman.getTodayShifts(login));
			actionWidget.setActionButton(dman.isClockedIn(login));
			double scheduled=dman.getTotalScheduledHours(login);
			actionWidget.setScheduledHours(String.format("%.2f", scheduled));
			double worked=dman.getWeeklyWorkedHours(login);
			actionWidget.setWorkedHours(String.format("%.2f", worked));
			
			Person employee=(Person)dman.getObjectByName(login, DBObjectType.PERSON);
			
			actionWidget.initEmployeeDataDialog
			(text2, 
					this, 
					employee.getM_address(), 
					employee.getM_home_phone(), 
					employee.getM_mobile_phone(), 
					employee.getM_email()
			);
		}
		
	}

	@Override
	public ArrayList<String> getNames() {
		ArrayList<String> retval=null;
		if(dman!=null)
		{
			retval=dman.getActiveEmployees();
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
	public void processClockEvent(String name, boolean isClockIn, String reason) {
		boolean retval=false;
		if(dman!=null)
		{
			String approver=null;
			if(isClockIn && dman.checkFiveMinuteRule(name))
			{
				SupervisorApprovalDialog approvalDialog=new SupervisorApprovalDialog(new Shell(), dman.getSupervisors());
				approvalDialog.open();
				approver=approvalDialog.getApprover();
				if(approver==null || approver.length()==0)
				{
					return;
				}
			}
			retval=dman.insertClockEvent(name, isClockIn, reason, approver);
			double worked=dman.getWeeklyWorkedHours(name);
			actionWidget.setWorkedHours(String.format("%.2f", worked));
		}
		
		if(retval && actionWidget!=null)
		{
			actionWidget.toggleActionButton();
		}
	}

	@Override
	public ArrayList<Object> getTimeSpans() {
		if(dman!=null)
		{
			return dman.getTimeSpan();
		}
		return null;
	}

	@Override
	public void addTimeOffRequest(String start, String end, String name) {
		if(dman!=null)
		{
			dman.requestTimeOff(name, start, end);
		}
		
	}

	@Override
	public ArrayList<TimeOff> getTimeOffs(String m_name) {
		if(dman!=null)
		{
			return dman.getTimeOffs(m_name);
		}
		return null;
	}

	@Override
	public void updateEmployeeData(String address, String homephone,
			String cellphone, String email, String pin) {
		dman.updateEmployeeData(actionWidget.getNameLabel(),
				address,
				homephone,
				cellphone,
				email,
				pin);
	}

	@Override
	public ArrayList<String> getClockOutReaons() {
		return dman.getClockOutReasons();
	}

	@Override
	public void setError() {
		isError=true;
		
	}

	@Override
	public void setInitialized() {
		isInitialized=true;
		
	}
	
}