package de.ivu.fare.e4.annotations.eval.validation;

import org.eclipse.core.runtime.IStatus;

import de.ivu.fare.e4.annotations.context.IMarkerStatusVisualizer;

public class MarkerStatusVisualizerDefault implements IMarkerStatusVisualizer {

    @Override
    public void visualizeStatusOnWidget(Object annotatedFieldValue, IStatus status) {
        // nothing
    }
}
