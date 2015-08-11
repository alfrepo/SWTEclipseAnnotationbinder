package de.ivu.fare.e4.annotations.eval.validation.internal;

import java.util.List;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ValidatorCombined implements IValidator {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ValidatorCombined.class);
    private final List<IValidator> validators;

    public ValidatorCombined(final List<IValidator> validators){
        this.validators = validators;
    }

    @Override
    public IStatus validate(Object value) {
        IStatus result = Status.OK_STATUS;
        LOG.debug("Starting validation of a combined validatior with embedded Validators: {}.", validators);

        for (IValidator validator : validators) {
            IStatus status = validator.validate(value);
            LOG.debug("Validating: {}. Validation result: {}", validator.getClass().getSimpleName(), status);

            if (status.getException() != null) {
                // log exceptions which occur during the validation
                LOG.error("Exception occured during validation", status.getException());
            }

            if (!status.isOK()) {
                LOG.debug("Embedded validator {} returned status {}", validator, status );
                result = status;
                break;
            }
        }

        LOG.debug("Stopped validating a combined vlaidator. Validation result: {}", result);


        return result;
    }

    @Override
    public String toString() {
        return String.format("<%s>combines validators: %s</CombinedValidator(%s)>",this.getClass().getSimpleName(), validators, this.getClass().getSimpleName());
    }
};
