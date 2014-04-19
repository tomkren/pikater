package org.pikater.core.options.xmlGenerators;

import org.pikater.core.ontology.description.Recommender;
import org.pikater.core.ontology.messages.Recommend;
import org.pikater.core.options.LogicalBoxDescription;

public class BasicRecommend_RecommendBox extends LogicalBoxDescription {

	public BasicRecommend_RecommendBox() {
		super("Basic Recommend",
				Recommend.class,
				"Basic Recommend"
				);

		this.setPicture("picture3.jpg");
		this.setAgentName(Recommender.class);
		
		this.setOutputSlots(AAA_SlotHelper.getRecommendOutputSlots());
	}

}
