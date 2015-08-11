package de.ivu.fare.e4.annotations.eval;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Widget;

import de.ivu.fare.e4.annotations.BindWidgetImageToModel;
import de.ivu.fare.e4.annotations.extractor.IWidgetExtractor;

/**
 * Retrieves the Attributes of the annotation and bind to the text property of widget
 *
 * @author alf
 *
 */
class EvaluatorImage {

    // will lookup required instances in this context
    @Inject
    IEclipseContext eclipseContext;

    @Inject
    DataBindingContext jfaceDataBindingContext;

    EvaluatorTools evaluatorTools;

    /**
     * retrieve all attributes from TEXT-binding annotation and bind
     *
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    void bindWidgetsImageAttribute(Field annotatedField, Object annotatedFieldValue,
            BindWidgetImageToModel bindWidgetImageToModel) throws NoSuchFieldException, SecurityException {

        // skip if there is no such annotation
        if (bindWidgetImageToModel == null) {
            return;
        }

        // retrieve ClassOfModel
        Class<?> modelClass = bindWidgetImageToModel.modelClass();
        // retrieve FieldInModel
        String modelFieldName = bindWidgetImageToModel.modelFieldName();
        // retrieve WidgetRetriever
        Class<? extends IWidgetExtractor> widgetRetrieverClass = bindWidgetImageToModel.widgetExtractor();

        // now retrieve instances from Context
        // retrieve Widget using: annotated object, WidgetRetriever
        IWidgetExtractor widgetRetriever = EvaluatorTools.findInContextOrMake(widgetRetrieverClass, eclipseContext, false);

        // retrieve Model from the IEclipseContext
        Object model = EvaluatorTools.findInContextOrMake(modelClass, eclipseContext, false);

        // get Model's Field from AnnotationsParameter "FieldInModel" and Model
        Field modelField = EvaluatorTools.getPublicOrPrivateDeclaredField(model.getClass(), modelFieldName);

        // get the Widget which we should bind to
        Widget widget = EvaluatorTools.getWidget(widgetRetriever, annotatedFieldValue, modelFieldName, modelClass);

        bindWidgetpropertyEnabilityToModel(bindWidgetImageToModel, modelFieldName, model, widget, annotatedFieldValue);
    }

    /** do the binding between field and widget' text property */
    private void bindWidgetpropertyEnabilityToModel(BindWidgetImageToModel bindingToSelectionAnnotation,
            String modelFieldName, Object model, Widget widget, Object annotatedFieldValue) {

        if (evaluatorTools == null) {
            evaluatorTools = new EvaluatorTools(eclipseContext);
        }

        EvaluatorTools.validateWidget(widget, modelFieldName);

        // bind the Model's Field to the Widget's TEXT attribute

        // Selection..
        IObservableValue observeSelectionInWidget = null;
        try {
            observeSelectionInWidget = WidgetProperties.image().observe(widget);
        } catch (Exception e) {
            new IllegalStateException(String.format(
                    "Failed to bind field '%s' to enabled-events of widget with Type '%s'", modelFieldName, widget
                            .getClass().getSimpleName()), e).printStackTrace();
        }

        // Observe model's field
        IObservableValue observeModelfieldInPojo = BeanProperties.value(bindingToSelectionAnnotation.modelClass(),
                modelFieldName).observe(model);

//        // bind them together
//        jfaceDataBindingContext.bindValue(observeSelectionInWidget, observeModelfieldInPojo, null, null);

        evaluatorTools.bindWithUpdateStrategy(jfaceDataBindingContext, annotatedFieldValue, observeSelectionInWidget, observeModelfieldInPojo);
    }

}
