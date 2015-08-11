package de.ivu.fare.e4.annotations.converters;

import java.text.DateFormat;
import java.util.Locale;

public class ConverterTimeToString extends ConverterDateToString {

	public ConverterTimeToString() {
		this.dateFormat = DateFormat.getTimeInstance(DateFormat.DEFAULT, Locale.getDefault());
	}

}
