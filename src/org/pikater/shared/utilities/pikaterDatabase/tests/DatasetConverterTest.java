package org.pikater.shared.utilities.pikaterDatabase.tests;

import java.io.File;

import org.pikater.shared.database.utils.DataSetConverter;

public class DatasetConverterTest {
	public static void main(String[] args) throws Exception{
		DataSetConverter.xlsxToArff(
				new File("core/converter_test/header.txt"),
				new File("core/converter_test/adult.xlsx"),
				System.err);
	}
}
