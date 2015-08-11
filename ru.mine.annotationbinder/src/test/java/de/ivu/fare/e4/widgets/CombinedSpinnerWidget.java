package de.ivu.fare.e4.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class CombinedSpinnerWidget extends CombinedAbstractWidget {
	
	private Text text;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CombinedSpinnerWidget(Composite parent, int style) {
		super(parent, style);
		text = new Text(getComposite(), SWT.BORDER);
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	@Override
	public Widget getWidget() {
		return text;
	}
	
	// generated Getter and Setter
	
	public Text getText() {
		return text;
	}
	
	public void setText(Text text) {
		this.text = text;
	}
	
}
