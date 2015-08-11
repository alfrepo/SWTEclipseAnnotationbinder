package de.ivu.fare.e4.annotations.eval.validation.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;

import de.ivu.fare.e4.annotations.context.ValidationContext;

/**
 * Adapter used in {@link ValidationContext}, to query the current validation state.
 * Adapter caches the current validation status.
 * The cached status changes, when the validator is used by JFace binding to validate its widget.
 * Only one widget is associated with a single adapter - the {@link #annotatedFieldValue}
 * Only one ValidationContext is associated with a single adapter. Adapter must know which validationContext to trigger on validation changes.
 *
 * @author alf
 *
 */
public class ValidatorCachingAdapter implements IValidator{

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory
            .getLogger(ValidatorCachingAdapter.class);

    private final ValidationContext validationContext;
    private final IValidator embedded;
    private final Object annotatedFieldValue;

    // cache
    private IStatus statusCachedOldValue = null;

    /**
     * @param validationContext - needed to notify listeners on validation
     * @param embedded - the embedded validator, which encapsulates the validation logic
     * @param annotatedFieldValue - the annotated field value
     */
    public ValidatorCachingAdapter(ValidationContext validationContext, IValidator embedded, Object annotatedFieldValue) {
        this.validationContext = validationContext;
        this.embedded = embedded;
        this.annotatedFieldValue = annotatedFieldValue;
    }

    @Override
    public IStatus validate(Object value) {
        LOG.debug("Validation Widget's '{}' value '{}'. Embedded validator {}", annotatedFieldValue, value, embedded);

        IStatus statusNewValue = embedded.validate(value);

        // check for changes
        if(statusNewValue == statusCachedOldValue || (statusNewValue!= null && statusNewValue.equals(statusCachedOldValue))){
            LOG.debug("Validation status {}. No Changes in cached value.", statusNewValue);
            // no changes because the values are equal

        }else{
            LOG.debug("Validation status {}. Status changed. Old status was {}", statusNewValue, statusCachedOldValue);

            // cache the new value
            statusCachedOldValue = statusNewValue;

            // notify the validation Registry about changes
            notifyValidationRegistryAboutChanges(annotatedFieldValue);

            // notify this validator's personal listeners about changes
            notifyListeners(statusNewValue);
        }

        return statusNewValue;
    }

    public IStatus getStatusCache(){
        return statusCachedOldValue;
    }

    private void notifyValidationRegistryAboutChanges(Object annotatedFieldValue) {
        // notify about validation changes by the value of the annotated field
        validationContext.notifyValidationStateChange(annotatedFieldValue);
    }

    @Override
    public String toString() {
        return String.format("<%s>Embedded_validator:%s-Annotated_object:%s </%s>",this.getClass().getSimpleName(), embedded, annotatedFieldValue, this.getClass().getSimpleName());
    }


    private final List<Listener> listeners = new ArrayList<>();

    public void add(Listener l){
        this.listeners.add(l);
    }

    public void remove(Listener l){
        this.listeners.remove(l);
    }

    private void notifyListeners(IStatus status){
        for(Listener l:listeners){
            l.onValidationStatusChange(status, annotatedFieldValue);
        }
    }

    /** This listeners may be added to the CachedValidator, to recognize the validaiton changes */
    public interface Listener{
        void onValidationStatusChange(IStatus newStatus, Object annotatedFieldValue);
    }
}
