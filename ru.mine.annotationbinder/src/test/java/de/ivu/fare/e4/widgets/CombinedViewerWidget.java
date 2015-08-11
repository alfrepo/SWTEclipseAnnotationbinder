package de.ivu.fare.e4.widgets;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

public class CombinedViewerWidget extends CombinedAbstractWidget {
	
	private Combo combo;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CombinedViewerWidget(Composite parent, int style) {
		super(parent, style);
		
		ComboViewer comboViewer = new ComboViewer(getComposite(), SWT.NONE);
		combo = comboViewer.getCombo();
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	// generated Getter and Setter
	
	@Override
	public Widget getWidget() {
		return combo;
	}
	
	public Combo getCombo() {
		return combo;
	}
	
	public void setCombo(Combo combo) {
		this.combo = combo;
	}
}
