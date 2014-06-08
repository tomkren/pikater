package org.pikater.shared.database.views.peter.models;

import java.util.Date;
import java.util.List;

import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.views.peter.ViewColumns;

public class DataSetRowModel extends AbstractModel {
	
	private JPADataSetLO dslo;
	
	public DataSetRowModel(JPADataSetLO dslo){
		this.dslo=dslo;
	}
	
	public String getDescription(){
		return dslo.getDescription();
	}
	
	public String getUserName(){
		return dslo.getOwner().getLogin();
	}
	
	public Date getCreationTime(){
		return dslo.getCreated();
	}
	
	public int getAttributeCount(){
		if(dslo.getAttributeMetaData()!=null){
			return dslo.getAttributeMetaData().size();
		}else{
			return -1;
		}
	}
	
	public int getNumberOfInstances(){
		if(dslo.getGlobalMetaData()!=null){
			return dslo.getGlobalMetaData().getNumberofInstances();
		}else{
			return 0;
		}
	}
	
	

	@Override
	public String formattedString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> columnFormat(List<ViewColumns> selectedColumns) {
		// TODO Auto-generated method stub
		return null;
	}

}
