package de.ivu.fare.e4.annotations.eval.validation;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Composite;

import de.ivu.fare.e4.annotations.Required;

public class ValidatorReqDefault implements IValidatorKnowsWidget {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ValidatorReqDefault.class);

    private String message = Required.DEFAULT_REQUIRED_MESSAGE;
    private Object widget;

    @Override
    public void setWidget(Object widget) {
        this.widget = widget;
    }

    public void setMessage(String message){
        this.message = message;
    }

    @Override
    public IStatus validate(Object value) {
        IStatus result = Status.OK_STATUS;

        // widget enabled?
        if(widget != null && widget instanceof Composite && !((Composite)widget).isEnabled()){
            LOG.debug("Widget {} passed the validation since it is disabled", widget);
            result = Status.OK_STATUS;

        }else if(value==null || String.valueOf(value).isEmpty()){
            String valmessage = String.format("Validation failed. Value '%s' did not pass the 'isEmpty' validation. %s", value, message);
            LOG.warn(valmessage);
            result = new Status(IStatus.ERROR, ValidatorReqDefault.class.getSimpleName().toString(), message);
        }
        return result;
    }
}
