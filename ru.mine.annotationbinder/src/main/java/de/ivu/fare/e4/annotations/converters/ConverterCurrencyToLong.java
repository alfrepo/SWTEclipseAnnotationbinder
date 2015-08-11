package de.ivu.fare.e4.annotations.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.core.databinding.conversion.IConverter;

public class ConverterCurrencyToLong  implements IConverter {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(ConverterCurrencyToLong.class);
    protected NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    protected NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    public Object getFromType() {
        return String.class;
    }

    @Override
    public Object getToType() {
        return Long.class;
    }

    @Override
	public Object convert(Object fromObject) {
		String stringWithCurrency = String.valueOf(fromObject);
		try {
			Number currencyValue = currencyFormat.parse(stringWithCurrency);
			return BigDecimal.valueOf(currencyValue.doubleValue()).setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100L)).longValue();
		} catch (ParseException e) {
			try {
				Number currencyValue = numberFormat.parse(stringWithCurrency);
				return BigDecimal.valueOf(currencyValue.doubleValue()).setScale(2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100L)).longValue();
			} catch (ParseException e1) {
				return 0L;
			}

		} catch (Exception e) {
			LOG.error("Could not convert the object in {}.", getClass(), e);
			throw e;
		}
	}

}