package de.ivu.fare.e4.annotations.eval;

import java.lang.reflect.Field;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;

import de.ivu.fare.e4.annotations.BindStructuredViewerInputToModel;
import de.ivu.fare.e4.annotations.BindViewerSingleSelectionToModel;
import de.ivu.fare.e4.annotations.BindWidgetEnabilityToModel;
import de.ivu.fare.e4.annotations.BindWidgetImageToModel;
import de.ivu.fare.e4.annotations.BindWidgetItemsToModel;
import de.ivu.fare.e4.annotations.BindWidgetSelectionToModel;
import de.ivu.fare.e4.annotations.BindWidgetTextToModel;
import de.ivu.fare.e4.annotations.BindWidgetVisibilityToModel;
import de.ivu.fare.e4.annotations.Required;
import de.ivu.fare.e4.annotations.Validated;
import de.ivu.fare.e4.annotations.context.ValidationContext;
import de.ivu.fare.e4.annotations.extractor.IWidgetExtractor;

/**
 * Evaluates annotations, which are applied to bind forms to the model.
 * Does the binding.
 *
 * <ul>
 * <li>The instance of the Model is found in injected {@link IEclipseContext} by class.
 * <li>From the instance of the Model - a model-field is retrieved by name.
 * <li>An instance of {@link IWidgetExtractor} is found in injected {@link IEclipseContext} by
 * class.
 * <li>Using the {@link IWidgetExtractor} a widget is retrieved from the annotated field. On Default
 * the field is treated as widget.
 * <li>At least the widget is bound to the model-field
 * </ul>
 *
 * @author alf
 *
 */
