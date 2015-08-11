package de.ivu.fare.e4.annotations.eval.validation;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ValidatorDefault implements IValidator {

    @Override
    public IStatus validate(Object value) {
        return Status.OK_STATUS;
    }

}
