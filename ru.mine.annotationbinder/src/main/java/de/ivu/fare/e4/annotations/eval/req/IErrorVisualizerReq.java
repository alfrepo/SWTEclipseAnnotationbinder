package de.ivu.fare.e4.annotations.eval.req;

public interface IErrorVisualizerReq {
	void setValidationSuccess(Object compositeObject);
	void setValidationFailed(Object compositeObject);
	void setValidationFailed(Object compositeObject, String message);
}
