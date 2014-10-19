package org.pikater.shared.experiment;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.pikater.core.ontology.subtrees.batchdescription.FileDataSaver;
import org.pikater.core.ontology.subtrees.batchdescription.examples.SearchOnly;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.shared.XStreamHelper;
import org.pikater.shared.logging.core.ConsoleLogger;
import org.pikater.shared.logging.database.PikaterDBLogger;
import org.pikater.shared.util.SimpleIDGenerator;

/**
 * <p>This class is the heart of the project - a high-level representation
 * of experiments that binds web and core. Experiments are saved
 * to database in this format, serialized as XML strings using the 
 * XStream technology. We call it the "universal format".</p>
 * 
 * From this format, experiments are also converted to web or core
 * specific formats:
 * <ul>
 * <li> Web formats and conversions from/to this format can be located in
 * the "org.pikater.web.experiment" package.
 * </ul>
 * 
 * @author stepan
 */
public class UniversalComputationDescription {
	/**
	 * Top-level ({@link UniversalElement element} independent) options for this experiment.
	 */
	private final Set<NewOption> globalOptions;

	/**
	 * Root {@link UniversalElement elements} of the whole experiment. Should only contain
	 * elements representing {@link FileDataSaver}.
	 */
	private final Set<UniversalElement> rootElements;

	/**
	 * All {@link UniversalElement elements} of the experiment.
	 */
	private final Set<UniversalElement> allElements;

	/**
	 * Central object generating IDs for instances of {@link UniversalElementOntology}
	 * within {@link UniversalElement experiment elements}.
	 */
	private final SimpleIDGenerator idGenerator;

	public UniversalComputationDescription() {
		this.globalOptions = new HashSet<NewOption>();
		this.rootElements = new HashSet<UniversalElement>();
		this.allElements = new HashSet<UniversalElement>();
		this.idGenerator = new SimpleIDGenerator();
	}

	// ----------------------------------------------------------
	// SOME BASIC INTERFACE

	/**
	 * Gets top-level ({@link UniversalElement element} independent) options
	 * for this experiment.
	 * Changes to the returned collection are propagated inside this class.
	 */
	public Set<NewOption> getGlobalOptions() {
		return globalOptions;
	}

	/**
	 * Gets root {@link UniversalElement elements} of this experiment - should
	 * only be elements representing {@link FileDataSaver}. If an element of
	 * another type is present, this object was most likely deserialized from XML
	 * which already contained the bad reference.
	 */
	public Set<UniversalElement> getRootElements() {
		return rootElements;
	}

	/**
	 * Gets all {@link UniversalElement elements} of the experiment.
	 */
	public Set<UniversalElement> getAllElements() {
		return allElements;
	}

	/**
	 * Adds an {@link UniversalElement element} to this experiment. If it a root element,
	 * it is automatically handled.
	 * @throws IllegalArgumentException if the given element doesn't associate itself to
	 * an ontology
	 */
	public void addElement(UniversalElement element) {
		if (element.getOntologyInfo() == null) {
			throw new IllegalArgumentException("The given element didn't have ontology defined.");
		} else {
			element.getOntologyInfo().setId(idGenerator.getAndIncrement());

			/*
			 * ALTERNATIVE CONDITION IMPLEMENTATION USING A CLASS DEFINED IN THE WEB APPLICATION:
			 * - it is more modular and clear but:
			 * - if used, the core system becomes partially dependent on the web application (unless
			 * the type is moved to core system of course)
			 * 
			 * if (BoxType.fromOntologyClass(element.getOntologyInfo().getOntologyClass()) == BoxType.OUTPUT)
			 */
			if (element.getOntologyInfo().getOntologyClass().equals(FileDataSaver.class)) {
				rootElements.add(element);
			}
			allElements.add(element);
		}
	}

	/**
	 * Can this experiment be shown in the experiment editor? In other words,
	 * can it be converted to the web format?
	 */
	public boolean isPresentationCompatible() {
		for (UniversalElement elementI : this.allElements) {
			if (elementI.getPresentationInfo() == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Is this experiment ready to be queued for execution and is it not likely
	 * to end up with needless errors? If not, the reason is logged.
	 */
	public boolean isValid() {
		for (UniversalElement rootElem : rootElements) {
			if (!rootElem.getOntologyInfo().getOntologyClass().equals(FileDataSaver.class)) {
				PikaterDBLogger.logThrowable("Invalid element processing", new IllegalStateException("Some root elements are not file savers."));
				return false;
			}
		}
		for (UniversalElement element : allElements) {
			for (UniversalElementConnector connector : element.getOntologyInfo().getInputDataSlots()) {
				if (connector.getFromElement() == null) {
					PikaterDBLogger.logThrowable("Required information not defined", new IllegalStateException("Some connectors don't have the 'fromElement' field defined."));
					return false;
				} else if (!allElements.contains(connector.getFromElement())) {
					PikaterDBLogger.logThrowable("Invalid element binding", new IllegalStateException("Some connectors' 'fromElement' fields are not registered in the root class."));
					return false;
				} else {
					try {
						connector.validate();
					} catch (Exception t) {
						PikaterDBLogger.logThrowable("Invalid connector", t);
						return false;
					}
				}
			}
		}
		return true;
	}

	public String toXML() {
		return XStreamHelper.serializeToXML(this, XStreamHelper.getSerializerWithProcessedAnnotations(UniversalComputationDescription.class));
	}

	public static UniversalComputationDescription fromXML(String xml) {
		return XStreamHelper.deserializeFromXML(UniversalComputationDescription.class, xml, XStreamHelper.getSerializerWithProcessedAnnotations(UniversalComputationDescription.class));
	}

	public static void main(String[] args) throws CloneNotSupportedException {
		UniversalComputationDescription uDescription = SearchOnly.createDescription().exportUniversalComputationDescription();
		ConsoleLogger.log(Level.INFO, XStreamHelper.serializeToXML(uDescription, XStreamHelper.getSerializerWithProcessedAnnotations(UniversalComputationDescription.class)));
	}
}
