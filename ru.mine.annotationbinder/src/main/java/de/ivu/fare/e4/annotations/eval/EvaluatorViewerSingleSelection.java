package de.ivu.fare.e4.annotations.eval;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.Viewer;

import de.ivu.fare.e4.annotations.BindViewerSingleSelectionToModel;
import de.ivu.fare.e4.annotations.extractor.IViewerExtractor;

/**
 * Retrieves the Attributes of the annotation and bind to the ITEMS property of widget
 *
 * @author alf
 *
 */
class EvaluatorViewerSingleSelection {

    // will lookup required instances in this context
    @Inject
    IEclipseContext eclipseContext;

    @Inject
    DataBindingContext jfaceDataBindingContext;

    private EvaluatorTools evaluatorTools;

    /**
     * retrieve all attributes from ITEMS-binding annotation and bind
     *
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    void bindViewersSingleSelectionAttribute(Field annotatedField, Object annotatedFieldValue,
            BindViewerSingleSelectionToModel bindViewerSingleSelectionToModel) throws NoSuchFieldException,
            SecurityException {

        // skip if there is no such annotation
        if (bindViewerSingleSelectionToModel == null) {
            return;
        }

        // retrieve ClassOfModel
        Class<?> modelClass = bindViewerSingleSelectionToModel.modelClass();
        // retrieve FieldInModel
        String modelFieldName = bindViewerSingleSelectionToModel.modelFieldName();
        // retrieve StructuredViewerExtractor
        Class<? extends IViewerExtractor> widgetRetrieverClass = bindViewerSingleSelectionToModel.viewerExtractor();

        // now retrieve instances from Context
        // retrieve Widget using: annotated object, WidgetRetriever
        IViewerExtractor viewerExtractor = EvaluatorTools.findInContextOrMake(widgetRetrieverClass, eclipseContext, false);

        // retrieve Model from the IEclipseContext
        Object model = EvaluatorTools.findInContextOrMake(modelClass, eclipseContext, false);

        // get the Widget which we should bind to
        Viewer viewer = EvaluatorTools.getViewer(viewerExtractor, annotatedFieldValue, modelFieldName, modelClass);

        bindWidgetpropertyItemsToModel(bindViewerSingleSelectionToModel, modelFieldName, model, viewer, annotatedFieldValue);
    }

    /** do the binding between field and widget' items property */
    private void bindWidgetpropertyItemsToModel(BindViewerSingleSelectionToModel bindViewerSingleSelectionToModel,
            String modelFieldName, Object model, Viewer viewer, Object annotatedFieldValue) {

        if (evaluatorTools == null) {
            evaluatorTools = new EvaluatorTools(eclipseContext);
        }

        // VALIDATE
        EvaluatorTools.validateWidget(viewer, modelFieldName);

        IObservableValue observeSingleSelectionComboViewer = ViewerProperties.singleSelection().observe(viewer);
        IObservableValue selectionObjectModelObserveValue = BeanProperties.value(modelFieldName).observe(model);

        // do the binding
        evaluatorTools.bindWithUpdateStrategy(jfaceDataBindingContext, annotatedFieldValue, observeSingleSelectionComboViewer, selectionObjectModelObserveValue);
    }

}
