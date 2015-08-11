package de.ivu.fare.e4.annotations.converters;

import org.eclipse.e4.core.di.annotations.Creatable;

/**
 * Used to convert any Object to String.
 * May be used to concat som object properties to one string
 * @author alf
 *
 * @param <T> - the type of POJO which should be converted to String
 */
@Creatable
public abstract class ConverterObjectToString<T> {

    public Object getFromType() {
        return Object.class;
    }

    public final Object getToType() {
        return String.class;
    }

    public abstract String convert(T fromObject, int columnIndex);


    /**
     * The default implementation to use
     * @author alf
     *
     */
    public static class DefaultConverterObjectToString extends ConverterObjectToString<Object>{
        @Override
        public String convert(Object fromObject, int columnIndex) {
            if(fromObject == null){
                return "";
            }
            return fromObject.toString();
        }
    }
}
