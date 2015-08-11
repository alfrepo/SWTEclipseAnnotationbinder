package de.ivu.fare.e4.widgets;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.SWT;

public final class Factory {
	
	/**
	 * @wbp.factory
	 */
	public static CombinedTextWidget createTestCombinedTextWidget(Composite parent) {
		CombinedTextWidget testCombinedTextWidget = new CombinedTextWidget(parent, SWT.NONE);
		return testCombinedTextWidget;
	}
	
	/**
	 * @wbp.factory
	 */
	public static CombinedComboWidget createTestCombinedComboWidget(Composite parent) {
		CombinedComboWidget testCombinedComboWidget = new CombinedComboWidget(parent, SWT.NONE);
		return testCombinedComboWidget;
	}
	
	/**
	 * @wbp.factory
	 */
	public static CombinedSpinnerWidget createTestCombinedSpinnerWidget(Composite parent) {
		CombinedSpinnerWidget testCombinedSpinnerWidget = new CombinedSpinnerWidget(parent, SWT.NONE);
		return testCombinedSpinnerWidget;
	}
	
}