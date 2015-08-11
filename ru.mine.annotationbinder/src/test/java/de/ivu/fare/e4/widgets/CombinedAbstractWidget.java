package de.ivu.fare.e4.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.layout.FillLayout;

public abstract class CombinedAbstractWidget extends Composite {
	
	private Label lblLabel;
	private Composite composite;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CombinedAbstractWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		lblLabel = new Label(this, SWT.NONE);
		lblLabel.setText("label");
		lblLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		composite = new Composite(this, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	/** Method to return the widget, which is added to the composite */
	public abstract Widget getWidget(); 
	
	// generated Getter and Setter
	
	public Label getLblLabel() {
		return lblLabel;
	}
	
	public void setLblLabel(Label lblLabel) {
		this.lblLabel = lblLabel;
	}
	public Composite getComposite() {
		return composite;
	}
}
