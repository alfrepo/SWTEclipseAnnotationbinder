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

import de.ivu.fare.e4.annotations.extractor.IStructuredViewerExtractor;
import de.ivu.fare.e4.annotations.extractor.IViewerExtractor;
import de.ivu.fare.e4.annotations.extractor.ViewerExtractor;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE })
@Inherited
public @interface BindViewerSingleSelectionToModel {

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
    Class<? extends IViewerExtractor> viewerExtractor() default ViewerExtractor.class;

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
