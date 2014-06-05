package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.io.File;

import org.pikater.shared.database.utils.DataSetConverter;

public class DatasetConverterTest {
	public static void main(String[] args){
		
		try {
			DataSetConverter dsc=new DataSetConverter(new File("core/datasets/adult.arff"));
			dsc.saveToXLS(new File("core/datasets/adul.xls"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
