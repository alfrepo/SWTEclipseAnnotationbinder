package de.ivu.fare.e4.annotations;

import java.beans.PropertyChangeSupport;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import de.ivu.fare.e4.annotations.extractor.IWidgetExtractor;
import de.ivu.fare.e4.annotations.extractor.WidgetExtractor;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE })
@Inherited
public @interface BindWidgetItemsToModel {

    /**
     * If the annotated field contains a composite, with the {@link Widget} to
     * be filled nested in the hierarchy, then the {@link Widget} intended to be
     * bound to the model - has to somehow be extracted from this {@link Composite}. The
     * {@link IWidgetExtractor} is able to extract the {@link Widget} from the {@link Composite}.
     * 
     * @return - the {@link IWidgetExtractor} to extract the {@link Widget} from
     *         the annotated field
     */
    Class<? extends IWidgetExtractor> widgetExtractor() default WidgetExtractor.class;

    /**
     * <p>
     * The Model has to be a Bean with existing setters and getter and {@link PropertyChangeSupport}
     * . Otherwise it wont be possible to bind to it using JFace bindings.
     * </p>
     * 
     * <p>
     * If possible the model should inherit form {@link AbstractBindableModel}. Alternatively it may
     * use {@link PropertyChangeSupport} as described in JavaDoc of this class.
     * </p>
     * 
     * @return - the Model, to which's field the annotated Widget will be bound
     *         to
     */
    Class<?> modelClass();

    /**
     * The name of the field inside the model, which the widget should be bound
     * to
     * 
     * @return
     */
    String modelFieldName();

}
