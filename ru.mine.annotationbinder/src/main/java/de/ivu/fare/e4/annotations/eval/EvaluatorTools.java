package de.ivu.fare.e4.annotations.eval;

import java.beans.PropertyChangeSupport;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.internal.databinding.beans.BeanPropertyHelper;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.InjectionException;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import de.ivu.fare.e4.annotations.context.BindingContextParams;
import de.ivu.fare.e4.annotations.context.IMarkerStatusVisualizer;
import de.ivu.fare.e4.annotations.context.MarkerRegistry;
import de.ivu.fare.e4.annotations.context.ValidationContext;
import de.ivu.fare.e4.annotations.context.ValidationContextData;
import de.ivu.fare.e4.annotations.eval.validation.internal.ValidatorCachingAdapter;
import de.ivu.fare.e4.annotations.eval.validation.internal.ValidatorCombined;
import de.ivu.fare.e4.annotations.extractor.IStructuredViewerExtractor;
import de.ivu.fare.e4.annotations.extractor.ITextExtractor;
import de.ivu.fare.e4.annotations.extractor.IViewerExtractor;
import de.ivu.fare.e4.annotations.extractor.IWidgetExtractor;

/**
 * Encapsulates methods, which differen annotaions have in common
 *
 * @param eclipseContext
 */
public class EvaluatorTools {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(EvaluatorTools.class);

    IEclipseContext eclipseContext;
    ValidationContext validationContext;
    DataBindingContext jfaceDataBindingContext;

    public EvaluatorTools(IEclipseContext eclipseContext) {
        this.eclipseContext = eclipseContext;
        this.validationContext = findInContextOrMakeAndStoreincontext(ValidationContext.class, eclipseContext);
        this.jfaceDataBindingContext = findInContextOrMakeAndStoreincontext(DataBindingContext.class, eclipseContext);
        ;
    }

    public void bindWithUpdateStrategy(DataBindingContext jfaceDataBindingContext, Object annotatedFieldValue,
            IObservableValue targetObservableValue, IObservableValue modelObservableValueInPojo) {
        // 1. create caching validators from BindingContextParams.
        BindingContextParams bindingContextParams = findInContextOrMake(BindingContextParams.class, eclipseContext,
                true);

        // pass the bindingContext on
        bindWithUpdateStrategy(jfaceDataBindingContext, annotatedFieldValue, targetObservableValue,
                modelObservableValueInPojo, bindingContextParams);
    }

    public void bindWithUpdateStrategy(DataBindingContext jfaceDataBindingContext, Object annotatedFieldValue,
            IObservableValue targetObservableValue, IObservableValue modelObservableValueInPojo,
            BindingContextParams bindingContextParams) {

        IConverter converterTargetToModel = bindingContextParams.converterTargetToModel(annotatedFieldValue);
        IConverter converterModelToTarget = bindingContextParams.converterModelToTarget(annotatedFieldValue);

        // retrieve the validators which where added to the jfaceDataBindingContext from another
        // annotations
        List<IValidator> validatorsAfterGet = bindingContextParams.validatorsAfterGet(annotatedFieldValue);
        List<IValidator> validatorsBeforeSet = bindingContextParams.validatorsBeforeSet(annotatedFieldValue);
        List<IValidator> validatorsAfterConvert = bindingContextParams.validatorsAfterConvert(annotatedFieldValue);

        // combined validators, which are wrapped into a Registry triggering adapter
        ValidatorCachingAdapter validatorAfterGet = validatorCachingAdapter(combine(validatorsAfterGet), annotatedFieldValue);
        ValidatorCachingAdapter validatorBeforeSet = validatorCachingAdapter(combine(validatorsBeforeSet), annotatedFieldValue);
        ValidatorCachingAdapter validatorAfterConvert = validatorCachingAdapter(combine(validatorsAfterConvert), annotatedFieldValue);

        // 2. visualize the validation status changes on the widgets, using the marker from the  BindingContextParams
        addMarkerStatusVisualizerToBinding(annotatedFieldValue, validatorAfterGet, validatorBeforeSet, validatorAfterConvert);

        // 3. create for ModelToTarget and TargetToModel strategies the : afterGet, afterConvert,
        // beforeSet validators
        UpdateValueStrategy targetToModelStrategy = getUpdateValueStrategy(annotatedFieldValue, converterTargetToModel, validatorAfterGet, validatorBeforeSet, validatorAfterConvert);


        // 4. model to target needs more wrappers around the validators, which would always return OK BUT trigger the listeners with the validation value
        IValidator validatorAlwaysOkAfterGet =  validatorModelToTargetAlwaysOk(validatorAfterGet, annotatedFieldValue);
        IValidator validatorAlwaysOkBeforeSet =  validatorModelToTargetAlwaysOk(validatorBeforeSet, annotatedFieldValue);
        IValidator validatorAlwaysOkAfterConvert =  validatorModelToTargetAlwaysOk(validatorAfterConvert, annotatedFieldValue);
        // and use them for the update strategy
        UpdateValueStrategy modelToTargetStrategy = getUpdateValueStrategy(annotatedFieldValue, converterModelToTarget, validatorAlwaysOkAfterGet, validatorAlwaysOkBeforeSet, validatorAlwaysOkAfterConvert);

        // 3. bind them target / model together using the strategy
        org.eclipse.core.databinding.Binding binding = jfaceDataBindingContext.bindValue(targetObservableValue, modelObservableValueInPojo, targetToModelStrategy, modelToTargetStrategy);

        // 4. add the caching validators to the ValidationContext. Add the validators only once
        ValidationContextData validationContextData = getValidationData(annotatedFieldValue, binding, validatorAfterGet, validatorBeforeSet, validatorAfterConvert);
        validationContext.addValidationData(validationContextData);

    }

