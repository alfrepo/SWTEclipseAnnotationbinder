package de.ivu.fare.e4.annotations.extractor;

import org.eclipse.jface.viewers.StructuredViewer;

/**
 * Class which is able to extract a StructuredViewer from the given object.
 * Is used locally (e.g. in field annotations to convert fields to {@link StructuredViewer}), where
 * it is known which kind of Object is passed to it,
 * 
 * @author alf
 *
 */
public class StructuredViewerExtractor implements IStructuredViewerExtractor {
    @Override
    public StructuredViewer getViewer(Object object) {
        return (StructuredViewer) object;
    }
}
