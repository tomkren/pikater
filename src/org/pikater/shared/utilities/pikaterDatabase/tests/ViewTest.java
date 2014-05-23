package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.util.ArrayList;
import java.util.List;

import org.pikater.shared.database.exceptions.NoResultException;
import org.pikater.shared.database.jpa.JPADataSetLO;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.utils.ResultFormatter;
import org.pikater.shared.database.views.MetaDataView;
import org.pikater.shared.database.views.ViewColumns;
import org.pikater.shared.database.views.models.MetaDataModel;

public class ViewTest {

	public void test(){
		String dataSetDescription="weather.arff";
		
		JPADataSetLO weather;
		try {
			weather = new ResultFormatter<JPADataSetLO>(
					DAOs.dataSetDAO.getByDescription(dataSetDescription)
					).getSingleResult();
			
			
			this.stringTest(weather);
			this.tableTest(weather);
			this.defaultGlobalTest(weather);
			this.defaultNumericalTest(weather);
			this.defaultCategoricalTest(weather);
			
		} catch (NoResultException e) {
			System.err.println("Dataset "+dataSetDescription+" doesn't exist.");
			e.printStackTrace();
		}		
	}
	
	public void defaultGlobalTest(JPADataSetLO result){
		MetaDataView mdv=new MetaDataView(result);
		MetaDataModel model=mdv.getGlobalMetaDataModel();
		
		for(String defTitle:mdv.getDefaultGlobalMetaDataTitles()){
			System.out.print(defTitle+"  ");
		}
		System.out.println();
		System.out.println("---------------------------------------");
		
		for(String rowI:model.getDefaultGlobalMetaDataColumns()){
			System.out.print(rowI+" ");
		}
		System.out.println();
		System.out.println("");
		
	}
	
	public void defaultNumericalTest(JPADataSetLO result){
		MetaDataView mdv=new MetaDataView(result);
		List<MetaDataModel> models=mdv.getNumericalMetaDataModels();
		
		for(String defTitle:mdv.getDefaultNumericalMetaDataTitles()){
			System.out.print(defTitle+"  ");
		}
		System.out.println();
		System.out.println("---------------------------------------");
		for(MetaDataModel mdm:models){
			for(String rowI:mdm.getDefaultNumericalMetaDataColumns()){
				System.out.print(rowI+" ");
			}
			System.out.println();
		}
		System.out.println("");
		
	}
	
	public void defaultCategoricalTest(JPADataSetLO result){
		MetaDataView mdv=new MetaDataView(result);
		List<MetaDataModel> models=mdv.getCategoricalMetaDataModels();
		
		for(String defTitle:mdv.getDefaultCategoricalMetaDataTitles()){
			System.out.print(defTitle+"  ");
		}
		System.out.println();
		System.out.println("---------------------------------------");
		for(MetaDataModel mdm:models){
			for(String rowI:mdm.getDefaultCategoricalMetaDataColumns()){
				System.out.print(rowI+" ");
			}
			System.out.println();
		}
		System.out.println("");
		
	}
	
	public void tableTest(JPADataSetLO result){
		List<ViewColumns> cols=new ArrayList<ViewColumns>();
		
		cols.add(ViewColumns.META_DATA_TYPE);
		
		cols.add(ViewColumns.ATTR_CAT_META_NUMBER_OF_CATEGORIES);
		cols.add(ViewColumns.ATTR_CAT_META_RATIO_OF_MISSING_VALUES);
		
		cols.add(ViewColumns.GLOBAL_META_DEFAULT_TASK);
		cols.add(ViewColumns.GLOBAL_META_NUMBER_OF_INSTANCES);
		
		cols.add(ViewColumns.ATTR_NUM_META_MINIMUM);
		
		MetaDataView mdv=new MetaDataView(result);
		
		for(String colName:mdv.getColumnTitles(cols)){
			System.out.print(colName+"  ");
		}
		
		System.out.println();
		System.out.println("----------------------------------------------------------");
		
		for(MetaDataModel model:mdv.getMetaDataModels()){
			List<String> row=model.columnFormat(cols);
			for(String rowI:row){
				System.out.print(rowI+" ");
			}
			System.out.println();
		}
		
	}
	
	
	public void stringTest(JPADataSetLO result){			
		List<MetaDataModel> models=new MetaDataView(result).getMetaDataModels();
		for(MetaDataModel model:models){
			System.out.println(model.formattedString());
		}
	}
	
	public static void main(String[] args) {
		ViewTest t=new ViewTest();
		t.test();
	}

}
