package org.pikater.shared.visualisation.datasource.multiple;

import java.io.InputStream;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.visualisation.datasource.ArffDataset;

public class MultipleArffDataset extends ArffDataset {
	
	public MultipleArffDataset(InputStream stream){
		super(stream);
	}
	
	public MultipleArffDataset(JPADataSetLO dslo){
		super(dslo);
	}
	
}
