package org.pikater.shared.experiment;

/**
 * <p>Instances of this class are logical "parts" of the parent 
 * {@link UniversalExperiment experiment} that correspond to
 * boxes of the web application's experiment editor.</p>
 * 
 * <p>They are connected among themselves with instances of
 * {@link UniversalElementConnector} that are stored in the
 * {@link #ontologyInfo} field.</p> 
 * 
 * @author stepan
 */
public class UniversalElement {
	/**
	 * Additional experiment related information about this element. For example,
	 * connections to other elements.
	 */
	private UniversalElementOntology ontologyInfo;

	/**
	 * Additional presentation related information about this element.
	 */
	private UniversalElementPresentation presentationInfo;

	public UniversalElement() {
		this.ontologyInfo = new UniversalElementOntology();
		this.presentationInfo = new UniversalElementPresentation();
	}

	/**
	 * Gets {@link #ontologyInfo ontology information}.
	 */
	public UniversalElementOntology getOntologyInfo() {
		return ontologyInfo;
	}

	/**
	 * Sets {@link #ontologyInfo ontology information}.
	 */
	public void setOntologyInfo(UniversalElementOntology ontologyInfo) {
		this.ontologyInfo = ontologyInfo;
	}

	/**
	 * Gets {@link #presentationInfo presentation information}.
	 */
	public UniversalElementPresentation getPresentationInfo() {
		return presentationInfo;
	}

	/**
	 * Sets {@link #presentationInfo presentation information}.
	 */
	public void setPresentationInfo(UniversalElementPresentation presentationInfo) {
		this.presentationInfo = presentationInfo;
	}
}
