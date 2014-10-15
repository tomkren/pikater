package org.pikater.shared.database.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.pikater.shared.database.exceptions.DataSetConverterCellException;
import org.pikater.shared.database.exceptions.DataSetConverterException;

public class DataSetConverter {
	
	public static final char DEFAULT_DELIMINITER=',';
	public static char DELIMINITER=DataSetConverter.DEFAULT_DELIMINITER;
	
	public enum InputType{
		INVALID,
		CSV,
		XLS,
		XLSX
	}
	
	private static void workbookToArff(String header,Workbook workbook,PrintStream out) throws DataSetConverterException{
		//Parsing the first sheet or throw an exception
		if(workbook.getNumberOfSheets()==0)
			throw new DataSetConverterException("No sheets available in the document");

		//Parsing the first sheet
		Sheet sheet=workbook.getSheetAt(0);

		int startrow=sheet.getFirstRowNum();
		if(startrow!=0)
			throw new DataSetConverterException("The data values must start at the first row");

		int lastrow=sheet.getLastRowNum();
		
		DataSetConverter.printArffHeaderSection(header, out);
		
		int columnnum=-3;
		
		for(int rownum=startrow;rownum<=lastrow;rownum++){

			Row row=sheet.getRow(rownum);

			int firstcell=row.getFirstCellNum();
			if(firstcell!=0)
				throw new DataSetConverterCellException("Data must start with the first column",rownum,firstcell);

			int lastcell=row.getLastCellNum();
			//TODO: somehow nicely check if the column number is the same in all rows
			int diff=lastcell-firstcell;
			if(columnnum==-3)
				columnnum=diff;
			else
				if(columnnum!=diff) throw new DataSetConverterCellException("The sheet must contain the same number of columns in every row",rownum,columnnum);

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
	
	public static void xlsxToArff(String header, File inputXLSXFile,File outputFile) throws DataSetConverterException, FileNotFoundException, IOException{
		XSSFWorkbook input=new XSSFWorkbook(new FileInputStream(inputXLSXFile));
		DataSetConverter.workbookToArff(header, input, new PrintStream(outputFile));	
	}
	
	public static void xlsxToArff(File headerFile, File inputXLSXFile,File outputFile) throws DataSetConverterException, FileNotFoundException, IOException{
		XSSFWorkbook input=new XSSFWorkbook(new FileInputStream(inputXLSXFile));
		DataSetConverter.workbookToArff(DataSetConverter.readFileToEnd(headerFile), input, new PrintStream(outputFile));	
	}
	
	public static void xlsxToArff(String header, File inputXLSXFile,PrintStream out) throws DataSetConverterException, FileNotFoundException, IOException{
		XSSFWorkbook input=new XSSFWorkbook(new FileInputStream(inputXLSXFile));
		DataSetConverter.workbookToArff(header, input, out);	
	}
	
	public static void xlsxToArff(File headerFile, File inputXLSXFile,PrintStream out) throws DataSetConverterException, FileNotFoundException, IOException{
		XSSFWorkbook input=new XSSFWorkbook(new FileInputStream(inputXLSXFile));
		DataSetConverter.workbookToArff(DataSetConverter.readFileToEnd(headerFile), input, out);	
	}
	
	public static void xlsToArff(String header, File inputXLSFile,File outputFile) throws DataSetConverterException, FileNotFoundException, IOException{
		HSSFWorkbook input=new HSSFWorkbook(new FileInputStream(inputXLSFile));
		DataSetConverter.workbookToArff(header, input, new PrintStream(outputFile));	
	}
	
	public static void xlsToArff(File headerFile, File inputXLSFile,File outputFile) throws DataSetConverterException, FileNotFoundException, IOException{
		HSSFWorkbook input=new HSSFWorkbook(new FileInputStream(inputXLSFile));
		DataSetConverter.workbookToArff(DataSetConverter.readFileToEnd(headerFile), input, new PrintStream(outputFile));	
	}
	
	public static void xlsToArff(String header, File inputXLSFile,PrintStream out) throws DataSetConverterException, FileNotFoundException, IOException{
		HSSFWorkbook input=new HSSFWorkbook(new FileInputStream(inputXLSFile));
		DataSetConverter.workbookToArff(header, input, out);	
	}
	
	public static void xlsToArff(File headerFile, File inputXLSFile,PrintStream out) throws DataSetConverterException, FileNotFoundException, IOException{
		HSSFWorkbook input=new HSSFWorkbook(new FileInputStream(inputXLSFile));
		DataSetConverter.workbookToArff(DataSetConverter.readFileToEnd(headerFile), input, out);	
	}
	
	public static void xlsToArff(File inputXLSFile,File outputFile) throws FileNotFoundException, DataSetConverterException, IOException{
		DataSetConverter.xlsToArff((String)null, inputXLSFile, outputFile);
	}
	
	public static void xlsToArff(File inputXLSFile,PrintStream out) throws FileNotFoundException, DataSetConverterException, IOException {
		DataSetConverter.xlsToArff((String)null, inputXLSFile, out);
	}
	
	public static void xlsxToArff(File inputXLSXFile,File outputFile) throws FileNotFoundException, DataSetConverterException, IOException {
		DataSetConverter.xlsxToArff((String)null, inputXLSXFile, outputFile);
	}
	
	public static void xlsxToArff(File inputXLSXFile,PrintStream out) throws FileNotFoundException, DataSetConverterException, IOException {
		DataSetConverter.xlsxToArff((String)null, inputXLSXFile, out);
	}
	
	public static void spreadSheetToArff(InputType inputType,File input,String header,File output) throws FileNotFoundException, DataSetConverterException, IOException{
		switch(inputType){
		case XLS:
			DataSetConverter.xlsToArff(header, input, output);
			break;
		case XLSX:
			DataSetConverter.xlsxToArff(header, input, output);
			break;
		case CSV:
			DataSetConverter.joinCSVFileWithHeader(input, header, output);
			break;
		case INVALID:
		default:
			throw new DataSetConverterException("Unsupported document format : " + input);
		}
	}
	
	private static String readFileToEnd(File textFile) throws IOException{
		BufferedReader br=null;
		try{
			br=new BufferedReader(new InputStreamReader(new FileInputStream(textFile)));
			StringBuilder sb=new StringBuilder();
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
	
	/**
	 * Copies the content of the input file to the prinstream
	 * The function doesn't close the printstream, so it can be used for other datas too
	 * @param input Input file
	 * @param ps Output stream
	 * @throws IOException
	 */
	private static void copyFileToStream(File input,PrintStream ps) throws IOException{
		BufferedReader br=null;
		String line=null;
		try{
			br=new BufferedReader(new InputStreamReader(new FileInputStream(input)));
			
			while((line=br.readLine())!=null){
				ps.println(line);
			}

		}finally{
			if(br!=null)br.close();
		}
	}
	
	/**
	 * Copies the content of the input file to the output file adding 
	 * data section ARFF tag before the data section
	 * e.g.
	 * before conversion   --->    after conversion
	 * a,b,c						'@data'
	 * d,e,f						a,b,c
	 * 							 	d,e,f
	 * 
	 * This function should be used for converting raw CSV data, which doesn't contain
	 * '@data' deliminiter
	 * 
	 * @param input The input file
	 * @param header Header, that can be null
	 * @param output The output file 
	 * @throws IOException
	 */
	public static void joinCSVFileWithHeader(File input,String header,File output) throws IOException{
		PrintStream ps= new PrintStream(output);
		DataSetConverter.printArffHeaderSection(header, ps);
		DataSetConverter.copyFileToStream(input, ps);
		ps.close();
	}
	
	/**
	 * Copies the content of the input file to the output file presuming,
	 * that the input file is a valid ARFF file
	 * Only header attachment is done. If header is null, the input file is
	 *  only copied to the ouput file. 
	 * This function should be used for joining ARFF data with header information
	 * 
	 * e.g.
	 * Header:
	 * header-line1
	 * header-line2
	 * 
	 * before conversion   --->    after conversion
	 * 								header-line1
	 * 								header-line2
	 * '@data'						   '@data'
	 * a,b,c							a,b,c
	 * d,e,f						 	d,e,f
	 * 
	 * 
	 * @param input The input file
	 * @param header Header, that can be null
	 * @param output The output file 
	 * @throws IOException
	 */
	public static void joinARFFFileWithHeader(File input,String header,File output) throws IOException{
		PrintStream ps= new PrintStream(output);
		if(header!=null){
			ps.println(header);
		}
		DataSetConverter.copyFileToStream(input, ps);
		ps.close();
	}
	
	private static void printArffHeaderSection(String header,PrintStream out){
		if(header!=null){
			out.println(header);
		}
		out.println("@data");
	}
}
