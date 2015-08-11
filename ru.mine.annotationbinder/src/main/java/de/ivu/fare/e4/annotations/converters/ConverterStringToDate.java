package de.ivu.fare.e4.annotations.converters;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.eclipse.core.databinding.conversion.IConverter;

public class ConverterStringToDate implements IConverter{

	protected DateFormat dateFormat =  DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());

	@Override
	public Object getFromType() {
		return String.class;
	}

	@Override
	public Object getToType() {
		return Date.class;
	}


	@Override
	public Object convert(Object fromObject) {
	    if(fromObject == null || fromObject.toString().isEmpty()){
	        return null;
	    }

		String dateString = (String) fromObject;
		Date result = null;
		try {
			result = dateFormat.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}



}
