package de.ivu.fare.e4.adapters.converter;

import org.eclipse.core.databinding.conversion.IConverter;

import de.ivu.fare.e4.model.Bean;

public class ConverterBeanToText implements IConverter {

	@Override
	public Object getFromType() {
		return Bean.class;
	}

	@Override
	public Object getToType() {
		return String.class;
	}

	@Override
	public Object convert(Object fromObject) {
		return ((Bean)fromObject).textOfBean;
	}

}
