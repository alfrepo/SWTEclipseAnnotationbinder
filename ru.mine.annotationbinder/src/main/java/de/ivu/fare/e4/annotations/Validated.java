package de.ivu.fare.e4.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

import de.ivu.fare.e4.annotations.context.IMarkerStatusVisualizer;
import de.ivu.fare.e4.annotations.eval.validation.MarkerStatusVisualizerDefault;
import de.ivu.fare.e4.annotations.extractor.ITextExtractor;
import de.ivu.fare.e4.annotations.extractor.IWidgetExtractor;
import de.ivu.fare.e4.annotations.extractor.WidgetExtractor;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE })
@Inherited
public @interface Validated {

    /**
     *
     * <p>
     * If the annotated field contains an Object, with the {@link Text} to be filled and the
     * {@link Text} is nested within the composite, then the {@link Text} intended to be bound to
     * the model - has to somehow be extracted from this Object.
     * </p>
     *
     * <p>
     * The {@link ITextExtractor} is able to extract the {@link Text} from the Object.
     * </p>
     *
     * <p>
     * To support changes the widget should generate {@link SWT#Modify events on change}
     * </p>
     *
     * @return - the {@link ITextExtractor} to extract the {@link Text} from the annotated field
     */
    Class<? extends IWidgetExtractor> widgetExtractor() default WidgetExtractor.class;


    /**
     * TODO alf
     * @return
     */
    Class<? extends IValidator>[] validators() default {};

    /**
     * How should the Status be visualized?
     *
     * @return - some logic which is able to display the errors, warnings
     */
    Class<? extends IMarkerStatusVisualizer> errorVisualizer() default MarkerStatusVisualizerDefault.class;

}
