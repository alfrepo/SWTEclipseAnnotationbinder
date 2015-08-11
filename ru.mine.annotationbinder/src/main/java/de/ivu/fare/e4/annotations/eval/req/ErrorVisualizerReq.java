package de.ivu.fare.e4.annotations.eval.req;

public class ErrorVisualizerReq implements IErrorVisualizerReq {

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
