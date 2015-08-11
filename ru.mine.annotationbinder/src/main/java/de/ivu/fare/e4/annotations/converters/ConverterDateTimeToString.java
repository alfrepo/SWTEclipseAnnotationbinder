package de.ivu.fare.e4.annotations.converters;

import java.text.DateFormat;

public class ConverterDateTimeToString extends ConverterDateToString {

	public ConverterDateTimeToString() {
		this.dateFormat = DateFormat.getDateTimeInstance();
	}

}
