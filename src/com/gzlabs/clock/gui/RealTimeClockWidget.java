package com.gzlabs.clock.gui;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

import com.gzlabs.utils.DateUtils;

/**
 * Provides a widget for date/time display
 * @author apavlune
 *
 */
public class RealTimeClockWidget extends Composite {

	private Label labelDate;
	private Label labelTime;
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public RealTimeClockWidget(Composite parent, int style) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(153, 204, 153));
		
		labelDate = new Label(this, SWT.NONE);
		labelDate.setBackground(SWTResourceManager.getColor(153, 204, 153));
		labelDate.setAlignment(SWT.CENTER);
		labelDate.setFont(SWTResourceManager.getFont("Arial", 16, SWT.NORMAL));
		labelDate.setBounds(10, 10, 166, 28);
		labelDate.setText("");
		
		labelTime = new Label(this, SWT.NONE);
		labelTime.setBackground(SWTResourceManager.getColor(153, 204, 153));
		labelTime.setFont(SWTResourceManager.getFont("Arial", 16, SWT.NORMAL));
		labelTime.setAlignment(SWT.CENTER);
		labelTime.setBounds(10, 44, 166, 28);
		labelTime.setText("");
	     
		 final int time = 1000;
		 Runnable timer = new Runnable() {
		      public void run() {
		        if(labelDate!=null)
				{
					labelDate.setText(DateUtils.getFormattedtDateString());
					
				}
				
				if(labelTime!=null)
				{
					labelTime.setText(DateUtils.getFormattedtTimeString());
					
				}
		        getDisplay().timerExec(time, this);
		      }
		    };
		    getDisplay().timerExec(time, timer);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}


}
