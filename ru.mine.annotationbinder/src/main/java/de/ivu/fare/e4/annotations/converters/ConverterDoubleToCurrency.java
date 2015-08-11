package de.ivu.fare.e4.annotations.converters;

import java.text.NumberFormat;

import org.eclipse.core.databinding.conversion.IConverter;

public class ConverterDoubleToCurrency implements IConverter {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ConverterDoubleToCurrency.class);
    protected NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    public Object getFromType() {
        return Double.class;
    }

    @Override
    public Object getToType() {
        return String.class;
    }

    @Override
    public Object convert(Object fromObject) {
        if (fromObject == null) {
            return "";
        }
        try {
        	Double doubleValue = (Double) fromObject;
            return currencyFormat.format(doubleValue);
        } catch(Exception e) {
            LOG.error("Could not convert the object in {}.", getClass(), e);
            throw e;
        }
    }

}
