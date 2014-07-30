package org.pikater.web.visualisation.implementation.datasource.multiple;

import java.io.InputStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.web.visualisation.implementation.datasource.ArffDataset;

public class MultipleArffDataset extends ArffDataset {
	
	public MultipleArffDataset(InputStream stream){
		super(stream);
	}
	
	public MultipleArffDataset(JPADataSetLO dslo){
		super(dslo);
	}
	
}
