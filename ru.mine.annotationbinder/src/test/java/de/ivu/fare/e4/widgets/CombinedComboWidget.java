package de.ivu.fare.e4.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

public class CombinedComboWidget extends CombinedAbstractWidget {
	
	private Combo combo;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CombinedComboWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		combo = new Combo(getComposite(), SWT.BORDER);
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	// generated Getter and Setter
	
	public Combo getText() {
		return combo;
	}
	
	public void setText(Combo combo) {
		this.combo = combo;
	}

	@Override
	public Widget getWidget() {
		return combo;
	}
}
