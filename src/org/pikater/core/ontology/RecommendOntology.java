package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.management.Agent;
import org.pikater.core.ontology.subtrees.management.Agents;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.ValueType;
import org.pikater.core.ontology.subtrees.newOption.restrictions.IRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.core.ontology.subtrees.recommend.GetMultipleBestAgents;



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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static RecommendOntology theInstance = new RecommendOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
