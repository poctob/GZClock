package com.gzlabs.clock.gui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.gzlabs.clock.IEmployeeDataManager;
import com.gzlabs.utils.WidgetUtilities;

public class EmployeeInfoDialog extends Dialog {
	private Text homePhone;
	private Text cellPhone;
	private Text email;
	private Text pin;
	private Text pinConfirm;
	private StyledText addressText;
	private IEmployeeDataManager eman;
	private String current_pin;
	private String current_address;
	private String current_hphone;
	private String current_cphone;
	private String current_email;
	
	private static final int PIN_MIN_LENGTH=4;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public EmployeeInfoDialog(Shell parentShell, IEmployeeDataManager p_eman, String p_cpin,String address, String hphone, String cphone, String email) {
		super(parentShell);
		this.eman=p_eman;
		current_pin=p_cpin;
		current_address=address;
		current_hphone=hphone;
		current_cphone=cphone;
		current_email=email;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 5;
		
		Label lblAddress = new Label(container, SWT.NONE);
		lblAddress.setText("Address:");
		
		TextViewer textViewer = new TextViewer(container, SWT.BORDER);
		addressText = textViewer.getTextWidget();
		GridData gd_addressText = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gd_addressText.widthHint = 319;
		gd_addressText.heightHint = 67;
		addressText.setLayoutData(gd_addressText);
		
		Label lblHomePhone = new Label(container, SWT.NONE);
		lblHomePhone.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHomePhone.setText("Home Phone:");
		
		homePhone = new Text(container, SWT.BORDER);
		homePhone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Label lblCellPhone = new Label(container, SWT.NONE);
		lblCellPhone.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblCellPhone.setText("Cell Phone:");
		
		cellPhone = new Text(container, SWT.BORDER);
		cellPhone.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Label lblEmail = new Label(container, SWT.NONE);
		lblEmail.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblEmail.setText("Email:");
		
		email = new Text(container, SWT.BORDER);
		email.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Label lblPin = new Label(container, SWT.NONE);
		lblPin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPin.setText("Pin:");
		
		pin = new Text(container, SWT.BORDER | SWT.PASSWORD);
		GridData gd_pin = new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1);
		gd_pin.widthHint = 168;
		pin.setLayoutData(gd_pin);
		
		Label lblConfirmPin = new Label(container, SWT.NONE);
		lblConfirmPin.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConfirmPin.setText("Confirm Pin:");
		
		pinConfirm = new Text(container, SWT.BORDER | SWT.PASSWORD);
		pinConfirm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 4, 1));
		
		Button btnCancel = new Button(container, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
		GridData gd_btnCancel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnCancel.widthHint = 84;
		btnCancel.setLayoutData(gd_btnCancel);
		btnCancel.setText("Cancel");
		
		Button btnClear = new Button(container, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearControls();
			}
		});
		GridData gd_btnClear = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnClear.widthHint = 85;
		gd_btnClear.heightHint = 30;
		btnClear.setLayoutData(gd_btnClear);
		btnClear.setText("Clear");
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		
		Button btnSubmit = new Button(container, SWT.NONE);
		btnSubmit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				submitData();
			}
		});
		GridData gd_btnSubmit = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btnSubmit.widthHint = 71;
		btnSubmit.setLayoutData(gd_btnSubmit);
		btnSubmit.setText("Submit");

		WidgetUtilities.safeTextSet(pin, current_pin);
		WidgetUtilities.safeTextSet(pinConfirm, current_pin);
		WidgetUtilities.safeTextSet(cellPhone, current_cphone);
		WidgetUtilities.safeTextSet(homePhone, current_hphone);
		WidgetUtilities.safeTextSet(this.email, current_email);
		if(current_address!=null && this.addressText!=null)
		{
			addressText.setText(current_address);
		}
		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(306, 418);
	}
	
	private void clearControls()
	{
		homePhone.setText("");
		cellPhone.setText("");
		email.setText("");
		pin.setText("");
		pinConfirm.setText("");
		addressText.setText("");
	}
	
	private void submitData()
	{
		if(!pin.getText().equals(pinConfirm.getText()))
		{
			MessageDialog.openError(new Shell(), "Error", "Pins Don't Match!");
			return;
		}
		
		if(pin.getText().length()<PIN_MIN_LENGTH)
		{
			MessageDialog.openError(new Shell(), "Error", "Pin is too short!");
			return;
		}
		if(eman!=null)
		{
			eman.updateEmployeeData(addressText.getText(),
					homePhone.getText(),
					cellPhone.getText(),
					email.getText(),
					pin.getText());
		}
		close();
	}
}
