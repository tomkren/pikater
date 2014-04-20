package org.pikater.core.ontology.filenameTranslation;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Stepan on 20.4.14.
 */
public class FilenameTranslationOntology extends BeanOntology {

    private FilenameTranslationOntology() {
        super("FilenameTranslationOntology");

        try {
            add(org.pikater.core.ontology.messages.TranslateFilename.class);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static FilenameTranslationOntology theInstance = new FilenameTranslationOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}