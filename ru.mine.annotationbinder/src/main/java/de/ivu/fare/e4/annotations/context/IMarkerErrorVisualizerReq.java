package de.ivu.fare.e4.annotations.context;

/** use {@link IMarkerStatusVisualizer} */
@Deprecated
public interface IMarkerErrorVisualizerReq {
	void setValidationSuccess(Object compositeObject);
	void setValidationFailed(Object compositeObject);
	void setValidationFailed(Object compositeObject, String message);
}
