package org.pikater.web.visualisation.definition.task;

import org.pikater.shared.database.jpa.JPADataSetLO;

public interface IVisualizeDataset
{
	void visualizeDataset(JPADataSetLO dataset, String[] attrs, String attrTarget) throws Throwable;
}