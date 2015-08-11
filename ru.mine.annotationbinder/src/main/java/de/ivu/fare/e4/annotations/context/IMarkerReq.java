package de.ivu.fare.e4.annotations.context;

/**
 *
 * @author alf
 *
 * @param <T> - the type of the annotated field
 */
public interface IMarkerReq<T> extends IMarker {

    /**
     * This class knows the following type of widget which it is able to handle
     *
     * @return the type
     */
    Class<T> getKnownWidgetType();

    /** Marks the widget as required */
    boolean markAsRequired(T annotatedFieldValue, String message);

    /** May return a message, which will be displayed, when the Requirenment is not fulfilled. Will be passed to {@link #markAsRequired(Object, String)} then.
     *  If a reqMessage is given directly at the annotation - it will be prefered over the message returned by this method */
    String getRequiredMessage();
}
