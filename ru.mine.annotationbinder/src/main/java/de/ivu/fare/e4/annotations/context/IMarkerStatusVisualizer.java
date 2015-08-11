package de.ivu.fare.e4.annotations.context;

import org.eclipse.core.runtime.IStatus;

/**
 *
 * @author alf
 *
 * @param <T> - the type of the annotated field
 */
public interface IMarkerStatusVisualizer<T> extends IMarker {

    void visualizeStatusOnWidget(T annotationFieldValue, IStatus status);
}
