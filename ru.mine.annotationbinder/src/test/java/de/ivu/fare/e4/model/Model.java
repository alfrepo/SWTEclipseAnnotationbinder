package de.ivu.fare.e4.model;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import de.ivu.fare.e4.annotations.AbstractBindableModel;

/**
 * Use the RegExp to replace the getter and setter as explained in AbstractBindableModel
 * 
 * @author skip
 *
 */
public class Model extends AbstractBindableModel{
	
	boolean isEnabled;
	Image imagefield;
	
	List<Bean> comboBeans;
	Bean beanSelection;
	
	List<Pojo> comboPojos;
	Pojo pojoSelection;

	List<Bean> comboViewerInputBean;
	Bean viewerSingleSelectionBean;
	
	List<Pojo> comboViewerInputPojo;
	Pojo viewerSingleSelectionPojo;
	
	boolean isRequired;
	
	String textfield;

	boolean isVisible;
	
	
	// GENERATED GETTER AND SETTER

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		firePropertyChange("isEnabled", this.isEnabled, this.isEnabled = isEnabled);
	}

	public Image getImage() {
		return imagefield;
	}

	public void setImage(Image image) {
		firePropertyChange("image", this.imagefield, this.imagefield = image);
	}

	public List<Bean> getComboBeans() {
		return comboBeans;
	}

	public void setComboBeans(List<Bean> comboBeans) {
		firePropertyChange("comboBeans", this.comboBeans, this.comboBeans = comboBeans);
	}

	public Bean getBeanSelection() {
		return beanSelection;
	}

	public void setBeanSelection(Bean beanSelection) {
		firePropertyChange("beanSelection", this.beanSelection, this.beanSelection = beanSelection);
	}

	public List<Pojo> getComboPojos() {
		return comboPojos;
	}

	public void setComboPojos(List<Pojo> comboPojos) {
		firePropertyChange("comboPojos", this.comboPojos, this.comboPojos = comboPojos);
	}

	public Pojo getPojoSelection() {
		return pojoSelection;
	}

	public void setPojoSelection(Pojo pojoSelection) {
		firePropertyChange("pojoSelection", this.pojoSelection, this.pojoSelection = pojoSelection);
	}

	public List<Bean> getComboViewerInputBean() {
		return comboViewerInputBean;
	}

	public void setComboViewerInputBean(List<Bean> comboViewerInputBean) {
		firePropertyChange("comboViewerInputBean", this.comboViewerInputBean, this.comboViewerInputBean = comboViewerInputBean);
	}

	public Bean getViewerSingleSelectionBean() {
		return viewerSingleSelectionBean;
	}

	public void setViewerSingleSelectionBean(Bean viewerSingleSelectionBean) {
		firePropertyChange("viewerSingleSelectionBean", this.viewerSingleSelectionBean, this.viewerSingleSelectionBean = viewerSingleSelectionBean);
	}

	public List<Pojo> getComboViewerInputPojo() {
		return comboViewerInputPojo;
	}

	public void setComboViewerInputPojo(List<Pojo> comboViewerInputPojo) {
		firePropertyChange("comboViewerInputPojo", this.comboViewerInputPojo, this.comboViewerInputPojo = comboViewerInputPojo);
	}

	public Pojo getViewerSingleSelectionPojo() {
		return viewerSingleSelectionPojo;
	}

	public void setViewerSingleSelectionPojo(Pojo viewerSingleSelectionPojo) {
		firePropertyChange("viewerSingleSelectionPojo", this.viewerSingleSelectionPojo, this.viewerSingleSelectionPojo = viewerSingleSelectionPojo);
	}

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		firePropertyChange("isRequired", this.isRequired, this.isRequired = isRequired);
	}

	public String getText() {
		return textfield;
	}

	public void setText(String text) {
		firePropertyChange("text", this.textfield, this.textfield = text);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		firePropertyChange("isVisible", this.isVisible, this.isVisible = isVisible);
	}
	
	
}
