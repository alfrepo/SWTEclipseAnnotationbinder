package de.ivu.fare.e4.annotations.converters;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.eclipse.core.databinding.conversion.IConverter;

public class ConverterDateToString implements IConverter{

	protected DateFormat dateFormat =  DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

	@Override
	public Object getFromType() {
		return Date.class;
	}

	@Override
	public Object getToType() {
		return String.class;
	}

	@Override
	public Object convert(Object fromObject) {
	    if(fromObject == null){
	        return "";
	    }
		Date date = (Date) fromObject;
		return dateFormat.format(date);
	}

}
