package de.ivu.fare.e4.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

public class CombinedTextWidget extends CombinedAbstractWidget {
	
	private Spinner spinner;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CombinedTextWidget(Composite parent, int style) {
		super(parent, style);
		
		spinner = new Spinner(this, SWT.BORDER);
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	@Override
	public Widget getWidget() {
		return spinner;
	}
	
	// generated Getter and Setter
	
	public Spinner getText() {
		return spinner;
	}
	
	public void setText(Spinner spinner) {
		this.spinner = spinner;
	}
	
}