    public void addMarkerStatusVisualizerToBinding(final Object annotatedFieldValue,
            ValidatorCachingAdapter... validatorCachingAdapters) {

        // 1. try to get the IMarkerStatusVisualizer explicitly added to the BindingContextParams
        BindingContextParams bindingContextParams = findInContextOrMake(BindingContextParams.class, eclipseContext,
                true);

        IMarkerStatusVisualizer markerStatusVisualizer = bindingContextParams
                .markerStatusVisualizer(annotatedFieldValue);

        // 2. try to get the IMarkerStatusVisualizer from the MarkerRegistry if necessary
        if (markerStatusVisualizer == null) {
            markerStatusVisualizer = MarkerRegistry.getMarkerUncheckedWidgetType(eclipseContext,
                    annotatedFieldValue.getClass(), IMarkerStatusVisualizer.class);
        }

        // skip if there is not visualizer or marker
        if (markerStatusVisualizer == null || validatorCachingAdapters == null || validatorCachingAdapters.length <= 0) {
            return;
        }

        // how would the listener react on validator changes?
        final IMarkerStatusVisualizer markerStatusVisualizerFinal = markerStatusVisualizer;
        ValidatorCachingAdapter.Listener l = new ValidatorCachingAdapter.Listener() {
            @Override
            public void onValidationStatusChange(IStatus newStatus, Object annotatedFieldValue) {
                markerStatusVisualizerFinal.visualizeStatusOnWidget(annotatedFieldValue, newStatus);
            }
        };

        // add the listener to all the ValidatorCachingAdapters for them to react on validation changes
        for (ValidatorCachingAdapter v : validatorCachingAdapters) {
            if (v == null) {
                continue;
            }
            v.add(l);
        }

    }

    // public void addMarkerStatusVisualizerToBinding(Binding binding, Object annotatedFieldValue) {
    // // 1. try to get the IMarkerStatusVisualizer explicitly added to the BindingContextParams
    // BindingContextParams bindingContextParams = findInContextOrMake(BindingContextParams.class,
    // eclipseContext, true);
    //
    // IMarkerStatusVisualizer markerStatusVisualizer =
    // bindingContextParams.markerStatusVisualizer(annotatedFieldValue);
    //
    // // 2. try to get the IMarkerStatusVisualizer from the MarkerRegistry if necessary
    // if(markerStatusVisualizer == null){
    // markerStatusVisualizer = MarkerRegistry.getMarkerUncheckedWidgetType(eclipseContext,
    // annotatedFieldValue.getClass(), IMarkerStatusVisualizer.class);
    // }
    //
    // // register the marker to the binding
    // if(markerStatusVisualizer != null){
    // // use the ControlDecorationSupport to hook in and listen for binding changes
    // ControlDecorationSupport.create(binding, SWT.RIGHT | SWT.UP, null,
    // getControlDecorationSupport(annotatedFieldValue, markerStatusVisualizer));
    // }
    // }

