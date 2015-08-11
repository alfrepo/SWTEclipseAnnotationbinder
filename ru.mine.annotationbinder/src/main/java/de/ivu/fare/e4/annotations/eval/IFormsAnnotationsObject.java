package de.ivu.fare.e4.annotations.eval;

/**
 * Marker interface, which is used to notify the {@link AnnotationsForWidgetsEvaluator} about new object with forms annotations.
 *
 * Usage:
 * <ul>
 * <li> This Interface should mark some class with Forms annotations in it
 * <li> An Instance, which's Fields should be evaluated should be injected into the context
 * <li> Instances of this Interface will be tracked by an {@link AnnotationsForWidgetsEvaluator} and evaluated on inject
 *
 * @author skip
 *
 */
public interface IFormsAnnotationsObject {
	// TODO alf implement listening in COntext for this object
}
