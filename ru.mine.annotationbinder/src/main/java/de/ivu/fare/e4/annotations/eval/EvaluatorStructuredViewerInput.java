package de.ivu.fare.e4.annotations.eval;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;

import de.ivu.fare.e4.annotations.BindStructuredViewerInputToModel;
import de.ivu.fare.e4.annotations.converters.ConverterObjectToString;
import de.ivu.fare.e4.annotations.converters.ConverterObjectToString.DefaultConverterObjectToString;
import de.ivu.fare.e4.annotations.extractor.IStructuredViewerExtractor;
import de.ivu.fare.e4.annotations.provider.PojoReflectiveLabelProvider;

/**
 * Retrieves the Attributes of the annotation and bind to the ITEMS property of widget
 *
 * @author alf
 *
 */
class EvaluatorStructuredViewerInput {

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
    void bindStructuredViewersInputAttribute(Field annotatedField, Object annotatedFieldValue,
            BindStructuredViewerInputToModel bindStructuredViewerInputToModel) throws NoSuchFieldException,
            SecurityException {

        // skip if there is no such annotation
        if (bindStructuredViewerInputToModel == null) {
            return;
        }

        // retrieve ClassOfModel
        Class<?> modelClass = bindStructuredViewerInputToModel.modelClass();
        // retrieve FieldInModel
        String modelFieldName = bindStructuredViewerInputToModel.modelFieldName();
        // retrieve StructuredViewerExtractor
        Class<? extends IStructuredViewerExtractor> widgetRetrieverClass = bindStructuredViewerInputToModel
                .structuredViewerExtractor();
        // retrieve pojoClass
        Class<?> pojoClass = bindStructuredViewerInputToModel.pojoClass();
        // retrieve BeanFieldName which is used as Bean Labels in widgets
        String[] beanLabelFieldNames = bindStructuredViewerInputToModel.beanLabelFieldNames();
        // retrieve the Converter which can turn PoJos into Strings
        Class<? extends ConverterObjectToString<?>> pojoToStringConverterClass = bindStructuredViewerInputToModel.pojoToStringConverter();
        // retrieve the name of the POJO field
        String pojoFieldNameForReflection = bindStructuredViewerInputToModel.pojoFieldNameForReflection();

        // now retrieve instances from Context
        // retrieve Widget using: annotated object, WidgetRetriever
        IStructuredViewerExtractor widgetRetriever = EvaluatorTools.findInContextOrMake(widgetRetrieverClass,
                eclipseContext, false);

        // retrieve Model from the IEclipseContext
        Object model = EvaluatorTools.findInContextOrMake(modelClass, eclipseContext, false);

        // get the Widget which we should bind to
        StructuredViewer structuredViewer = EvaluatorTools.getWidget(widgetRetriever, annotatedFieldValue,
                modelFieldName, modelClass);

        // get the converter which should be responsible for converting the PoJo to String
        ConverterObjectToString<?> objectToStringConverter = EvaluatorTools.findInContextOrMake(pojoToStringConverterClass, eclipseContext, false);

        bindWidgetpropertyItemsToModel(bindStructuredViewerInputToModel, modelFieldName, model, structuredViewer,
                pojoClass, beanLabelFieldNames, objectToStringConverter, pojoFieldNameForReflection);
    }

    /** do the binding between field and widget' items property */
    private void bindWidgetpropertyItemsToModel(BindStructuredViewerInputToModel bindingToTextAnnotation,
            String modelFieldName, Object model, StructuredViewer structuredViewer, Class pojoClass,
            String[] beanLabelFieldNames, ConverterObjectToString<?> objectToStringConverter,
            String pojoFieldNameForReflection) {

        // VALIDATE
        EvaluatorTools.validateWidget(structuredViewer, modelFieldName);

        // TODO alf check whether the given modelFieldName contains a List, Map, Set
        // TODO alf check whether the field with this name exists and get/set methods exist too

        //
        // BIND
        //

        // POJO Version
        ObservableListContentProvider listContentProvider = new ObservableListContentProvider();

        IBaseLabelProvider labelProvider = getLabelProvider(objectToStringConverter, pojoClass, beanLabelFieldNames, pojoFieldNameForReflection, listContentProvider);
        structuredViewer.setLabelProvider(labelProvider);

        structuredViewer.setContentProvider(listContentProvider);
        //
        IObservableList listNamedModeFieldName = BeanProperties.list(modelFieldName).observe(model);
        structuredViewer.setInput(listNamedModeFieldName);
    }



    private IBaseLabelProvider getLabelProvider(final ConverterObjectToString objectToStringConverter, final Class<?> pojoClass, final String[] beanLabelFieldNames, final String pojoFieldNameForReflection, final ObservableListContentProvider listContentProvider){

        // 1. treat the model as a BEAN (getter and setter REQUIRED)
        if(beanLabelFieldNames.length > 0){
            IObservableMap[] observeMaps = getObserveMaps(pojoClass, beanLabelFieldNames, listContentProvider);

            // 1. check whether a converter is given
            if (objectToStringConverter != null && !(objectToStringConverter instanceof DefaultConverterObjectToString)) {
                return getObservableMapLabelProvider(objectToStringConverter, observeMaps);
            }

            // 2. on default return the labelprovider, which is an instance of DefaultConverterPojoToString
            return new ObservableMapLabelProvider(observeMaps);
        }

        // BELOW TREAT THE MODEL AS A POJO (NO GETTER OR SETTER REQUIRED)

        // 2. is there a n explicit converter given? Can convert data without any other information
        if(!(objectToStringConverter instanceof DefaultConverterObjectToString)){
             return getPojoReflectiveLabelProvider(pojoClass, pojoFieldNameForReflection, objectToStringConverter);
        }

        // 3. if the pojoFieldNameForReflection is set - use that to get the field via reflection
        if(!pojoFieldNameForReflection.isEmpty()){

            // fallback to the labelProvider which would just return the value of the bean field
            return new PojoReflectiveLabelProvider(pojoClass, pojoFieldNameForReflection);
        }

        // 4. Fallback to the default converter
        return getPojoReflectiveLabelProvider(pojoClass, pojoFieldNameForReflection, new DefaultConverterObjectToString());
    }

    private IObservableMap[] getObserveMaps(final Class<?> pojoClass, String[] beanLabelFieldNames,
            final ObservableListContentProvider listContentProvider) {
        IObservableMap[] observeMaps = PojoObservables.observeMaps(listContentProvider.getKnownElements(), pojoClass, beanLabelFieldNames);
        return observeMaps;
    }

    private ObservableMapLabelProvider getObservableMapLabelProvider(final ConverterObjectToString objectToStringConverter, final IObservableMap[] observeMaps){
        return new ObservableMapLabelProvider(observeMaps){
            @Override
            public String getColumnText(Object element, int columnIndex) {
                return objectToStringConverter.convert(element, columnIndex);
            }
        };
    }

    private PojoReflectiveLabelProvider getPojoReflectiveLabelProvider(final Class<?> pojoClass, final String fielName, final ConverterObjectToString objectToStringConverter){
        return new PojoReflectiveLabelProvider(pojoClass, fielName){
            @Override
            public String getText(Object element) {
                return objectToStringConverter.convert(element, 0);
            }
        };
    }
}
