package de.ivu.fare.e4.annotations.eval;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.databinding.swt.ISWTObservableList;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Widget;

import de.ivu.fare.e4.annotations.BindWidgetItemsToModel;
import de.ivu.fare.e4.annotations.extractor.IWidgetExtractor;

/**
 * Retrieves the Attributes of the annotation and bind to the ITEMS property of widget
 *
 * @author alf
 *
 */
class EvaluatorItems {

    // will lookup required instances in this context
    @Inject
    IEclipseContext eclipseContext;

    @Inject
    DataBindingContext bindingContext;

    // ITEMS BINDINGS
    /**
     * retrieve all attributes from ITEMS-binding annotation and bind
     *
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    void bindWidgetsItemAttribute(Field annotatedField, Object annotatedFieldValue,
            BindWidgetItemsToModel bindWidgetItemsToModel) throws NoSuchFieldException, SecurityException {

        // skip if there is no such annotation
        if (bindWidgetItemsToModel == null) {
            return;
        }

        // retrieve ClassOfModel
        Class<?> modelClass = bindWidgetItemsToModel.modelClass();
        // retrieve FieldInModel
        String modelFieldName = bindWidgetItemsToModel.modelFieldName();
        // retrieve WidgetRetriever
        Class<? extends IWidgetExtractor> widgetRetrieverClass = bindWidgetItemsToModel.widgetExtractor();

        // now retrieve instances from Context
        // retrieve Widget using: annotated object, WidgetRetriever
        IWidgetExtractor widgetRetriever = EvaluatorTools.findInContextOrMake(widgetRetrieverClass, eclipseContext, false);

        // retrieve Model from the IEclipseContext
        Object model = EvaluatorTools.findInContextOrMake(modelClass, eclipseContext, false);

        // get the Widget which we should bind to
        Widget widget = EvaluatorTools.getWidget(widgetRetriever, annotatedFieldValue, modelFieldName, modelClass);

        bindWidgetpropertyItemsToModel(bindWidgetItemsToModel, modelFieldName, model, widget);
    }

    /** do the binding between field and widget' items property */
    private void bindWidgetpropertyItemsToModel(BindWidgetItemsToModel bindingToTextAnnotation, String modelFieldName,
            Object model, Widget widget) {

        EvaluatorTools.validateWidget(widget, modelFieldName);

        // bind the Model's Field to the Widget's ITEMS attribute
        // Observe items in widget
        ISWTObservableList observeDropdownItemsInWidget;
        try {
            observeDropdownItemsInWidget = WidgetProperties.items().observe(widget);
        } catch (Exception e) {
            new IllegalStateException(String.format(
                    "Failed to bind field '%s' to items-property of widget with Type '%s'", modelFieldName, widget
                            .getClass().getSimpleName()), e).printStackTrace();
            return;
        }

        // Observe model's list
        IObservableList observeModelfieldInPojo = BeanProperties.list(modelFieldName).observe(model);

        // bind them together
        bindingContext.bindList(observeDropdownItemsInWidget, observeModelfieldInPojo, null, null);
    }

}