    private ControlDecorationUpdater getControlDecorationSupport(final Object annotationFieldValue,
            final IMarkerStatusVisualizer markerStatusVisualizer) {
        return new ControlDecorationUpdater() {

            @SuppressWarnings("unchecked")
            @Override
            protected void update(ControlDecoration decoration, IStatus status) {
                markerStatusVisualizer.visualizeStatusOnWidget(annotationFieldValue, status);
            }

            @Override
            protected String getDescriptionText(IStatus status) {
                // nothing - the text should be provided by the IStatus
                return null;
            }

            @Override
            protected Image getImage(IStatus status) {
                // nothing - the image should be provided by the widget
                return null;
            }
        };
    }

    private UpdateValueStrategy getUpdateValueStrategy(final Object annotationFieldvalue, IConverter converter,
            IValidator validatorAfterGet, IValidator validatorBeforeSet,
            IValidator validatorAfterConvert) {
        boolean needStrategy = false;
        needStrategy |= converter != null;
        needStrategy |= (validatorAfterGet != null);
        needStrategy |= (validatorBeforeSet != null);
        needStrategy |= (validatorAfterConvert != null);

        // if all values were null - return null instead of strategy
        if (!needStrategy) {
            return null;
        }

        // update strategy
        UpdateValueStrategy updateValueStrategy = new UpdateValueStrategy();

        updateValueStrategy.setConverter(converter);
        updateValueStrategy.setAfterGetValidator(validatorAfterGet);
        updateValueStrategy.setBeforeSetValidator(validatorBeforeSet);
        updateValueStrategy.setAfterConvertValidator(validatorAfterConvert);

        return updateValueStrategy;
    }

    private ValidationContextData getValidationData(final Object annotationFieldvalue,
            final org.eclipse.core.databinding.Binding binding, ValidatorCachingAdapter... validator) {
        // no validators - nothing to wrap by data. Return null.
        if (validator == null || validator.length <= 0) {
            return null;
        }

        // clear null values passed
        List<ValidatorCachingAdapter> validatorList = new ArrayList<>(Arrays.asList(validator));
        validatorList.removeAll(Collections.singleton(null));

        // only null values passed?
        if (validatorList.isEmpty()) {
            return null;
        }

        return new ValidationContextData(annotationFieldvalue, binding, validatorList);
    }

    public enum BindingDirection {
                                  fromModelToTarget,
                                  fromTargetToModel
    }

    /** How should the Values from model be converted, before passing them to widgets? */
    public IConverter retrieveConverter(Class<? extends IConverter> converterType) {
        // converter not set? It is set to IConverter.class as default
        if (converterType.equals(IConverter.class)) {
            return null;
        }
        return EvaluatorTools.findInContextOrMake(converterType, eclipseContext, false);
    }

    public static void validateWidget(Object widget, String modelFieldName) {
        if (widget == null) {
            throw new NullPointerException("The widget is null. Could not bind to the field " + modelFieldName);
        }
    }

    /**
     * Adapter which triggers the listeners within the {@link ValidationContext} on validation
     * change
     */
    public ValidatorCachingAdapter validatorCachingAdapter(final IValidator validator,
            final Object annotatedFieldValue) {

        // if the validator is null - no validation changes will ever happen - return null
        if (validator == null) {
            return null;
        }

        return new ValidatorCachingAdapter(validationContext, validator, annotatedFieldValue);
    }

    /** Adapter is always OK */
    public IValidator validatorModelToTargetAlwaysOk(final IValidator validator, final Object annotatedFieldValue) {

        // if the validator is null - no validation changes will ever happen - return null
        if (validator == null) {
            return null;
        }

        return new IValidator() {
            IValidator embedded = validator;

            @Override
            public IStatus validate(Object value) {
                /*
                 * Trigger the validation but return OK
                 *
                 * Indeed this validator never returns ERROR, since during the transfer of the model value to the target the value would
                 * be rejected and the Widget would not be updated, if the validator would return ERROR.
                 * This would make it impossible to set the model to an invalid state (e.g. make the
                 * model empty), as it is initially.
                 *
                 * Disabling the Validation is not possible too, because
                 * Validator state changes trigger the listeners in ValidaitonContext on validaiton
                 * state change.
                 * Triggered listeners are responsible for giving feedback to the user, about
                 * invalid field state (e.g. by showing the warning icon).
                 *
                 */
                IStatus realStatus = embedded.validate(value);
                LOG.debug("The real status after validating value {} with annotatedFieldValue {} is {}. But OK_STATUS will be returned.", value, annotatedFieldValue, realStatus);

                return Status.OK_STATUS;
            }
        };
    }

