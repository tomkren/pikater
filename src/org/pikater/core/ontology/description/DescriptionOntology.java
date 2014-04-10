package org.pikater.core.ontology.description;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Martin Pilat on 27.12.13.
 */
public class DescriptionOntology extends BeanOntology{

    private DescriptionOntology() {
        super("DescriptionOntology");

        String thisPackage = DescriptionOntology.class.getPackage().getName();
        System.out.println("                   " + thisPackage);
        
        try {
            add(thisPackage);
            add(org.pikater.core.ontology.messages.ExecuteExperiment.class);
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static DescriptionOntology theInstance = new DescriptionOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
