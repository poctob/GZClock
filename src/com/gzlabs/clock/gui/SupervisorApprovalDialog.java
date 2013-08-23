package com.gzlabs.clock.gui;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.layout.GridData;
import org.eclipse.jface.viewers.ComboViewer;

import com.gzlabs.utils.WidgetUtilities;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SupervisorApprovalDialog extends Dialog {
	
	private ArrayList<String> supervisors;
	private String approver;
	Combo supervisorCombo;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public SupervisorApprovalDialog(Shell parentShell, ArrayList<String> p_supervisors) {
		super(parentShell);
		this.supervisors=p_supervisors;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label lblClockInTime = new Label(container, SWT.NONE);
		lblClockInTime.setText("Clock In time is outside of the allowable range for scheduled shift,");
		
		Label lblPleaseChooseThe = new Label(container, SWT.NONE);
		lblPleaseChooseThe.setText("Please choose the approving supervisor:");
		
		ComboViewer comboViewer = new ComboViewer(container, SWT.NONE);
		supervisorCombo = comboViewer.getCombo();
		supervisorCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		if(supervisors!=null)
		{
			for(String s: supervisors)
			{
				WidgetUtilities.safeComboAdd(supervisorCombo, s);
			}
		}
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
				approver=supervisorCombo.getText();
				close();
			}
		});
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 233);
	}
	
	@Override
	protected void okPressed(){}
	
	public String getApprover()
	{
		return approver;
	}

}
