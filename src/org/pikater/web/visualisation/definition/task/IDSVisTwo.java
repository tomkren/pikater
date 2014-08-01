package org.pikater.web.visualisation.definition.task;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.visualisation.definition.AttrComparisons;

public interface IDSVisTwo
{
	void visualizeDatasetComparison(JPADataSetLO dataset1, JPADataSetLO dataset2, AttrComparisons comparisonList) throws Throwable;
}