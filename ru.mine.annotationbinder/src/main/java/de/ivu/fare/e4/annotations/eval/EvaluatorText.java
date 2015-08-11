package de.ivu.fare.e4.annotations.eval;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Widget;

import de.ivu.fare.e4.annotations.BindWidgetTextToModel;
import de.ivu.fare.e4.annotations.context.BindingContextParams;
import de.ivu.fare.e4.annotations.extractor.IWidgetExtractor;

/**
 * Retrieves the Attributes of the annotation and bind to the text property of widget
 *
 * @author alf
 *
 */
class EvaluatorText {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(EvaluatorText.class);

    // will lookup required instances in this context
    @Inject
    IEclipseContext eclipseContext;

    @Inject
    DataBindingContext jfaceDataBindingContext;

    EvaluatorTools evaluatorTools = null;

    /**
     * retrieve all attributes from TEXT-binding annotation and bind
     *
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    void bindWidgetsTextAttribute(Field annotatedField, Object annotatedFieldValue,
            BindWidgetTextToModel bindingToTextAnnotation) throws NoSuchFieldException, SecurityException {

        // skip if there is no such annotation
        if (bindingToTextAnnotation == null) {
            return;
        }

        // retrieve ClassOfModel
        Class<?> modelClass = bindingToTextAnnotation.modelClass();
        // retrieve FieldInModel
        String modelFieldName = bindingToTextAnnotation.modelFieldName();
        // retrieve WidgetRetriever
        Class<? extends IWidgetExtractor> widgetRetrieverClass = bindingToTextAnnotation.widgetExtractor();
        // retrieve converter
        Class<? extends IConverter> converterClassModelToTarget = bindingToTextAnnotation.converterModelToTarget();
        Class<? extends IConverter> converterClassTargetToModel = bindingToTextAnnotation.converterTargetToModel();

        // now retrieve instances from Context
        // retrieve Widget using: annotated object, WidgetRetriever
        IWidgetExtractor widgetRetriever = EvaluatorTools.findInContextOrMake(widgetRetrieverClass, eclipseContext, false);

        // retrieve Model from the IEclipseContext
        Object model = EvaluatorTools.findInContextOrMake(modelClass, eclipseContext, false);

        // retrieve Converters
        IConverter converterModelToTarget = EvaluatorTools.findInContextOrMakeOrNull(converterClassModelToTarget, eclipseContext, false);
        IConverter converterTargetToModel = EvaluatorTools.findInContextOrMakeOrNull(converterClassTargetToModel, eclipseContext, false);

        // TODO alf check whether there are getter and setter for the current modelFieldName

        // get Model's Field from AnnotationsParameter "FieldInModel" and Model
        Field modelField = EvaluatorTools.getPublicOrPrivateDeclaredField(model.getClass(), modelFieldName);

        // get the Widget which we should bind to
        Widget widget = EvaluatorTools.getWidget(widgetRetriever, annotatedFieldValue, modelFieldName, modelClass);

        bindWidgetpropertyTextToModel(bindingToTextAnnotation, modelFieldName, model, widget, eclipseContext,
                annotatedField, annotatedFieldValue, converterModelToTarget, converterTargetToModel);
    }

    /** do the binding between field and widget' text property */
    private void bindWidgetpropertyTextToModel(BindWidgetTextToModel bindingToTextAnnotation, String modelFieldName,
            Object model, Widget widget, IEclipseContext eclipseContext, Field annotatedField,
            Object annotatedFieldValue, IConverter converterModelToTarget, IConverter converterTargetToModel) {
        // bind the Model's Field to the Widget's TEXT attribute

        EvaluatorTools.validateWidget(widget, modelFieldName);

        // Text..
        IObservableValue observeTextInWidget = null;
        try {
            observeTextInWidget = WidgetProperties.text(SWT.Modify).observe(widget);
        } catch (IllegalArgumentException e) {
            // failed to bind to the text property. Not all widgets do support the SWT.Modify event.
            // E.g. combos do not.
        }

        // Combo..
        if (observeTextInWidget == null) {
            try {
                observeTextInWidget = WidgetProperties.text().observe(widget);
            } catch (IllegalArgumentException e) {
                // failed to bind to the text
            }
        }

        if (observeTextInWidget == null) {
            new IllegalStateException(String.format(
                    "Failed to bind field '%s' to text-property of widget with Type '%s'", modelFieldName, widget
                            .getClass().getSimpleName())).printStackTrace();
            return;
        }

        // add the converters to the binding parameters
        BindingContextParams bindingContextParams = EvaluatorTools.findInContextOrMake(BindingContextParams.class, eclipseContext, true);
        bindingContextParams.converterTargetToModelSet(converterTargetToModel, annotatedFieldValue);
        bindingContextParams.converterModelToTargetSet(converterModelToTarget, annotatedFieldValue);

        // Observe model's field
        IObservableValue observeModelfieldInPojo = BeanProperties.value(bindingToTextAnnotation.modelClass(),
                modelFieldName).observe(model);

        if (evaluatorTools == null) {
            evaluatorTools = new EvaluatorTools(eclipseContext);
        }

        // do the binding
        evaluatorTools.bindWithUpdateStrategy(jfaceDataBindingContext, annotatedFieldValue, observeTextInWidget, observeModelfieldInPojo);

    }

}