    /**
     * Combines all validators to one. Combined validator will call all IValidators from list and
     * return OK when all of the m return ok.
     */
    public IValidator combine(final List<IValidator> validators) {
        if (validators == null) {
            return null;
        }

        // return null Validators
        Iterator<IValidator> iterator = validators.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) {
                iterator.remove();
            }
        }

        // is empty after cleaning up?
        if (validators.isEmpty()) {
            return null;
        }

        // combine all validators to one
        return new ValidatorCombined(validators);
    }

    public static final <T> T findInContextOrMakeAndStoreincontext(Class<T> clazz, IEclipseContext context) {
        T value = findInContextOrMake(clazz, context, true);
        return value;
    }

    /**
     * Use to create objects with a parameterized constructor
     *
     * @param clazz
     *            - the type
     * @param context
     * @param constructor
     *            - the constructor
     * @param constructorParameters
     *            - the parameters for the constructor
     * @return
     */
    public static final <T> T findInContextOrMake(Class<T> clazz, IEclipseContext context, Constructor<T> constructor,
            List<Object> constructorParameters) {
        Assert.isNotNull(constructor);
        Assert.isNotNull(constructorParameters);

        // try to find an existing one
        T result = context.get(clazz);

        // try to make a new one using the given consturctor
        if (result == null) {

            constructor.setAccessible(true);
            try {
                T instance = constructor.newInstance(constructorParameters);
                result = instance;
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                String message = String.format(
                        "Failed to create the instance of '%s' using constructor '%s' with parameters '%s'",
                        clazz.getSimpleName(), constructor.getClass().getSimpleName(), constructorParameters, e);
                throw new IllegalStateException(message, e);
            }
        }

        // error
        if (result == null) {
            throw new IllegalStateException(
                    String.format("Failed to retrieve class %s from context", clazz.getSimpleName()));
        }
        return result;
    }

    /** Reference {@link #findInContextOrMake(Class, IEclipseContext)} */
    public static final <T> List<T> findInContextOrMake(Class<? extends T>[] types, IEclipseContext context) {
        ArrayList<T> result = new ArrayList<>();

        for (Class<? extends T> type : types) {
            result.add(findInContextOrMake(type, context, false));
        }
        return result;
    }

    public static final <T> T findInContextOrMakeOrNull(Class<T> clazz, IEclipseContext context,
            boolean putIntoContext) {
        try {
            return findInContextOrMake(clazz, context, putIntoContext);
        } catch (InjectionException e) {
            return null;
        }
    }

    /** use for constructors without parameters */
    public static final <T> T findInContextOrMake(Class<T> clazz, IEclipseContext context, boolean putIntoContext) {
        // try to find an existing one
        T result = context.get(clazz);

        // try to make a new one
        if (result == null) {
            result = ContextInjectionFactory.make(clazz, context);

            // error
            if (result == null) {
                throw new IllegalStateException(
                        String.format("Failed to retrieve class %s from context", clazz.getSimpleName()));
            } else {
                // putIntoContext
                if (putIntoContext) {
                    context.set(clazz, result);
                }
            }
        }

        return result;
    }

    /**
     * Checks whether there is a field with corresponding getter and setter, as described in
     * {@link PropertyChangeSupport}
     *
     * @param beanClazz
     *            - the Class of the bean, where to look for the field
     * @param beanFieldNameClass
     *            - the field name
     * @return - true if it exists
     */
    public static final boolean checkWhetherBeanClassFieldWithListenersExists(Class beanClazz,
            String beanFieldNameClass) {
        // check whether the field exists
        checkWhetherPojoFieldExists(beanClazz, beanFieldNameClass);

        // check whether it is a bean field with getters and setters
        try {
            BeanPropertyHelper.getPropertyDescriptor(beanClazz, beanFieldNameClass);
        } catch (Exception exception) {
            String message = String.format("The field %s within the Class %s does not have any getter or setter.",
                    beanClazz, beanFieldNameClass);
            throw new IllegalArgumentException(message, exception);
        }

        return true;
    }

    public static final boolean checkWhetherPojoFieldExists(Class pojoClazz, String pojoFieldName) {
        // check whether the field exists in class
        try {
            getPublicOrPrivateDeclaredField(pojoClazz, pojoFieldName);
        } catch (NoSuchFieldException exception) {
            String message = String.format(
                    "The field '%s' does not exist within the Class %s. It is required by the annotation.",
                    pojoFieldName, pojoClazz);
            throw new IllegalArgumentException(message, exception);
        }

        return true;
    }

    public static final Object getPojoFieldInInstance(Class pojoClazz, String pojoFieldName, Object instance) {

        // check whether the object's class is the given class
        if (!pojoClazz.isInstance(instance)) {
            throw new IllegalArgumentException(
                    String.format("The object '%s' is not an instance of class '%s'. Not able to retrieve field '%s'",
                            instance, pojoFieldName, pojoFieldName));
        }

        try {
            return getPublicOrPrivateDeclaredField(pojoClazz, pojoFieldName).get(instance);
        } catch (IllegalArgumentException | IllegalAccessException | SecurityException | NoSuchFieldException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Widget getWidget(IWidgetExtractor widgetExtractor, Object annotatedFieldValue, String fieldName,
            Class modelClass) {
        try {
            return widgetExtractor.getWidget(annotatedFieldValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                    "The widget could not be exctracted with widgetExtractor '%s' on Model '%s', field '%s'",
                    widgetExtractor, modelClass, fieldName), e);
        }
    }

    public static Viewer getViewer(IViewerExtractor widgetExtractor, Object annotatedFieldValue, String fieldName,
            Class modelClass) {
        try {
            return widgetExtractor.getViewer(annotatedFieldValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                    "The Viewer could not be exctracted with widgetExtractor '%s' on Model '%s', field '%s'",
                    widgetExtractor, modelClass, fieldName), e);
        }
    }

    public static Text getText(ITextExtractor textExtractor, Object annotatedFieldValue, String fieldName) {
        try {
            return textExtractor.getText(annotatedFieldValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    String.format("The Text could not be exctracted with widgetExtractor '%s', field '%s'",
                            textExtractor, fieldName),
                    e);
        }
    }

    public static StructuredViewer getWidget(IStructuredViewerExtractor widgetExtractor, Object annotatedFieldValue,
            String fieldName, Class modelClass) {
        try {
            return widgetExtractor.getViewer(annotatedFieldValue);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format(
                    "The StructuredViewer could not be exctracted with widgetExtractor '%s' on Model '%s', field '%s'",
                    widgetExtractor, modelClass, fieldName), e);
        }
    }

    public static List<Field> getAllPublicOrPrivateFieldsFromInheritanceHierarchy(Class<?> type) {
        ArrayList<Field> result = new ArrayList<Field>();
        if (type == null || type.equals(Object.class)) {
            return result;
        }

        List<Field> declaredFields = Arrays.asList(type.getDeclaredFields());
        List<Field> superFields = getAllPublicOrPrivateFieldsFromInheritanceHierarchy(type.getSuperclass());

        result.addAll(declaredFields);
        result.addAll(superFields);

        return result;
    }

    /**
     * Finds all fields of the given type.
     * Finds private fields.
     * Skips static fields.
     *
     * @param type
     *            - the type which fields should be found
     * @return - the list of fields
     */
    public static List<Field> getAllNonStaticFields(Class<? extends Object> type) {
        ArrayList<Field> fields = new ArrayList<Field>();
        for (Field f : type.getDeclaredFields()) {
            f.setAccessible(true);
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                // skip static field
                continue;
            }

            fields.add(f);
        }
        if (type.getSuperclass() != Object.class) {
            fields.addAll(getAllNonStaticFields(type.getSuperclass()));
        }
        return fields;
    }

    public static Field getPublicOrPrivateDeclaredField(Class<? extends Object> type, String fieldName)
            throws NoSuchFieldException {
        for (Field field : getAllNonStaticFields(type)) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }

        throw new NoSuchFieldException(type.getSimpleName() + "." + fieldName);
    }

}
