package de.ivu.fare.e4.annotations.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.validation.IValidator;

/**
 * This container stores the parameter, which will be used to initialize the bindings to the model.
 * This object is a container which is filled from different annotations and used, when the binding is established.
 *
 * Compared to {@link ValidationContext} this container - is local to the annotation evaluation
 *
 * The key (annotatedFieldValue) is always the value of the field, which has been annotated with the annotation, which caused the BindingContext filling
 *
 * The validatorBeforeSet, validatorAfterConvert, validatorAfterGet will all be used to validate both of the possible directions:
 * Target to Model, Model to Target
 *
 * @author alf
 *
 */
public class BindingContextParams {

    private final Map<Object, IMarkerStatusVisualizer> markerStatusVisualizer = Collections.synchronizedMap(new HashMap<Object, IMarkerStatusVisualizer>());

    public IMarkerStatusVisualizer markerStatusVisualizer(Object annotatedFieldValue){
        return markerStatusVisualizer.get(annotatedFieldValue);
    }

    // the key is always the INSTANCE, which was found inside the annotated field
    private final Map<Object, IConverter> converterModelToTarget = Collections.synchronizedMap(new HashMap<Object, IConverter>());
    private final Map<Object, IConverter> converterTargetToModel = Collections.synchronizedMap(new HashMap<Object, IConverter>());

    private final Map<Object, List<IValidator>> validatorAfterGet = Collections.synchronizedMap(new HashMap<Object, List<IValidator>>());
    private final Map<Object, List<IValidator>> validatorAfterConvert = Collections.synchronizedMap(new HashMap<Object, List<IValidator>>());
    private final Map<Object, List<IValidator>> validatorBeforeSet = Collections.synchronizedMap(new HashMap<Object, List<IValidator>>());

    public List<IValidator> validatorsAfterGet(Object annotatedFieldValue){
        List<IValidator> list = validatorAfterGet.get(annotatedFieldValue);
        if(list == null){
            list = new ArrayList<>();
            validatorAfterGet.put(annotatedFieldValue, list);
        }
        return list;
    }

    public List<IValidator> validatorsAfterConvert(Object annotatedFieldValue){
        List<IValidator> list = validatorAfterConvert.get(annotatedFieldValue);
        if(list == null){
            list = new ArrayList<>();
            validatorAfterConvert.put(annotatedFieldValue, list);
        }
        return list;
    }

    public List<IValidator> validatorsBeforeSet(Object annotatedFieldValue){
        List<IValidator> list = validatorBeforeSet.get(annotatedFieldValue);
        if(list == null){
            list = new ArrayList<>();
            validatorBeforeSet.put(annotatedFieldValue, list);
        }
        return list;
    }

    public IConverter converterTargetToModel(Object annotatedFieldValue){
        return converterTargetToModel.get(annotatedFieldValue);
    }

    public void converterTargetToModelSet(IConverter converterNew, Object annotatedFieldValue){
        converterTargetToModel.put(annotatedFieldValue, converterNew);
    }

    public IConverter converterModelToTarget(Object annotatedFieldValue){
        return converterModelToTarget.get(annotatedFieldValue);
    }

    public void converterModelToTargetSet(IConverter converterNew, Object annotatedFieldValue){
        converterModelToTarget.put(annotatedFieldValue, converterNew);
    }
}
