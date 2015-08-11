package de.ivu.fare.e4.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.e4.core.contexts.IEclipseContext;

import de.ivu.fare.e4.annotations.context.IMarkerReq;
import de.ivu.fare.e4.annotations.context.MarkerRegistry;
import de.ivu.fare.e4.annotations.eval.validation.IValidatorKnowsWidget;
import de.ivu.fare.e4.annotations.eval.validation.ValidatorReqDefault;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE })
@Inherited
public @interface Required {

    public static final String DEFAULT_REQUIRED_MESSAGE = "Required";
    public static final Class<? extends IMarkerReq> DEFAULT_REQUIREDMARKER_CLASS = IMarkerReq.class;

    /**
     * The Validators, which are responsible for checking, whether the Requirenment on the annotated field is fulfilled
     * @return
     */
    Class<? extends IValidatorKnowsWidget>[] validators() default {ValidatorReqDefault.class};

    /**
     * How should the given widget be marked as required?
     * Maybe the label has an othe color, or a (*) is added to the label.
     * The marker class returned by this method will be looked up in the {@link IEclipseContext}.
     * If no instance is found - then an instance of a {@link MarkerRegistry} will be looked up in
     * context and it will be asked about marking the given object as required.
     *
     * @return - the marker which is able to visualize the requirenmen to the user
     */
    Class<? extends IMarkerReq> reqMarker() default IMarkerReq.class;


      /**
      * Which message to display when validation fails
      *
      * @return the message
      */
     String validationErrorMessage() default DEFAULT_REQUIRED_MESSAGE;


}
