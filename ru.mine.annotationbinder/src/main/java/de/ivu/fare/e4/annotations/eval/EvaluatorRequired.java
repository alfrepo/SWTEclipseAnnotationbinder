package de.ivu.fare.e4.annotations.eval;

import java.lang.reflect.Field;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.e4.core.contexts.IEclipseContext;

import de.ivu.fare.e4.annotations.Required;
import de.ivu.fare.e4.annotations.context.BindingContextParams;
import de.ivu.fare.e4.annotations.context.IMarkerReq;
import de.ivu.fare.e4.annotations.context.MarkerRegistry;
import de.ivu.fare.e4.annotations.eval.validation.IValidatorKnowsWidget;
import de.ivu.fare.e4.annotations.eval.validation.ValidatorReqDefault;


/**
 * Retrieves the Attributes of the annotation and bind to the text property of widget
 *
 * @author alf
 *
 */
class EvaluatorRequired {

    // will lookup required instances in this context
    @Inject
    IEclipseContext eclipseContext;

    @Inject
    DataBindingContext bindingContext;

    EvaluatorTools evaluatorTools = null;


    public boolean evaluateRequiredField(final Field annotatedField, final Object annotatedFieldValue,
            final Required requiredAnnotation) {

        // current field not annotated
        if (requiredAnnotation == null) {
            return false;
        }

        // nothing to mark as required
        if (annotatedFieldValue == null) {
            return false;
        }


        final Class<? extends IValidatorKnowsWidget>[] validatorTypes =  requiredAnnotation.validators();
        final Class<? extends IMarkerReq> reqMarkerType = requiredAnnotation.reqMarker();
        final String validationErrorMessageFromAnnotation = requiredAnnotation.validationErrorMessage();



        // instantiate the objects by type
        final List<IValidatorKnowsWidget> validators = EvaluatorTools.findInContextOrMake(validatorTypes, eclipseContext);

        // retrieve the marker from any of the possible sources
        IMarkerReq reqMarker = getMarker(reqMarkerType, annotatedFieldValue);

        // message from annotation or from marker object
        String validationErrorMessageToUse = getErrorMessage(validationErrorMessageFromAnnotation, reqMarker);

        // pass the validation message to the default validators, if there are any
        for(IValidatorKnowsWidget validator : validators){
            if(validator instanceof ValidatorReqDefault){
                ValidatorReqDefault valDef = (ValidatorReqDefault) validator;
                valDef.setMessage(validationErrorMessageToUse);
            }
            // pass on the widget to the validator, so that it can check whether the widget is enabled
            validator.setWidget(annotatedFieldValue);
        }

        // get the registries
        final BindingContextParams bindingContextParams = evaluatorTools.findInContextOrMake(BindingContextParams.class, eclipseContext, true);

        // APPLY STUFF HERE

        /* add validators to the BindingContextParams.
           from there they will be extracted and attached to an update strategy.
           during attaching to the update strategy - the validators will be wrapped to ValidatonContextValidatorAdapter and added to the ValidationContext */
        bindingContextParams.validatorsAfterGet(annotatedFieldValue).addAll(validators);

        // mark the widget with the reqMarker
        if(reqMarker != null){
            reqMarker.markAsRequired(annotatedFieldValue, validationErrorMessageToUse);
        }

        return true;
    }

    private IMarkerReq getMarker(Class<? extends IMarkerReq> reqMarkerType, Object annotatedFieldValue){
        IMarkerReq result = null;

        // 1. check if a non default req is given via Annotaiton
        if(!reqMarkerType.equals(Required.DEFAULT_REQUIREDMARKER_CLASS)){
            result = evaluatorTools.findInContextOrMakeOrNull(reqMarkerType, eclipseContext, false);
        }

        // 2. check if a reqMarker may be retrieved via registry
        if(result == null){
            result = MarkerRegistry.getMarkerUncheckedWidgetType(eclipseContext, annotatedFieldValue.getClass(), IMarkerReq.class);
        }

        // 3. try to find an existing instance in context if its still null
        if(result == null){
            result = eclipseContext.get(reqMarkerType);
        }

        // Fallback to null;
        return result;
    }

    private String getErrorMessage(String validationErrorMessageFromAnnotation, IMarkerReq reqMarker) {
        // 1. explicit error message set directly at the annotation?
        if(validationErrorMessageFromAnnotation != null && !validationErrorMessageFromAnnotation.isEmpty() && !validationErrorMessageFromAnnotation.equals(Required.DEFAULT_REQUIRED_MESSAGE)){
            return validationErrorMessageFromAnnotation;
        }

        // 2. error message provided by the ReqMarker ?
        if(reqMarker != null && reqMarker.getRequiredMessage() != null && !reqMarker.getRequiredMessage().isEmpty()){
            return reqMarker.getRequiredMessage();
        }

        // 3. fallback to the default message from the annotation
        return validationErrorMessageFromAnnotation;
    }

}
