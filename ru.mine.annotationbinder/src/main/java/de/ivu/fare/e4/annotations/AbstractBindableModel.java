package de.ivu.fare.e4.annotations;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Abstract class to provide PropertyChangeSupport for JavaBeans.
 *
 * <p>
 * To generate the <b>firePropertyChange()</b> method calls in setters - use SearchAndReplace with
 * RegExp.
 * </p>
 *
 * <ul>
 * <li>Find: <b>(this.+) = (\w+); </b>
 * <li>Replace with: <b>firePropertyChange("$2", $1, $1 = $2);</b>
 * </ul>
 */
public abstract class AbstractBindableModel {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(AbstractBindableModel.class);

    /**
     * Should be transient to not conflict with serialization.
     */
    private transient PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    /**
     * Adds a property change listener.
     *
     * @param listener
     *            The property change listeter
     */
    public void addPropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Removes a property change listener.
     *
     * @param listener
     *            The property change listener
     */
    public void removePropertyChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /**
     * Convenience method to fire property change events.
     *
     * @param propertyName
     *            Name of the property
     * @param oldValue
     *            Old value of the property
     * @param newValue
     *            New value of the property
     */
    protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

}
