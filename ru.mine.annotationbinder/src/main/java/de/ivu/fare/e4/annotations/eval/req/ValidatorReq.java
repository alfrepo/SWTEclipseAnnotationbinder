package de.ivu.fare.e4.annotations.eval.req;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

@Creatable
public class ValidatorReq implements IValidatorReq {

    @Override
    public boolean validate(Widget widget) {

        // maybe we can recognize some widgets on default
        if (widget instanceof Text) {
            return !((Text) widget).getText().isEmpty();

        } else if (widget instanceof Button) {
            return ((Button) widget).getSelection();

        }
        return true;
    }
}
