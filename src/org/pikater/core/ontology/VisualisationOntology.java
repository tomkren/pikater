package org.pikater.core.ontology;

import org.pikater.core.ontology.subtrees.visualisation.GenerateScatterPlot;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class VisualisationOntology extends BeanOntology {
	
	private static final long serialVersionUID = 7444542988181213752L;

	private VisualisationOntology() {
		super("VisualisationOntology");

		try {
			add(GenerateScatterPlot.class);
		} catch (BeanOntologyException e) {
			e.printStackTrace();
		}
	}
	
	static VisualisationOntology theInstance = new VisualisationOntology();

	public static Ontology getInstance() {
		return theInstance;
	}
}
