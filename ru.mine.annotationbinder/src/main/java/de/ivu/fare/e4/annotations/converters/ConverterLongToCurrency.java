package de.ivu.fare.e4.annotations.converters;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.eclipse.core.databinding.conversion.IConverter;

public class ConverterLongToCurrency implements IConverter {
    
    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ConverterLongToCurrency.class);
    protected NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    public Object getFromType() {
        return Long.class;
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
        	Long longValue = (Long) fromObject;
        	Double doubleValue = BigDecimal.valueOf(longValue).divide(BigDecimal.valueOf(100L)).doubleValue();
            return currencyFormat.format(doubleValue);
        } catch(Exception e) {
            LOG.error("Could not convert the object in {}.", getClass(), e);
            throw e;
        }
    }

}
