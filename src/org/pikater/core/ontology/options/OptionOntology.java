package org.pikater.core.ontology.options;

import org.pikater.core.ontology.options.types.AbstractOption;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;


public class OptionOntology extends BeanOntology {

    private OptionOntology() {
        super("OptionOntology");

        String optionPackage = OptionOntology.class.getPackage().getName();
        String oTypesPackage = AbstractOption.class.getPackage().getName();
        
        try {
            add(optionPackage);
            add(oTypesPackage);
            
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static OptionOntology theInstance = new OptionOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}