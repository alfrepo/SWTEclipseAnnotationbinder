package de.ivu.fare.e4.annotations.eval;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import de.ivu.fare.e4.annotations.AbstractBindableModel;
import de.ivu.fare.e4.annotations.BindWidgetTextToModel;
import de.ivu.fare.e4.annotations.converters.ConverterDateToString;
import de.ivu.fare.e4.annotations.converters.ConverterStringToDate;

public class Main implements IFormsAnnotationsObject {
	private DataBindingContext m_bindingContext;

	protected Shell shell;

//	@BindWidgetTextToModel(modelClass=Model.class, modelFieldName="textValue")

	@BindWidgetTextToModel(modelClass=Model.class, modelFieldName="dateField", converterModelToTarget=ConverterDateToString.class, converterTargetToModel=ConverterStringToDate.class)
	private Text text;

	@BindWidgetTextToModel( modelClass=Model.class, modelFieldName="comboValue")
	private Combo combo;

	@BindWidgetTextToModel( modelClass=Model.class, modelFieldName="spinnerValue")
	private Spinner spinner;

	@BindWidgetTextToModel( modelClass=Model.class, modelFieldName="labelValue")
	private Label lblNewLabel;

	@BindWidgetTextToModel( modelClass=Model.class, modelFieldName="listValue" )
	private List list;
	private Button btnRadioSpanish;
	private Button btnRadioEnglish;

