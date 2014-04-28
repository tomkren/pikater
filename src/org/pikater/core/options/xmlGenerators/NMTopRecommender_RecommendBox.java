package org.pikater.core.options.xmlGenerators;

import org.pikater.core.ontology.description.Recommend;
import org.pikater.core.options.LogicalBoxDescription;

public class NMTopRecommender_RecommendBox extends LogicalBoxDescription {

	public NMTopRecommender_RecommendBox() {
		super("NMTop Recommend",
				Recommend.class,
				"NMTop Recommend"
				);

		this.setPicture("picture4.jpg");
		this.setAgentName(Recommend.class);
		
		this.setOutputSlots(AAA_SlotHelper.getRecommendOutputSlots());
	}

}
