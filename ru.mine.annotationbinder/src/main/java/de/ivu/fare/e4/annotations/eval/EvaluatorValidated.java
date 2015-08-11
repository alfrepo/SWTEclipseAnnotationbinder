package de.ivu.fare.e4.annotations.eval;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Widget;

import de.ivu.fare.e4.annotations.Validated;
import de.ivu.fare.e4.annotations.context.BindingContextParams;
import de.ivu.fare.e4.annotations.context.IMarkerStatusVisualizer;
import de.ivu.fare.e4.annotations.context.ValidationContext;
import de.ivu.fare.e4.annotations.extractor.IWidgetExtractor;

/**
 * Retrieves the Attributes of the annotation and bind to the text property of widget
 *
 * @author alf
 *
 */
class EvaluatorValidated {

    // will lookup required instances in this context
    @Inject
    IEclipseContext eclipseContext;

    @Inject
    DataBindingContext bindingContext;

    EvaluatorTools evaluatorTools = null;


    public boolean evaluateValidatedField(final Field annotatedField, final Object annotatedFieldValue, final Validated validatedAnnotation){

        // current field not annotated
        if (validatedAnnotation == null) {
            return false;
        }

        // nothing to mark as required
        if (annotatedFieldValue == null) {
            return false;
        }


        // TODO alf - try to retrieve the IMarkerStatusVisualizer from MarkerRegistry too
        final IMarkerStatusVisualizer errorVisualizer = EvaluatorTools.findInContextOrMake(
                validatedAnnotation.errorVisualizer(), eclipseContext, false);
        final List<IValidator> validators = EvaluatorTools.findInContextOrMake(validatedAnnotation.validators(),
                eclipseContext);
        final IWidgetExtractor widgetExtractor = EvaluatorTools.findInContextOrMake(
                validatedAnnotation.widgetExtractor(), eclipseContext, false);


        // Extract the widget
        final Widget widget = EvaluatorTools.getWidget(widgetExtractor, annotatedFieldValue, annotatedField.getName(), null);

        final ValidationContext validationContext = EvaluatorTools.findInContextOrMakeAndStoreincontext(ValidationContext.class, eclipseContext.getParent());
        //CONTEXTFILL BindingContextParams.class
        eclipseContext.set(ValidationContext.class, validationContext);

        final BindingContextParams bindingContextParams = EvaluatorTools.findInContextOrMakeAndStoreincontext(BindingContextParams.class, eclipseContext.getParent());
        //CONTEXTFILL BindingContextParams.class
        eclipseContext.set(BindingContextParams.class, bindingContextParams);

        // add Validators to the bindingContextParams
        bindingContextParams.validatorsAfterGet(annotatedFieldValue).addAll(validators);

        // TODO alf clean up
//        bindingContextParams.fromModelToTarget.validatorsAfterGet(annotatedFieldValue).addAll(validators);
//        bindingContextParams.fromTargetToModel.validatorsAfterGet(annotatedFieldValue).addAll(validators);

        return true;
    }

}