	private Model model = new Model();
	private Text textDouble;
	private Text textDateYear;
	private Text textDateDay;
	private DateTime dateTime;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			@Override
            public void run() {
				try {
					Main window = new Main();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(new FormLayout());

		lblNewLabel = new Label(shell, SWT.NONE);
		FormData fd_lblNewLabel = new FormData();
		fd_lblNewLabel.left = new FormAttachment(0, 10);
		lblNewLabel.setLayoutData(fd_lblNewLabel);
		lblNewLabel.setText("New Label");

		text = new Text(shell, SWT.BORDER);
		fd_lblNewLabel.bottom = new FormAttachment(text, -6);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(0, 57);
		fd_text.left = new FormAttachment(0, 10);
		text.setLayoutData(fd_text);

		combo = new Combo(shell, SWT.NONE);
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(0, 105);
		fd_combo.left = new FormAttachment(0, 10);
		combo.setLayoutData(fd_combo);

		spinner = new Spinner(shell, SWT.BORDER);
		FormData fd_spinner = new FormData();
		fd_spinner.top = new FormAttachment(0, 153);
		fd_spinner.left = new FormAttachment(0, 10);
		spinner.setLayoutData(fd_spinner);

		list = new List(shell, SWT.BORDER);
		FormData fd_list = new FormData();
		fd_list.left = new FormAttachment(text, 97);
		list.setLayoutData(fd_list);

		Group group = new Group(shell, SWT.NONE);
		fd_list.top = new FormAttachment(group, -64, SWT.TOP);
		fd_list.bottom = new FormAttachment(group, -29);
		FormData fd_group = new FormData();
		fd_group.top = new FormAttachment(0, 120);
		fd_group.left = new FormAttachment(combo, 82);
		fd_group.right = new FormAttachment(100, -75);
		group.setLayoutData(fd_group);
		group.setLayout(new RowLayout(SWT.HORIZONTAL));

		btnRadioEnglish = new Button(group, SWT.RADIO);
		btnRadioEnglish.setText("English");

		btnRadioSpanish = new Button(group, SWT.RADIO);
		btnRadioSpanish.setText("Spanish");

		textDouble = new Text(shell, SWT.BORDER);
		fd_group.bottom = new FormAttachment(textDouble, -29);
		FormData fd_textDouble = new FormData();
		fd_textDouble.top = new FormAttachment(0, 197);
		fd_textDouble.left = new FormAttachment(0, 244);
		fd_textDouble.right = new FormAttachment(100, -114);
		textDouble.setLayoutData(fd_textDouble);

		Label lblDouble = new Label(shell, SWT.NONE);
		FormData fd_lblDouble = new FormData();
		fd_lblDouble.left = new FormAttachment(0, 183);
		fd_lblDouble.top = new FormAttachment(textDouble, 0, SWT.TOP);
		lblDouble.setLayoutData(fd_lblDouble);
		lblDouble.setText("Double");

		Label lblDate = new Label(shell, SWT.NONE);
		FormData fd_lblDate = new FormData();
		fd_lblDate.bottom = new FormAttachment(100, -10);
		fd_lblDate.left = new FormAttachment(0, 182);
		lblDate.setLayoutData(fd_lblDate);
		lblDate.setText("year , day");

		textDateYear = new Text(shell, SWT.BORDER);
		FormData fd_textDateYear = new FormData();
		fd_textDateYear.bottom = new FormAttachment(lblDate, 0, SWT.BOTTOM);
		fd_textDateYear.left = new FormAttachment(textDouble, 0, SWT.LEFT);
		textDateYear.setLayoutData(fd_textDateYear);

		textDateDay = new Text(shell, SWT.BORDER);
		FormData fd_textDateDay = new FormData();
		fd_textDateDay.bottom = new FormAttachment(lblDate, 0, SWT.BOTTOM);
		fd_textDateDay.left = new FormAttachment(textDateYear, 6);
		textDateDay.setLayoutData(fd_textDateDay);

		dateTime = new DateTime(shell, SWT.BORDER | SWT.DROP_DOWN);
		dateTime.setDate(22, 12, 2017);
		FormData fd_dateTime = new FormData();
		fd_dateTime.right = new FormAttachment(0, 424);
		fd_dateTime.top = new FormAttachment(0);
		fd_dateTime.left = new FormAttachment(0);
		dateTime.setLayoutData(fd_dateTime);
		m_bindingContext = initDataBindings();


		observeModelViaThread();
	}

	class Model extends AbstractBindableModel{
		public boolean speaksEnglish = false;
		public boolean speaksSpanish = false;

		public String labelValue;
		public String textValue;
		public String comboValue;
		public String spinnerValue;
		public String listValue;
		public Date dateField = new Date();
		public Double priceField;



		public Date getDateField() {
			return dateField;
		}
		public void setDateField(Date dateField) {
			firePropertyChange("dateField", this.dateField, this.dateField = dateField);
		}
		public Double getPriceField() {
			return priceField;
		}
		public void setPriceField(Double priceField) {
			firePropertyChange("priceField", this.priceField, this.priceField = priceField);
		}
		public boolean isSpeaksEnglish() {
			return speaksEnglish;
		}
		public void setSpeaksEnglish(boolean speaksEnglish) {
			firePropertyChange("speaksEnglish", this.speaksEnglish, this.speaksEnglish = speaksEnglish);
		}
		public boolean isSpeaksSpanish() {
			return speaksSpanish;
		}
		public void setSpeaksSpanish(boolean speaksSpanish) {
			firePropertyChange("speaksSpanish", this.speaksSpanish, this.speaksSpanish = speaksSpanish);
		}
		public String getLabelValue() {
			return labelValue;
		}
		public void setLabelValue(String labelValue) {
			firePropertyChange("labelValue", this.labelValue, this.labelValue = labelValue);
		}
		public String getTextValue() {
			return textValue;
		}
		public void setTextValue(String textValue) {
			firePropertyChange("textValue", this.textValue, this.textValue = textValue);
		}
		public String getComboValue() {
			return comboValue;
		}
		public void setComboValue(String comboValue) {
			firePropertyChange("comboValue", this.comboValue, this.comboValue = comboValue);
		}
		public String getSpinnerValue() {
			return spinnerValue;
		}
		public void setSpinnerValue(String spinnerValue) {
			firePropertyChange("spinnerValue", this.spinnerValue, this.spinnerValue = spinnerValue);
		}
		public String getListValue() {
			return listValue;
		}
		public void setListValue(String listValue) {
			firePropertyChange("listValue", this.listValue, this.listValue = listValue);
		}


	}
	public Button getBtnRadioSpanish() {
		return btnRadioSpanish;
	}
	public Button getBtnRadioEnglish() {
		return btnRadioEnglish;
	}



	/**
	 * A Thread, which:
	 * - publishes the model's value to console
	 * - increments the model value to trigger the changes in bound widgets
	 */
	private void observeModelViaThread() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				while (true) {

					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							System.out.println("Thread is alive. Model.speaksEnglish: "+model.speaksEnglish);
							System.out.println("Thread is alive. Model.speaksSpanish: "+model.speaksSpanish);
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
							System.out.println("Thread is alive. Model.speaksSpanish: "+simpleDateFormat.format(model.dateField));
						}
					});

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
		};
		new Thread(r).start();
	}

	public Combo getCombo() {
		return combo;
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeSelectionBtnRadioEnglishObserveWidget = WidgetProperties.selection().observe(btnRadioEnglish);
		IObservableValue speaksEnglishModelObserveValue = BeanProperties.value("speaksEnglish").observe(model);
		bindingContext.bindValue(observeSelectionBtnRadioEnglishObserveWidget, speaksEnglishModelObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnRadioSpanishObserveWidget = WidgetProperties.selection().observe(btnRadioSpanish);
		IObservableValue speaksSpanishModelObserveValue = BeanProperties.value("speaksSpanish").observe(model);
		bindingContext.bindValue(observeSelectionBtnRadioSpanishObserveWidget, speaksSpanishModelObserveValue, null, null);
		//
		return bindingContext;
	}
}
