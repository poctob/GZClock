package com.gzlabs.clock.gui;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.gzlabs.clock.IEmployeeDataManager;
import com.gzlabs.clock.ILoginManager;
import com.gzlabs.clock.ITimeOffManager;
import com.gzlabs.utils.WidgetUtilities;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ActionWidget extends Composite {
	private Table table;
	private Label nameLabel; 
	private ILoginManager lman;
	private Label hoursScheduledLabel;
	private Label hoursWorkedLabel;
	private Button timeOff;	
	private Button actionButton; 
	private Button updateInfo;
	private TimeOffDialog tof;
	private SelectionAdapter toSelectionAdapter;
	private SelectionAdapter eiSelectionAdapter;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ActionWidget(Composite parent, int style, ILoginManager lm) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(153, 204, 153));
		this.lman=lm;
		actionButton = new Button(this, SWT.NONE);
		actionButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String name=nameLabel.getText();
				String event_text=actionButton.getText();
				ClockOutReasonDialog clockDialog=
						new ClockOutReasonDialog(getShell(), lman.getClockOutReaons());

				if(event_text.equals("Clock Out"))
				{
					clockDialog.open();
					if(clockDialog.getSelectedReason()!=null)
					{
						lman.processClockEvent(name, false, clockDialog.getSelectedReason());
					}
					return;
				}
				processClockIn();
			}
		});
		
		actionButton.setForeground(SWTResourceManager.getColor(SWT.COLOR_DARK_GREEN));
		actionButton.setFont(SWTResourceManager.getFont("Berlin Sans FB", 20, SWT.BOLD));
		actionButton.setBounds(10, 53, 224, 69);
		actionButton.setText("Clock In");
		
		timeOff = new Button(this, SWT.NONE);
	
		timeOff.setText("Time Off Request");
		timeOff.setForeground(SWTResourceManager.getColor(153, 50, 204));
		timeOff.setFont(SWTResourceManager.getFont("Berlin Sans FB", 16, SWT.BOLD));
		timeOff.setBounds(10, 158, 224, 69);
		
		updateInfo = new Button(this, SWT.NONE);
	
		updateInfo.setText("Update Profile");
		updateInfo.setForeground(SWTResourceManager.getColor(50, 205, 50));
		updateInfo.setFont(SWTResourceManager.getFont("Berlin Sans FB", 16, SWT.NORMAL));
		updateInfo.setBounds(10, 272, 224, 69);
		
		Group grpWeeklySummary = new Group(this, SWT.NONE);
		grpWeeklySummary.setBackground(SWTResourceManager.getColor(153, 204, 153));
		grpWeeklySummary.setFont(SWTResourceManager.getFont("Dingbats", 14, SWT.BOLD));
		grpWeeklySummary.setText("Weekly Summary");
		grpWeeklySummary.setBounds(275, 75, 236, 104);
		
		Label lblTotalHoursWorked = new Label(grpWeeklySummary, SWT.NONE);
		lblTotalHoursWorked.setBackground(SWTResourceManager.getColor(153, 204, 153));
		lblTotalHoursWorked.setBounds(10, 76, 141, 18);
		lblTotalHoursWorked.setText("Total Hours Worked: ");
		
		Label lblTotalHoursScheduled = new Label(grpWeeklySummary, SWT.NONE);
		lblTotalHoursScheduled.setBackground(SWTResourceManager.getColor(153, 204, 153));
		lblTotalHoursScheduled.setBounds(10, 35, 154, 18);
		lblTotalHoursScheduled.setText("Total Hours Scheduled: ");
		
		hoursScheduledLabel = new Label(grpWeeklySummary, SWT.NONE);
		hoursScheduledLabel.setBackground(SWTResourceManager.getColor(153, 204, 153));
		hoursScheduledLabel.setAlignment(SWT.RIGHT);
		hoursScheduledLabel.setBounds(170, 35, 56, 18);
		hoursScheduledLabel.setText("0.0");
		
		hoursWorkedLabel = new Label(grpWeeklySummary, SWT.NONE);
		hoursWorkedLabel.setBackground(SWTResourceManager.getColor(153, 204, 153));
		hoursWorkedLabel.setText("0.0");
		hoursWorkedLabel.setAlignment(SWT.RIGHT);
		hoursWorkedLabel.setBounds(170, 76, 56, 18);
		
		Label lblHello = new Label(this, SWT.NONE);
		lblHello.setBackground(SWTResourceManager.getColor(153, 204, 153));
		lblHello.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		lblHello.setBounds(10, 10, 58, 28);
		lblHello.setText("Hello");
		
		nameLabel = new Label(this, SWT.NONE);
		nameLabel.setBackground(SWTResourceManager.getColor(153, 204, 153));
		nameLabel.setText("Person");
		nameLabel.setFont(SWTResourceManager.getFont("Arial", 18, SWT.NORMAL));
		nameLabel.setBounds(74, 10, 251, 28);
		
		Group grpTodaysSchedule = new Group(this, SWT.NONE);
		grpTodaysSchedule.setBackground(SWTResourceManager.getColor(153, 204, 153));
		grpTodaysSchedule.setText("Today's Schedule");
		grpTodaysSchedule.setFont(SWTResourceManager.getFont("Dingbats", 14, SWT.BOLD));
		grpTodaysSchedule.setBounds(275, 185, 236, 168);
		
		table = new Table(grpTodaysSchedule, SWT.BORDER | SWT.FULL_SELECTION);
		table.setForeground(SWTResourceManager.getColor(SWT.COLOR_LIST_FOREGROUND));
		table.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		table.setBounds(0, 31, 236, 137);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnFrom = new TableColumn(table, SWT.CENTER);
		tblclmnFrom.setWidth(76);
		tblclmnFrom.setText("From");
		
		TableColumn tblclmnTo = new TableColumn(table, SWT.CENTER);
		tblclmnTo.setWidth(72);
		tblclmnTo.setText("To");
		
		TableColumn tblclmnPosition = new TableColumn(table, SWT.CENTER);
		tblclmnPosition.setWidth(70);
		tblclmnPosition.setText("Position");
		
	}
	private void processClockIn()
	{
		if(MessageDialog.openConfirm(getShell(), "Confirm", "Are You Sure?"))
		{
			lman.processClockEvent(nameLabel.getText(), true, null);
		}
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public void setNameLabel(final String name, final ITimeOffManager ito)
	{
		WidgetUtilities.safeLabelSet(nameLabel, name);	
		if(toSelectionAdapter!=null)
		{
			timeOff.removeSelectionListener(toSelectionAdapter);
		}
		toSelectionAdapter=new TimeOffSelectionAdapter(name, ito);
		timeOff.addSelectionListener(toSelectionAdapter);
	}
	
	public void initEmployeeDataDialog(final String pin, 
			final IEmployeeDataManager p_eman,
			final String address,
			final String hphone,
			final String cphone,
			final String email)
	{
		if(eiSelectionAdapter!=null)
		{
			updateInfo.removeSelectionListener(eiSelectionAdapter);
		}
		eiSelectionAdapter=new EmployeeDataSelectionAdapter(pin,
				p_eman,
				address,
				hphone,
				cphone,
				email);
		updateInfo.addSelectionListener(eiSelectionAdapter);		
	}
	
	public void populateTodaysSchedule(ArrayList<String> schedule)
	{
		table.removeAll();
		int step=table.getColumnCount();
		if(schedule !=null && schedule.size()%step==0)
		{
			for(int i=0; i<schedule.size(); i+=step)
			{
				TableItem ti = new TableItem(table, SWT.NONE);
				String [] cells=new String[step];
				for(int j=0; j<step; j++)
				{
					cells[j]=schedule.get(i+j);
				}
				ti.setText(cells);
			}
		}
		
	}

	public void toggleActionButton() {
		String event_text=actionButton.getText();
		if( event_text.equals("Clock In"))
		{
			WidgetUtilities.safeButtonSet(actionButton, "Clock Out");
		}
		else
		{
			WidgetUtilities.safeButtonSet(actionButton, "Clock In");
		}
	}
	
	public void setActionButton(boolean logedin)
	{
		WidgetUtilities.safeButtonSet(actionButton, logedin?"Clock Out":"Clock In");
	}
	
	public void setScheduledHours(String hours)
	{
		WidgetUtilities.safeLabelSet(hoursScheduledLabel, hours);
	}
	
	public void setWorkedHours(String hours)
	{
		WidgetUtilities.safeLabelSet(hoursWorkedLabel, hours);
	}

	public String getNameLabel() {
		return nameLabel.getText();
	}
	
	private class TimeOffSelectionAdapter extends SelectionAdapter
	{
		private String name;
		private ITimeOffManager ito;
		
		public TimeOffSelectionAdapter(String p_name, ITimeOffManager p_ito)
		{
			this.name=p_name;
			this.ito=p_ito;
		}
		@Override
		public void widgetSelected(SelectionEvent e) {
			if(tof==null)
			{
				tof=new TimeOffDialog(new Shell(),nameLabel.getText(),ito);
			}
			else
			{
				tof.setM_name(name);
				tof.setItoman(ito);
			}
			tof.open();
		}
	}
	
	private class EmployeeDataSelectionAdapter extends SelectionAdapter
	{
		private String pin; 
		private IEmployeeDataManager p_eman;
		private String address;
		private String hphone;
		private String cphone;
		private String email;
		
	
		public EmployeeDataSelectionAdapter(String pin,
				IEmployeeDataManager p_eman, String address, String hphone,
				String cphone, String email) {
			super();
			this.pin = pin;
			this.p_eman = p_eman;
			this.address = address;
			this.hphone = hphone;
			this.cphone = cphone;
			this.email = email;
		}


		public void widgetSelected(SelectionEvent e) {
			EmployeeInfoDialog tof=new EmployeeInfoDialog(new Shell(),p_eman, pin
			,address, hphone, cphone, email);
			tof.open();
		}
	}
}