@SuppressWarnings("restriction")
public class AnnotationsForWidgetsEvaluator {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AnnotationsForWidgetsEvaluator.class);

    private static final String ANNOTATIONS_FOR_WIDGETS_EVALUATOR_CONTEXT_NAME = "AnnotationsForWidgetsEvaluator context";

    // will retrieve required instances from this context
    private IEclipseContext eclipseContext;

    // will use this context to bind widgets to the model
    private DataBindingContext bindingContext = new DataBindingContext();

    // will store all validaion data, which may be used to listen for validaiton changes
    private ValidationContext validationContext;

    public AnnotationsForWidgetsEvaluator(IEclipseContext context) {
        if (context == null) {
            throw new IllegalArgumentException("invalid context null");
        }

        // create a child context
        this.eclipseContext = context.createChild(ANNOTATIONS_FOR_WIDGETS_EVALUATOR_CONTEXT_NAME);
        eclipseContext.set(DataBindingContext.class, bindingContext);

        // if necessary create the ValidationContext
        validationContext = context.get(ValidationContext.class);
        if(validationContext == null){
            // create the ValidationContext inside the parent-context, to make it visible for context.get(ValidationContext.class);
            validationContext = ContextInjectionFactory.make(ValidationContext.class, eclipseContext);
            eclipseContext.set(ValidationContext.class, validationContext);
        }
    }

    /**
     * Checks all fields within the object for known annotations. Evaluates them and does the
     * binding.
     *
     * @param target
     *            - taget object which contains annotated fields
     */
    public void evaluate(Object target) {
        // evaluate text bindings
        try {
            LOG.debug("ValidationContext before evaluation: {}", validationContext );
            evaluateAllBinding(target);
            LOG.debug("Validation Context after evaluation: {}", validationContext );
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
    }

    private void evaluateAllBinding(final Object object) throws IllegalArgumentException, IllegalAccessException,
            NoSuchFieldException, SecurityException {

        // retrieve the BindingText annotations and iterate them
        for (Field annotatedField : EvaluatorTools.getAllPublicOrPrivateFieldsFromInheritanceHierarchy(object.getClass())) {
            // allow to access private fields
            annotatedField.setAccessible(true);

            // retrieve annotated object
            Object annotatedFieldValue = annotatedField.get(object);


            // evaluate the IValidator producing annotations first, so that they may be reused within the bindings
            /*
             * Validated and Required annotations have to be evaluated before binding.
             * They fill the BindingContextProps, so that the binding may use them
             */
            evaluateValidatedAnnotation(annotatedField, annotatedFieldValue);
            evaluateRequiredAnnotationToMarkFields(annotatedField, annotatedFieldValue);

            // evaluate annotations
            evaluateTextBinding(annotatedField, annotatedFieldValue);
            evaluateItemsBinding(annotatedField, annotatedFieldValue);
            evaluateSelectionBinding(annotatedField, annotatedFieldValue);
            evaluateEnabilityBinding(annotatedField, annotatedFieldValue);
            evaluateStructuredViewerInput(annotatedField, annotatedFieldValue);
            evaluateViewerSingleSelection(annotatedField, annotatedFieldValue);
            evaluateVisibilityBinding(annotatedField, annotatedFieldValue);
            evaluateImageBinding(annotatedField, annotatedFieldValue);

//            evaluateRequiredAnnotationToMarkFields(annotatedField, annotatedFieldValue);
        }
    }

    private void evaluateViewerSingleSelection(Field annotatedField, Object annotatedFieldValue)
            throws NoSuchFieldException, SecurityException {
        // annotation
        BindViewerSingleSelectionToModel bindViewerSingleSelectionToModel = annotatedField
                .getAnnotation(BindViewerSingleSelectionToModel.class);

        // binding to items
        EvaluatorViewerSingleSelection evaluatorViewerSingleSelection = ContextInjectionFactory.make(
                EvaluatorViewerSingleSelection.class, eclipseContext);
        evaluatorViewerSingleSelection.bindViewersSingleSelectionAttribute(annotatedField, annotatedFieldValue,
                bindViewerSingleSelectionToModel);
    }

    private void evaluateStructuredViewerInput(Field annotatedField, Object annotatedFieldValue)
            throws NoSuchFieldException, SecurityException {
        // annotation
        BindStructuredViewerInputToModel bindStructuredViewerInputToModeleld = annotatedField
                .getAnnotation(BindStructuredViewerInputToModel.class);

        // binding to items
        EvaluatorStructuredViewerInput evaluatorStructuredViewerInput = ContextInjectionFactory.make(
                EvaluatorStructuredViewerInput.class, eclipseContext);
        evaluatorStructuredViewerInput.bindStructuredViewersInputAttribute(annotatedField, annotatedFieldValue,
                bindStructuredViewerInputToModeleld);
    }

    private void evaluateImageBinding(Field annotatedField, Object annotatedFieldValue)
            throws NoSuchFieldException, SecurityException {
        // annotation
        BindWidgetImageToModel annotation = annotatedField
                .getAnnotation(BindWidgetImageToModel.class);

        // binding to items
        EvaluatorImage evaluator = ContextInjectionFactory.make(EvaluatorImage.class, eclipseContext);
        evaluator.bindWidgetsImageAttribute(annotatedField, annotatedFieldValue,  annotation);
    }

    private void evaluateVisibilityBinding(Field annotatedField, Object annotatedFieldValue)
            throws NoSuchFieldException, SecurityException {
        // annotation
        BindWidgetVisibilityToModel annotation = annotatedField
                .getAnnotation(BindWidgetVisibilityToModel.class);

        // binding to items
        EvaluatorVisibility evaluator = ContextInjectionFactory.make(EvaluatorVisibility.class, eclipseContext);
        evaluator.bindWidgetsVisibilityAttribute(annotatedField, annotatedFieldValue,  annotation);
    }

    private void evaluateEnabilityBinding(Field annotatedField, Object annotatedFieldValue)
            throws NoSuchFieldException, SecurityException {
        // annotation
        BindWidgetEnabilityToModel annotation = annotatedField
                .getAnnotation(BindWidgetEnabilityToModel.class);

        // binding to items
        EvaluatorEnability evaluator = ContextInjectionFactory.make(EvaluatorEnability.class,
                eclipseContext);
        evaluator.bindWidgetsEnabilityAttribute(annotatedField, annotatedFieldValue,
                annotation);
    }

    private void evaluateSelectionBinding(Field annotatedField, Object annotatedFieldValue)
            throws NoSuchFieldException, SecurityException {
        // annotation
        BindWidgetSelectionToModel bindWidgetSelectionToModel = annotatedField
                .getAnnotation(BindWidgetSelectionToModel.class);

        // binding to items
        EvaluatorSelection evaluatorSelection = ContextInjectionFactory.make(EvaluatorSelection.class,
                eclipseContext);
        evaluatorSelection.bindWidgetsSelectionAttribute(annotatedField, annotatedFieldValue,
                bindWidgetSelectionToModel);
    }

    private void evaluateItemsBinding(Field annotatedField, Object annotatedFieldValue) throws NoSuchFieldException,
            SecurityException {
        // annotation
        BindWidgetItemsToModel bindWidgetItemsToModel = annotatedField.getAnnotation(BindWidgetItemsToModel.class);

        // binding to items
        EvaluatorItems evaluatorItems = ContextInjectionFactory.make(EvaluatorItems.class, eclipseContext);
        evaluatorItems.bindWidgetsItemAttribute(annotatedField, annotatedFieldValue, bindWidgetItemsToModel);
    }

    private void evaluateTextBinding(Field annotatedField, Object annotatedFieldValue) throws NoSuchFieldException,
            SecurityException {
        // annotation
        BindWidgetTextToModel bindingToTextAnnotation = annotatedField.getAnnotation(BindWidgetTextToModel.class);

        // binding to text
        EvaluatorText evaluatorText = ContextInjectionFactory.make(EvaluatorText.class, eclipseContext);
        evaluatorText.bindWidgetsTextAttribute(annotatedField, annotatedFieldValue, bindingToTextAnnotation);
    }

    private void evaluateRequiredAnnotationToMarkFields(Field annotatedField, Object annotatedFieldValue) {
        // annotation
        Required requiredAnnotation = annotatedField.getAnnotation(Required.class);

        // binding to items
        EvaluatorRequired evaluator = ContextInjectionFactory.make(EvaluatorRequired.class, eclipseContext);

        evaluator.evaluateRequiredField(annotatedField, annotatedFieldValue, requiredAnnotation);
    }

    private void evaluateValidatedAnnotation(Field annotatedField, Object annotatedFieldValue) {
        // annotation
        Validated validatedAnnotation = annotatedField.getAnnotation(Validated.class);

        // binding to items
        EvaluatorValidated evaluator = ContextInjectionFactory.make(EvaluatorValidated.class, eclipseContext);
        evaluator.evaluateValidatedField(annotatedField, annotatedFieldValue, validatedAnnotation);
    }

}
