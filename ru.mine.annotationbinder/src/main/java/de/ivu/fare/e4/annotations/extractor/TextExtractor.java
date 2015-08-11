package de.ivu.fare.e4.annotations.extractor;

import org.eclipse.swt.widgets.Text;

public class TextExtractor implements ITextExtractor {
	@Override
	public Text getText(Object object) {
		return (Text) object;
	}
}
