package org.pikater.shared.database.experiment;


import java.util.ArrayList;

import org.pikater.core.ontology.description.ComputationDescription;
import org.pikater.core.ontology.description.FileDataSaver;
import org.pikater.core.ontology.description.examples.SearchOnly;
import org.pikater.core.ontology.messages.Option;

import com.thoughtworks.xstream.XStream;

public class UniversalComputationDescription {

	private ArrayList<Option> globalOptions;

	/* Contain all ontology elements */
	private ArrayList<UniversalElement> elements;

	/* Tree of ComputingDescription Ontology elements wrapped in UniversalElement
	 * ArrayList contains only FileDataSavers */
	private ArrayList<UniversalElement> rootElements;

	/* Tree of Gui wrapperBoxes
	 * Null if all UniversalElements doesn't contains any UniversalGui object */
	private UniversalGuiWrapper guiWrappers;


	/*
	 * Global options of experiment.
	 */
	public ArrayList<Option> getGlobalOptions() {
		if (this.globalOptions == null) {
			return new ArrayList<Option>();
		}
		return globalOptions;
	}
	public void addGlobalOptions(ArrayList<Option> globalOptions) {
		if (this.globalOptions == null) {
			this.globalOptions = new ArrayList<Option>();
		}
		this.globalOptions.addAll(globalOptions);
	}
	public void addGlobalOption(Option globalOption) {
		if (this.globalOptions == null) {
			this.globalOptions = new ArrayList<Option>();
		}
		this.globalOptions.add(globalOption);
	}

	/*
	 * UniversalElements containing FileDataSavers ontology + guiInfo
	 */
    public ArrayList<UniversalElement> getRootElements() {

        return rootElements;
    }

    /*
     * Add UniversalElement
     */
    void addElement(UniversalElement element) {

    	if (element.getElement() == null) {
    		throw new IllegalArgumentException("UniversalElement doesn't contain element");
    	}


    	if (this.elements == null) {
    		this.elements = new ArrayList<UniversalElement>();
    	}
    	this.elements.add(element);


		if (element.getElement().getType() == FileDataSaver.class) {

			if (this.rootElements == null) {
				this.rootElements = new ArrayList<UniversalElement>();
	    	}
			this.rootElements.add(element);
		}


		if (element.getGui() == null) {

			if (guiWrappers == null) {
				this.guiWrappers = new UniversalGuiWrapper();
			}
			this.guiWrappers.addGuiWrapper(guiWrappers);
		}

    }

    /*
     *  Create new UniversalGuiWrapper and moves inside UniversalGuiWrappers
     *  and UniversalElements in parameters.
     *  Inputed wrappers and elements have to be in the same
     *  UniversalGuiWrapper.
     */
	public void insertIntoGuiWrapper(
			ArrayList<UniversalGuiWrapper> guiWrappers,
			ArrayList<UniversalElement> elements) {
		
		if (guiWrappers == null && elements == null) {
			throw new IllegalArgumentException(
					"Both lists are null (guiWrappers and elements)");
		}

		if (guiWrappers.isEmpty() && elements.isEmpty()) {
			throw new IllegalArgumentException(
					"Both lists are empty (guiWrappers and elements)");
		}

		UniversalGuiWrapper parent = null;
		
		if ( guiWrappers != null && ( ! guiWrappers.isEmpty()) ) {
			parent = this.getParentWrapperOf( guiWrappers.get(0) );
		} else {
			parent = this.getParentWrapperOf( elements.get(0) );
		}

		for ( UniversalGuiWrapper wrapperI : guiWrappers ) {
			UniversalGuiWrapper parentI =
					this.getParentWrapperOf(wrapperI);
			if (parentI != parent) {
				throw new IllegalArgumentException(
						"GuiWrappers and elements are not "
						+ "in the same UniversalGuiWrapper");
			}
		}

		for ( UniversalElement elementI : elements ) {
			UniversalGuiWrapper parentI =
					this.getParentWrapperOf(elementI);
			if (parentI != parent) {
				throw new IllegalArgumentException(
						"GuiWrappers and elements are not "
						+ "in the same UniversalGuiWrapper");
			}
		}

		UniversalGuiWrapper newWrapper = new UniversalGuiWrapper();
		newWrapper.addElements(elements);
		newWrapper.addGuiWrappers(guiWrappers);
		
		parent.replaceByWrapper(newWrapper);
	}

	private UniversalGuiWrapper getParentWrapperOf(UniversalGuiWrapper wrapper) {
		return this.guiWrappers.findWrapper(wrapper);
	}
	private UniversalGuiWrapper getParentWrapperOf(UniversalElement element) {
		return this.guiWrappers.findWrapper(element);
	}

	/*
	 * Decides if this Experiment can be show in Gui.
	 */
	public boolean containGuiInformations() {
		if (this.rootElements == null) {
			return false;
		}
		
		for ( UniversalElement elementI : this.rootElements) {
			if (elementI.getGui() == null) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Export to XML
	 */
	public String exportXML() {

		XStream xstream = new XStream();
		String xml = xstream.toXML(this);

		return xml;
	}

	/*
	 * Import to XML
	 */
	public static UniversalComputationDescription importXML(String xml) {

		XStream xstream = new XStream();

		UniversalComputationDescription uComputDes =
				(UniversalComputationDescription)xstream.fromXML(xml);

		return uComputDes;
	}


	public static void main(String [ ] args) {

		ComputationDescription description =
				SearchOnly.createDescription();

        UniversalComputationDescription uDescription =
        		description.ExportUniversalComputationDescription();

        String xml = uDescription.exportXML();
        System.out.println(xml);
	}

}
