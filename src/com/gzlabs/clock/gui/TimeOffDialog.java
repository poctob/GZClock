package com.gzlabs.clock.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Combo;

import com.gzlabs.clock.ITimeOffManager;
import com.gzlabs.gzroster.data.TimeOff;
import com.gzlabs.utils.DateUtils;
import com.gzlabs.utils.WidgetUtilities;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class TimeOffDialog extends Dialog {
	private Table table;
	private ITimeOffManager itoman;
	
	private Combo toTimeCombo;
	private Combo fromTimeCombo;
	private Button fromAllDay;
	private Button toAllDay;
	private DateTime fromDateTime;
	private DateTime toDateTime;
	private TableColumn tblclmnTo;
	private String m_name;
	/**
	 * @param itoman the itoman to set
	 */
	public void setItoman(ITimeOffManager itoman) {
		this.itoman = itoman;
	}

	/**
	 * @param m_name the m_name to set
	 */
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public TimeOffDialog(Shell parentShell,String name, ITimeOffManager it) {
		super(parentShell);
		itoman=it;
		m_name=name;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 4;
		
		Label lblFrom = new Label(container, SWT.NONE);
		lblFrom.setText("From:");
		
		fromDateTime = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN);
		
		GridData gd_fromDateTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_fromDateTime.widthHint = 113;
		fromDateTime.setLayoutData(gd_fromDateTime);
		
		fromTimeCombo = new Combo(container, SWT.NONE);
		GridData gd_fromTimeCombo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_fromTimeCombo.widthHint = 89;
		fromTimeCombo.setLayoutData(gd_fromTimeCombo);
		
		fromAllDay = new Button(container, SWT.CHECK);
		fromAllDay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				fromTimeCombo.setEnabled(!fromAllDay.getSelection());
			}
		});
		fromAllDay.setText("All Day");
		
		Label lblTo = new Label(container, SWT.NONE);
		lblTo.setText("To:");
		
		toDateTime = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN);
		GridData gd_toDateTime = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_toDateTime.widthHint = 116;
		toDateTime.setLayoutData(gd_toDateTime);
		
		toTimeCombo = new Combo(container, SWT.NONE);
		toTimeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		toAllDay = new Button(container, SWT.CHECK);
		toAllDay.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toTimeCombo.setEnabled(!toAllDay.getSelection());
			}
		});
		toAllDay.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		toAllDay.setText("All Day");
		
		table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 2);
		gd_table.widthHint = 298;
		table.setLayoutData(gd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnDates = new TableColumn(table, SWT.CENTER);
		tblclmnDates.setWidth(127);
		tblclmnDates.setText("From");
		
		tblclmnTo = new TableColumn(table, SWT.CENTER);
		tblclmnTo.setWidth(132);
		tblclmnTo.setText("To");
		
		TableColumn tblclmnStatus = new TableColumn(table, SWT.CENTER);
		tblclmnStatus.setWidth(100);
		tblclmnStatus.setText("Status");
		
		populateCombos();
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				processTimeOff();
				close();				
			}
		});
		button.setText("Submit");
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(362, 417);
	}
	
	private void populateCombos()
	{
		if(itoman!=null)
		{
			ArrayList<Object> values=itoman.getTimeSpans();
			if(values!=null)
			{
				fromTimeCombo.removeAll();
				toTimeCombo.removeAll();
				for(Object value: values)
				{
					
					WidgetUtilities.safeComboAdd(fromTimeCombo, (String)value);					
					WidgetUtilities.safeComboAdd(toTimeCombo, (String)value);
				}
			}
			
			ArrayList<TimeOff> timesoff=itoman.getTimeOffs(m_name);
			Collections.sort(timesoff,new Comparator<TimeOff>()
					{
						public int compare(TimeOff t1, TimeOff t2)
						{
							return t1.getStart().compareTo(t2.getStart());
						}
					}
					);
			Collections.reverse(timesoff);
			
			
			if(timesoff!=null)
			{
				table.removeAll();
				for(TimeOff to : timesoff)
				{
					TableItem ti = new TableItem(table, SWT.NONE);
					String [] cells={to.getStartStr(), to.getEndStr(), to.getStatus()};
					ti.setText(cells);
					ti.setFont(SWTResourceManager.getFont("Arial", 8, SWT.NORMAL));
				}
			}
		}
	
	}
	
	private void processTimeOff()
	{
		String start_time=fromAllDay.getSelection()?"00:00:00.0":fromTimeCombo.getText()+":00.0";
		String end_time=toAllDay.getSelection()?"23:59:59.0":toTimeCombo.getText()+":00.0";
		if(start_time.length()==9)
		{
			start_time="0"+start_time;
		}
		if(end_time.length()==9)
		{
			end_time="0"+end_time;
		}
		
		
		String start_date=DateUtils.dateStringFromWidget(fromDateTime,null);
		String end_date=DateUtils.dateStringFromWidget(toDateTime, null);
		
		if(itoman!=null)
		{
			itoman.addTimeOffRequest(start_date+" "+start_time, end_date+" "+end_time,m_name);
			populateCombos();
			
		}
	}
	
	@Override
	protected void okPressed(){}
}
