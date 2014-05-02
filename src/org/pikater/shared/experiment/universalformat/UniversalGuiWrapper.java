package org.pikater.shared.experiment.universalformat;

import java.util.ArrayList;

public class UniversalGuiWrapper {

	private ArrayList<UniversalGuiWrapper> guiWrappers;

	private ArrayList<UniversalElement> elements;

	UniversalGuiWrapper() {}

	/**
	 *  Create and save new GuiWrapper
	 */
    void replaceByWrapper(UniversalGuiWrapper wrapper) {
    	
    	this.elements.removeAll( wrapper.getElements() );
    	this.guiWrappers.removeAll( wrapper.getGuiWrappers() );
    	
    	this.guiWrappers.add(wrapper);
    }

	ArrayList<UniversalGuiWrapper> getGuiWrappers() {
		return this.guiWrappers;
	}
	ArrayList<UniversalElement> getElements() {
		return this.elements;
	}

    void addElements(ArrayList<UniversalElement> elements) {
    	if (elements == null) {
    		return;
    	}

    	for (UniversalElement elementI : elements) {
    		addElement(elementI);
    	}
    }
    void addGuiWrappers(ArrayList<UniversalGuiWrapper> wrappers) {
    	if (wrappers == null) {
    		return;
    	}

    	for (UniversalGuiWrapper wrapperI : wrappers) {
    		this.addGuiWrapper(wrapperI);
    	}
    }
    void addElement(UniversalElement element) {

    	if (this.elements == null ) {
    		this.elements = new ArrayList<UniversalElement>();
    	}
    	this.elements.add(element);
    }

    void addGuiWrapper(UniversalGuiWrapper wrapper) {
    	
    	if (this.guiWrappers == null ) {
    		this.guiWrappers = new ArrayList<UniversalGuiWrapper>();
    	}
    	this.guiWrappers.add(wrapper);
    }

    /**
     * Return parentWrapper of wrapper in parameter
     */
    UniversalGuiWrapper getParentWrapper(UniversalGuiWrapper wrapper) {

    	if (this.guiWrappers.contains(wrapper)) {
    		return this;
    	} else {

    		for (UniversalGuiWrapper wrapperI : this.guiWrappers) {
    			UniversalGuiWrapper result =
    					wrapperI.getParentWrapper(wrapper);
    			if ( result != null ) {
    				return result;
    			}
    		}
    		return null;
    	}
    }
 
    /**
     * Return parentWrapper of element in parameter
     */
    UniversalGuiWrapper getParentWrapper(UniversalElement element) {

    	if (this.elements.contains(element)) {
    		return this;
    	} else {

    		for (UniversalGuiWrapper wrapperI : guiWrappers) {
    			UniversalGuiWrapper result =
    					wrapperI.getParentWrapper(element);
    			if ( result != null ) {
    				return result;
    			}
    		}
    		return null;
    	}
    }

}
