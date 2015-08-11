package de.ivu.fare.e4.annotations.eval.validation;

import de.ivu.fare.e4.annotations.context.IMarkerErrorVisualizerReq;

public class MarkerErrorVisualizerReq implements IMarkerErrorVisualizerReq {

	@Override
	public void setValidationSuccess(Object compositeObject) {
		// dummy does nothing
	}

	@Override
	public void setValidationFailed(Object compositeObject) {
		// dummy does nothing		
	}

	@Override
	public void setValidationFailed(Object compositeObject, String message) {
		// dummy does nothing
	}
	
}
