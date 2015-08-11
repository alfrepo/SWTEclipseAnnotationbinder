package de.ivu.fare.e4.annotations.context;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;

import de.ivu.fare.e4.annotations.eval.validation.internal.ValidatorCachingAdapter;

/**
 * Object used to communicate the required field state to the environment.
 * Single ValidationData encapsulates validators for a single annotated field (annotated field must be a widget)
 *
 * @author alf
 *
 */
public class ValidationContextData implements IValidationData  {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ValidationContextData.class);

    private static final String MULTI_STATUS_MESSAGE = "Accumulated status of all validators";
    private final Object annotatedFieldValue;
    private final org.eclipse.core.databinding.Binding binding;

    // validators for all validating moments: afterGet, beforeConvert, afterConvert
    private final Set<ValidatorCachingAdapter> validatorCachingAdapters = new HashSet<>();

    public ValidationContextData(Object annotatedFieldValue, org.eclipse.core.databinding.Binding binding, List<ValidatorCachingAdapter> validatorCachingAdapter) {
        this.annotatedFieldValue = annotatedFieldValue;

        // clear the null values. There still have to be some data
        validatorCachingAdapter.removeAll(Collections.singleton(null));

        // ValidationData without validators does not make sence
        Assert.isTrue(!validatorCachingAdapter.isEmpty());

        this.validatorCachingAdapters.addAll(validatorCachingAdapter);
        this.binding = binding;
    }

    @Override
    public org.eclipse.core.databinding.Binding getBinding() {
        return binding;
    }

    @Override
    public Object getAnnotatedFieldValue() {
        return this.annotatedFieldValue;
    }

    @Override
    public Set<ValidatorCachingAdapter> getAssociatedValidationContextValidatorAdapter() {
        return validatorCachingAdapters;
    }

    @Override
    public MultiStatus getAccumulatedCachedStatusOfAllAssociatedValidators(){
        LOG.debug("Starting evaluation of embedded {} objects", ValidatorCachingAdapter.class.getSimpleName());

        MultiStatus result = new MultiStatus(ValidationContextData.class.getSimpleName(), 0, MULTI_STATUS_MESSAGE, (Throwable)null);

        for(ValidatorCachingAdapter adapter : validatorCachingAdapters){
            IStatus adapterCachedStatus = adapter.getStatusCache();
            LOG.debug("CachedValue {} in object {}", adapterCachedStatus, adapter);

            // skip the adapter which has never been used for validation
            if(adapterCachedStatus != null){
                result.merge(adapterCachedStatus);
            }
        }

        LOG.debug("Stopping evaluation of embedded {}. Result: {} ", ValidatorCachingAdapter.class.getSimpleName(), result);
        return result;
    }


    @Override
    public String toString() {
        return String.format("<%s | '%s' - Registered %s validators: %s>", this.getClass().getSimpleName(), annotatedFieldValue, validatorCachingAdapters.size(), validatorCachingAdapters);
    }
}
