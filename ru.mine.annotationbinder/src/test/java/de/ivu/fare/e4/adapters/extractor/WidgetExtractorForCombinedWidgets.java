package de.ivu.fare.e4.adapters.extractor;

import org.eclipse.swt.widgets.Widget;

import de.ivu.fare.e4.annotations.extractor.WidgetExtractor;
import de.ivu.fare.e4.widgets.CombinedAbstractWidget;

/**
 * One Extractor for all Combined Widgets.
 * @author skip
 *
 */
public class WidgetExtractorForCombinedWidgets extends WidgetExtractor {

	@Override
	public Widget getWidget(Object combinedAbstractWidget) {
		return ((CombinedAbstractWidget)combinedAbstractWidget).getWidget();
	}
	
}
