package de.ivu.fare.e4.annotations.context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;


public class ValidationContext {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ValidationContext.class);

    // annotatedFieldValue - IValidationData
    private final Map<Object, IValidationData> validationData = Collections.synchronizedMap( new HashMap<Object, IValidationData>() );
    private final List<IValidationContextListener> listeners = Collections.synchronizedList(new ArrayList<IValidationContextListener>());

    public void addListener(IValidationContextListener l) {
        this.listeners.add(l);
    }

    public void removeListener(IValidationContextListener l) {
        this.listeners.remove(l);
    }

    public List<IValidationData> getAllValidationData() {
        return new ArrayList<IValidationData>(validationData.values());
    }

    public void notifyValidationStateChange(IValidationData validationData) {
        for (IValidationContextListener l : listeners) {
            l.onChange(validationData, this);
        }
    }

    public void notifyValidationStateChange(Object annotationFieldvalue) {
        IValidationData data = validationData.get(annotationFieldvalue);
        if(data != null){
            notifyValidationStateChange(data);
        }else{
            LOG.error("Failed to retrieve IValidationData for the annotationFieldvalue {}. Notification about validation changes will not happen.", data);
        }
    }


    public void validateModelToTarget(){
        for(IValidationData v : getAllValidationData()){
            v.getBinding().validateModelToTarget();
        }
    }


    public void addValidationData(IValidationData validationData) {
        if(validationData == null){
            return;
        }

        Object annotatedFieldValue = validationData.getAnnotatedFieldValue();
        Assert.isNotNull(annotatedFieldValue);
        this.validationData.put(annotatedFieldValue, validationData);
    }

    public void removeValidationData(IValidationData validationData) {
        this.validationData.remove(validationData.getAnnotatedFieldValue());
    }

    @Override
    public String toString() {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.format("<%s>", this.getClass().getSimpleName()) );
        stringBuilder.append(String.format("%s listeners, %s validationData. ", listeners.size(), validationData.size()));
        stringBuilder.append(String.format("Listeners: %s, ValidationData: %s", listeners, validationData));
        stringBuilder.append(String.format("</%s>", this.getClass().getSimpleName()) );

        return stringBuilder.toString();
    }


}
