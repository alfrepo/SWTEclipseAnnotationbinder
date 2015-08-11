package de.ivu.fare.e4.annotations;

import java.beans.PropertyChangeSupport;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Composite;

import de.ivu.fare.e4.annotations.converters.ConverterObjectToString;
import de.ivu.fare.e4.annotations.converters.ConverterObjectToString.DefaultConverterObjectToString;
import de.ivu.fare.e4.annotations.extractor.IStructuredViewerExtractor;
import de.ivu.fare.e4.annotations.extractor.StructuredViewerExtractor;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE })
@Inherited
public @interface BindStructuredViewerInputToModel {

    /**
     * If the annotated field contains a composite, with the {@link StructuredViewer} to
     * be filled nested in the hierarchy, then the {@link StructuredViewer} intended to be
     * bound to the model - has to somehow be extracted from this {@link Composite}. The
     * {@link IStructuredViewerExtractor} is able to extract the {@link StructuredViewer} from the
     * {@link Composite}.
     *
     * @return - the {@link IStructuredViewerExtractor} to extract the {@link StructuredViewer} from
     *         the annotated field
     */
    Class<? extends IStructuredViewerExtractor> structuredViewerExtractor() default StructuredViewerExtractor.class;

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

    /**
     * <p>
     * The {@link #modelFieldName()} retrieves a collection of
     * </p>
     *
     * @return - the Model, to which's field the annotated Widget will be bound
     *         to
     */
    Class<?> pojoClass();

    /**
     * Specify the name of the fields which should be observed inside the pojo. E.g. 'price' to observer if the modelFieldName type is Ticket and you wish to observer ticket.price
     * If only one fieldName is specified and no pojoToStringConverter is specified - the field will be retrieved via reflection.
     * If not specified the Evaluator will fall back to the default {@link #pojoToStringConverter()}.
     * If {@link #pojoToStringConverter()} is set - then pojoToStringConverter will predominate
     *
     * <p>
     * The name of the bean-field, which should be used to represent a bean instance inside the
     * widget
     * </p>
     *
     * @return - the name of the POJO-field used as label
     */
    String[] beanLabelFieldNames() default {};


    /**
     * If you wish to access the fields directly via reflection - just set the name of the field here.
     * To access the field via reflection - the object does not have to be a Bean - it may be a POJO withoud explicite getter or setter.
     * Since there are no listeners in PoJos - the binding will not react on changes within the field object
     *
     * Alternatively you can set the {@link #beanLabelFieldNames()} property, if the given object is a BEAN.Then you will get the possibility to bind to multiple properties,
     *
     * @return - the name of the reflective field
     */
    String pojoFieldNameForReflection() default "";

    /**
     * If defined - then the result of this method will predominate the {@link #beanLabelFieldNames()}
     * This converter will be used to turn a PoJo into a string.
     * Very useful, if some properties should be concatenated to a String
     */
    Class <? extends ConverterObjectToString<?>> pojoToStringConverter() default DefaultConverterObjectToString.class;
}
