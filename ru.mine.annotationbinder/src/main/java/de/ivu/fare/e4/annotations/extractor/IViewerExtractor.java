package de.ivu.fare.e4.annotations.extractor;

import org.eclipse.jface.viewers.Viewer;

/**
 * Class which is able to extract a Viewer from the given object.
 * Is used locally (e.g. in field annotations to convert fields to {@link Viewer}), where
 * it is known which kind of Object is passed to it,
 * 
 * @author alf
 *
 */
public interface IViewerExtractor {
    public Viewer getViewer(Object object);
}
