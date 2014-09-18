package org.pikater.web.visualisation.definition.task;

import org.pikater.shared.database.jpa.JPAAttributeMetaData;
import org.pikater.shared.database.jpa.JPADataSetLO;

public interface IDSVisOne
{
	void visualizeDataset(JPADataSetLO dataset, JPAAttributeMetaData[] attrs, JPAAttributeMetaData attrTarget) throws Exception;
}