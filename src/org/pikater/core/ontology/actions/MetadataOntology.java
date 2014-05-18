package org.pikater.core.ontology.actions;


import org.pikater.core.ontology.metadata.attributes.AttributeMetadata;
import org.pikater.core.ontology.metadata.Metadata;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

/**
 * Created by Stepan on 4.5.14.
 */
public class MetadataOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5355736320938592917L;

	private MetadataOntology() {
        super("MetadataOntology");

        String metadataPackage =
        		Metadata.class.getPackage().getName();
        String attributeMetadataPackage =
        		AttributeMetadata.class.getPackage().getName();
        
        try {
            add(metadataPackage);
            add(attributeMetadataPackage);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static MetadataOntology theInstance = new MetadataOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
