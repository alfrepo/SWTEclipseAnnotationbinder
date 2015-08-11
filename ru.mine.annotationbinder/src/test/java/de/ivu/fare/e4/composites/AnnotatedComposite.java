package de.ivu.fare.e4.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import de.ivu.fare.e4.adapters.converter.ConverterBeanToText;
import de.ivu.fare.e4.adapters.extractor.WidgetExtractorForCombinedWidgets;
import de.ivu.fare.e4.annotations.BindWidgetSelectionToModel;
import de.ivu.fare.e4.annotations.BindWidgetTextToModel;
import de.ivu.fare.e4.model.Model;
import de.ivu.fare.e4.widgets.CombinedComboWidget;
import de.ivu.fare.e4.widgets.CombinedSpinnerWidget;
import de.ivu.fare.e4.widgets.CombinedTextWidget;
import de.ivu.fare.e4.widgets.Factory;

public class AnnotatedComposite extends Composite {
	
	@BindWidgetTextToModel(modelFieldName="textfield", modelClass=Model.class, widgetExtractor=WidgetExtractorForCombinedWidgets.class)
	private CombinedTextWidget text;
	
	@BindWidgetTextToModel(modelFieldName="comboPojos", modelClass=Model.class, widgetExtractor=WidgetExtractorForCombinedWidgets.class)
	private CombinedComboWidget combinedComboWidget;
	
	@BindWidgetSelectionToModel(modelFieldName="viewerSingleSelectionBean", modelClass=Model.class)
	@BindWidgetTextToModel(modelFieldName="comboViewerInputBean", modelClass=Model.class)
	private CombinedSpinnerWidget combinedSpinnerWidget;

	
    /**
     * Create the composite.
     * @param parent
     * @param style
     */
    public AnnotatedComposite(Composite parent, int style) {
        super(parent, style);
        setLayout(new GridLayout(1, false));
        
        text = Factory.createTestCombinedTextWidget(this);
        text.getLblLabel().setText("Text field");
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        
        combinedComboWidget = Factory.createTestCombinedComboWidget(this);
        combinedComboWidget.getLblLabel().setText("Combo box");
        combinedComboWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        combinedSpinnerWidget = Factory.createTestCombinedSpinnerWidget(this);
        combinedSpinnerWidget.getLblLabel().setText("Spinner");
        combinedSpinnerWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
	public CombinedTextWidget getText() {
		return text;
	}
	public CombinedComboWidget getCombinedComboWidget() {
		return combinedComboWidget;
	}
	public CombinedSpinnerWidget getCombinedSpinnerWidget() {
		return combinedSpinnerWidget;
	}
}
