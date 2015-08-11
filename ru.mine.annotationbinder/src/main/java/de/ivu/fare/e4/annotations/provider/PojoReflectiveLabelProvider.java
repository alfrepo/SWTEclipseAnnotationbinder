package de.ivu.fare.e4.annotations.provider;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.ivu.fare.e4.annotations.eval.EvaluatorTools;

public class PojoReflectiveLabelProvider implements ILabelProvider {

    Class<?> pojoClazz;
    String fieldName;

    public PojoReflectiveLabelProvider(Class<?> pojoClazz, String fieldname) {
        this.pojoClazz = pojoClazz;
        this.fieldName = fieldname;
    }

    @Override
    public void addListener(ILabelProviderListener listener) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {

    }

    @Override
    public Image getImage(Object element) {
        return null;
    }

    @Override
    public String getText(Object element) {
        Object instance = EvaluatorTools.getPojoFieldInInstance(pojoClazz, fieldName, element);
        if(instance == null){
            return null;
        }
        return instance.toString();
    }

}
