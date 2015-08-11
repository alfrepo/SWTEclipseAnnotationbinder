package de.ivu.fare.e4.annotations.context;

import java.util.Set;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.MultiStatus;

import de.ivu.fare.e4.annotations.eval.validation.internal.ValidatorCachingAdapter;

/**
 * With every single annotated field there may exist many {@link IValidator} objects.
 * AfterGetValidator, afterConvertValidator, beforeSet.
 * And this for both directions: targetToModel and modelToTarget.
 * The validators cache their validation stati.
 * All the validators, independant of their purpose are stored in this data object, are stored in this object,
 * so that the evaluation of all cached Stati may be done from outside, e.g. to check whether the validation of a page is fulfilled.
 *
 * */
public interface IValidationData {

    /** The field value, which was annotated*/
    Object getAnnotatedFieldValue();

    /** The Validators, which do cache the last value */
    Set<ValidatorCachingAdapter> getAssociatedValidationContextValidatorAdapter();

    /** The accumulated status of all validators */
    MultiStatus getAccumulatedCachedStatusOfAllAssociatedValidators();

    Binding getBinding();


}
