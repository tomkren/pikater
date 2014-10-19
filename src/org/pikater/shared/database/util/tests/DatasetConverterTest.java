package org.pikater.shared.database.util.tests;

import java.io.File;

import org.pikater.shared.database.util.DataSetConverter;

public class DatasetConverterTest {
	public static void main(String[] args) throws Exception {
		DataSetConverter.xlsxToArff(new File("core/converter_test/header.txt"), new File("core/converter_test/adult.xlsx"), System.err);
	}
}
