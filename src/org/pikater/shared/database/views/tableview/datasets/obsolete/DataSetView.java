package org.pikater.shared.database.views.tableview.datasets.obsolete;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.views.tableview.datasets.obsolete.models.DataSetRowModel;

public class DataSetView extends View {
	public List<DataSetRowModel> getAllDatasets(){
		List<DataSetRowModel> models=new ArrayList<DataSetRowModel>();
		List<JPADataSetLO> dslos=DAOs.dataSetDAO.getAll();
		for(JPADataSetLO dslo:dslos){
			models.add(new DataSetRowModel(dslo));
		}
		return models;
	}
}
