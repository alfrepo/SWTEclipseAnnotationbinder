package de.ivu.fare.e4.annotations.eval.validation;

import org.eclipse.core.databinding.validation.IValidator;

public interface IValidatorKnowsWidget extends IValidator {

    /**
     * The Widget will be passed here.
     * It may be stored to check, whether the widget is enabled or disabled to skip the validation of disabled widgets
     *
     * @param widget
     */
    void setWidget(Object widget);

}
