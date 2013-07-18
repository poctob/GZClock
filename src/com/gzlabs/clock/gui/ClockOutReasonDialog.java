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
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.layout.GridData;

import com.gzlabs.utils.WidgetUtilities;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class ClockOutReasonDialog extends Dialog {

	private ArrayList<String> reasons;
	private Combo reasonCombo;
	private String selectedReason;
	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ClockOutReasonDialog(Shell parentShell, ArrayList<String> reasons) {
		super(parentShell);
		this.reasons=reasons;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Label lblSelectClockOut = new Label(container, SWT.NONE);
		lblSelectClockOut.setText("Select clock out reason below:");
		
		ComboViewer comboViewer = new ComboViewer(container, SWT.NONE);
		reasonCombo = comboViewer.getCombo();
		reasonCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		for(String s:this.reasons)
		{
			WidgetUtilities.safeComboAdd(reasonCombo, s);
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
				selectedReason=reasonCombo.getText();
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
		return new Point(252, 179);
	}
	
	@Override
	protected void okPressed(){}
	
	public String getSelectedReason()
	{
		return selectedReason;
	}
}
