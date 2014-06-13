package org.pikater.shared.database.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class DataSetConverter {
	
	public static final char DEFAULT_DELIMINITER=',';
	public static char DELIMINITER=DataSetConverter.DEFAULT_DELIMINITER;
	
	private static void workbookToArff(String header,Workbook workbook,PrintStream out) throws Exception{
		//Parsing the first sheet or throw an exception
		if(workbook.getNumberOfSheets()==0)
			throw new Exception("No sheets available in the document ");

		//Parsing the first sheet
		Sheet sheet=workbook.getSheetAt(0);

		int startrow=sheet.getFirstRowNum();
		if(startrow!=0)
			throw new Exception("The data values must start at the first row");

		int lastrow=sheet.getLastRowNum();
		
		if(header!=null){
			out.println(header);
		}

		out.println("@data");
		
		int columnnum=-3;
		
		for(int rownum=startrow;rownum<=lastrow;rownum++){

			Row row=sheet.getRow(rownum);

			int firstcell=row.getFirstCellNum();
			if(firstcell!=0)
				throw new Exception("Data must start with the first column");

			int lastcell=row.getLastCellNum();
			//TODO: somehow nicely check if the column number is the same in all rows
			int diff=lastcell-firstcell;
			if(columnnum==-3)
				columnnum=diff;
			else
				if(columnnum!=diff) throw new Exception("The sheet must contain the same number of columns in every row");

			for(int col=firstcell;col<lastcell;col++){
				Cell cell=row.getCell(col);
				if(cell!=null){
					out.print(cell.toString());
				}
				if(col==lastcell-1){
					out.println();
				}else{
					out.print(DataSetConverter.DELIMINITER);
				}
			}
		}
	}
	
	public static void xlsxToArff(String header, File inputXLSXFile,File outputFile) throws Exception{
		XSSFWorkbook input=new XSSFWorkbook(new FileInputStream(inputXLSXFile));
		DataSetConverter.workbookToArff(header, input, new PrintStream(outputFile));	
	}
	
	public static void xlsxToArff(File headerFile, File inputXLSXFile,File outputFile) throws Exception{
		XSSFWorkbook input=new XSSFWorkbook(new FileInputStream(inputXLSXFile));
		DataSetConverter.workbookToArff(DataSetConverter.readFileToEnd(headerFile), input, new PrintStream(outputFile));	
	}
	
	public static void xlsxToArff(String header, File inputXLSXFile,PrintStream out) throws Exception{
		XSSFWorkbook input=new XSSFWorkbook(new FileInputStream(inputXLSXFile));
		DataSetConverter.workbookToArff(header, input, out);	
	}
	
	public static void xlsxToArff(File headerFile, File inputXLSXFile,PrintStream out) throws Exception{
		XSSFWorkbook input=new XSSFWorkbook(new FileInputStream(inputXLSXFile));
		DataSetConverter.workbookToArff(DataSetConverter.readFileToEnd(headerFile), input, out);	
	}
	
	public static void xlsToArff(String header, File inputXLSFile,File outputFile) throws Exception{
		HSSFWorkbook input=new HSSFWorkbook(new FileInputStream(inputXLSFile));
		DataSetConverter.workbookToArff(header, input, new PrintStream(outputFile));	
	}
	
	public static void xlsToArff(File headerFile, File inputXLSFile,File outputFile) throws Exception{
		HSSFWorkbook input=new HSSFWorkbook(new FileInputStream(inputXLSFile));
		DataSetConverter.workbookToArff(DataSetConverter.readFileToEnd(headerFile), input, new PrintStream(outputFile));	
	}
	
	public static void xlsToArff(String header, File inputXLSFile,PrintStream out) throws Exception{
		HSSFWorkbook input=new HSSFWorkbook(new FileInputStream(inputXLSFile));
		DataSetConverter.workbookToArff(header, input, out);	
	}
	
	public static void xlsToArff(File headerFile, File inputXLSFile,PrintStream out) throws Exception{
		HSSFWorkbook input=new HSSFWorkbook(new FileInputStream(inputXLSFile));
		DataSetConverter.workbookToArff(DataSetConverter.readFileToEnd(headerFile), input, out);	
	}
	
	public static void xlsToArff(File inputXLSFile,File outputFile) throws Exception{
		DataSetConverter.xlsToArff((String)null, inputXLSFile, outputFile);
	}
	
	public static void xlsToArff(File inputXLSFile,PrintStream out) throws Exception{
		DataSetConverter.xlsToArff((String)null, inputXLSFile, out);
	}
	
	public static void xlsxToArff(File inputXLSXFile,File outputFile) throws Exception{
		DataSetConverter.xlsxToArff((String)null, inputXLSXFile, outputFile);
	}
	
	public static void xlsxToArff(File inputXLSXFile,PrintStream out) throws Exception{
		DataSetConverter.xlsxToArff((String)null, inputXLSXFile, out);
	}
	
	private static String readFileToEnd(File textFile) throws IOException{
		BufferedReader br=null;
		try{
			br=new BufferedReader(new InputStreamReader(new FileInputStream(textFile)));
			StringBuffer sb=new StringBuffer();
			String line=null;
			while((line=br.readLine())!=null){
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			return sb.toString();
		}finally{
			if(br!=null)br.close();
		}
	}
}
