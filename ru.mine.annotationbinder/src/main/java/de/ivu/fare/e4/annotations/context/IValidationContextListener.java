package de.ivu.fare.e4.annotations.context;

public interface IValidationContextListener {

    /** Triggered when the validated data - changes */
    void onChange(IValidationData data, ValidationContext context );
}

