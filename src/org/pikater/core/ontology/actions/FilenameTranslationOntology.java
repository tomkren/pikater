package org.pikater.core.ontology.actions;

import org.pikater.core.ontology.subtrees.fileNameTranslate.TranslateFilename;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Stepan on 20.4.14.
 */
public class FilenameTranslationOntology extends BeanOntology {

	private static final long serialVersionUID = 4284091955440820705L;

	private FilenameTranslationOntology() {
        super("FilenameTranslationOntology");

        try {
            add(TranslateFilename.class);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static FilenameTranslationOntology theInstance = new FilenameTranslationOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
