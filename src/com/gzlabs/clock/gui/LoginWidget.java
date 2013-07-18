package com.gzlabs.clock.gui;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.gzlabs.clock.ILoginManager;
import com.gzlabs.utils.WidgetUtilities;
/**
 * Provides keypad for entering the pin.
 * @author apavlune
 *
 */
public class LoginWidget extends Composite {
	private Text textPin;
	private Combo comboNames;
	private ILoginManager ilm;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public LoginWidget(Composite parent, int style, ILoginManager lm) {
		super(parent, style);
		setBackground(SWTResourceManager.getColor(153, 204, 153));
		this.ilm=lm;
		
		Label lblYourName = new Label(this, SWT.NONE);
		lblYourName.setBackground(SWTResourceManager.getColor(153, 204, 153));
		lblYourName.setBounds(10, 22, 77, 18);
		lblYourName.setText("Your Name:");
		
		comboNames = new Combo(this, SWT.NONE);
		comboNames.setBounds(93, 10, 204, 30);
		
		Label lblYourPin = new Label(this, SWT.NONE);
		lblYourPin.setBackground(SWTResourceManager.getColor(153, 204, 153));
		lblYourPin.setBounds(10, 66, 69, 18);
		lblYourPin.setText("Your Pin:");
		
		textPin = new Text(this, SWT.BORDER | SWT.PASSWORD);
		textPin.setBounds(93, 56, 204, 28);
		
		Button button1 = new Button(this, SWT.NONE);
		button1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("1");
			}
		});
		button1.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button1.setBounds(10, 102, 69, 56);
		button1.setText("1");
		
		Button button2 = new Button(this, SWT.NONE);
		button2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("2");
			}
		});
		button2.setText("2");
		button2.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button2.setBounds(120, 102, 69, 56);
		
		Button button3 = new Button(this, SWT.NONE);
		button3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("3");
			}
		});
		button3.setText("3");
		button3.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button3.setBounds(230, 102, 69, 56);
		
		Button button4 = new Button(this, SWT.NONE);
		button4.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("4");
			}
		});
		button4.setText("4");
		button4.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button4.setBounds(10, 182, 69, 56);
		
		Button button5 = new Button(this, SWT.NONE);
		button5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("5");
			}
		});
		button5.setText("5");
		button5.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button5.setBounds(120, 182, 69, 56);
		
		Button button6 = new Button(this, SWT.NONE);
		button6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("6");
			}
		});
		button6.setText("6");
		button6.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button6.setBounds(230, 182, 69, 56);
		
		Button button7 = new Button(this, SWT.NONE);
		button7.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("7");
			}
		});
		button7.setText("7");
		button7.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button7.setBounds(10, 263, 69, 56);
		
		Button button8 = new Button(this, SWT.NONE);
		button8.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("8");
			}
		});
		button8.setText("8");
		button8.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button8.setBounds(120, 263, 69, 56);
		
		Button button9 = new Button(this, SWT.NONE);
		button9.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("9");
			}
		});
		button9.setText("9");
		button9.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button9.setBounds(230, 263, 69, 56);
		
		Button button0 = new Button(this, SWT.NONE);
		button0.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				textPin.append("0");
			}
		});
		button0.setText("0");
		button0.setFont(SWTResourceManager.getFont("Arial", 18, SWT.BOLD));
		button0.setBounds(120, 343, 69, 56);
		
		Button buttonClear = new Button(this, SWT.NONE);
		buttonClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearSelections();
			}
		});
		buttonClear.setBounds(10, 411, 88, 30);
		buttonClear.setText("Clear");
		
		Button buttonLogin = new Button(this, SWT.NONE);
		buttonLogin.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(comboNames.getText().length()>0 && textPin.getText().length()>0 && ilm !=null)
				{
					ilm.processLogIn(comboNames.getText(), textPin.getText());
				}
			}
		});
		buttonLogin.setText("Log In");
		buttonLogin.setBounds(209, 411, 88, 30);
		populateNameCombo(ilm.getNames());
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	/**
	 * Populates combo with names.
	 * @param names Names list
	 */
	private void populateNameCombo(ArrayList<String> names)
	{
		for(String s : names)
		{
			WidgetUtilities.safeComboAdd(comboNames, s);
		}
	}
	
	/**
	 * Clears controls
	 */
	public void clearSelections()
	{
		textPin.setText("");
		comboNames.clearSelection();
	}
}
