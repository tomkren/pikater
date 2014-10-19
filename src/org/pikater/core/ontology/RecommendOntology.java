package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.Agents;
import org.pikater.core.ontology.subtrees.metadata.attributes.AttributeMetadata;
import org.pikater.core.ontology.subtrees.newoption.base.NewOption;
import org.pikater.core.ontology.subtrees.newoption.base.ValueType;
import org.pikater.core.ontology.subtrees.newoption.restrictions.IRestriction;
import org.pikater.core.ontology.subtrees.newoption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newoption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.recommend.GetMultipleBestAgents;
import org.pikater.shared.logging.core.ConsoleLogger;



/**
 * Created by Stepan on 18.6.14.
 */
public class RecommendOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5355736320938592917L;

	private RecommendOntology() {
        super("RecomendOntology");

        String optionPackage = NewOption.class.getPackage().getName();
        String restrictionPackage = IRestriction.class.getPackage().getName();
        String typePackage = ValueType.class.getPackage().getName();
        String valueDataPackage = IValueData.class.getPackage().getName();
        String valuePackage = BooleanValue.class.getPackage().getName();
        
        String metaDataPackage=AttributeMetadata.class.getPackage().getName();
        
        
        try {

            add(org.pikater.core.ontology.subtrees.recommend.Recommend.class);
            
            add(GetMultipleBestAgents.class);
            add(Agent.class);
            add(Agents.class);

            add(optionPackage);
            add(restrictionPackage);
            add(typePackage);
            add(valueDataPackage);
            add(valuePackage);
            
            add(metaDataPackage);
            
            
        } catch (Exception e) {
        	ConsoleLogger.logThrowable("Unexpected error occured:", e);
        }
    }

    static RecommendOntology theInstance = new RecommendOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
