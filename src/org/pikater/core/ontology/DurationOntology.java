package org.pikater.core.ontology;

import org.pikater.core.ontology.subtrees.duration.Duration;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;


public class DurationOntology extends BeanOntology {

	private static final long serialVersionUID = -2595494753081736728L;

	private DurationOntology() {
		super("DurationOntology");

		String durationPackage = Duration.class.getPackage().getName();
		try {
			add(durationPackage);
			
		} catch (BeanOntologyException e) {
			e.printStackTrace();
		}
	}

	static DurationOntology theInstance = new DurationOntology();

	public static Ontology getInstance() {
		return theInstance;
	}
}
