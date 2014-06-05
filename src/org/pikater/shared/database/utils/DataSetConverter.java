package org.pikater.shared.database.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DataSetConverter {
	
	public enum DocType{
		Table,
		Native
	}
	
	DocType docType;
	File inputFile;
	
	
	public DataSetConverter(File inputFile) throws Exception{
		this.inputFile=inputFile;
		init();
	}
	
	private void init() throws Exception{
		if(
				inputFile.getAbsolutePath().toLowerCase().endsWith("xls")||
				inputFile.getAbsolutePath().toLowerCase().endsWith("xlsx")||
				inputFile.getAbsolutePath().toLowerCase().endsWith("ods")
		  )
		{
			this.docType=DocType.Table;
		}else{
			this.docType=DocType.Native;
			this.initNative();
		}
	}
	
	
	BufferedReader br=null;
	
	boolean inDataSection=false;
	
	private void initNative() throws Exception{
		try {
			br=new BufferedReader(new InputStreamReader(new FileInputStream(inputFile)));
			String s=br.readLine();
			s=s.toLowerCase();
			if((s!=null)&&s.startsWith("@relation")){
				relationName=s.split(" ")[1];
				
				while((s=br.readLine())!=null){
					s=s.toLowerCase();
					if(s.startsWith("@attribute")){
						
						Attribute attr=new Attribute();
						attr.attrName=s.split(" ")[1];
						attr.attrProperties=s.split(" ")[2];
						//System.out.println("Attribute: "+attr.attrName+"   "+attr.attrProperties);
						attributeList.add(attr);
					}else if(s.startsWith("@data")){
						inDataSection=true;
						return;
					}else if(s.equals("")){
						
					}else{
						throw new Exception();
					}
					
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveToXLS(File outputFile) throws Exception {
		try {			
			Workbook wb=new HSSFWorkbook();
			Sheet s= wb.createSheet();
			Row r= null;
			Cell c = null;
			
			//The first cell of the first row contains the relation name 
			int rowNum=0;
			r=s.createRow(rowNum);
			c=r.createCell(0);
			c.setCellValue(this.getRelationName());
			
			//Copy attribute names to the second row
			rowNum=1;
			r=s.createRow(rowNum);
			int cellNum=0;
			for(Attribute attr : this.getAttributeList()){
				Cell attrNameCell=r.createCell(cellNum);
				attrNameCell.setCellValue(attr.attrName);
				cellNum++;
		    }
			//Copy the attribute properties to the second row
			cellNum=0;
			rowNum=2;
			r=s.createRow(rowNum);
			for(Attribute attr : this.getAttributeList()){
				Cell attrPropertiesCell=r.createCell(cellNum);
				attrPropertiesCell.setCellValue(attr.attrProperties);
				cellNum++;
			}
			//Now copying the data items
			String rowS=null;
			boolean emptyLineVisited=false;
			while((rowS=br.readLine())!=null){
				if(!rowS.equals("")){
					if(emptyLineVisited) throw new Exception();
					rowS=rowS.toLowerCase();
					String[] rowContent=rowS.trim().split(",");
					
					rowNum++;
					r=s.createRow(rowNum);
					cellNum=0;
					
					for(String item:rowContent){
						c=r.createCell(cellNum);
						cellNum++;
						c.setCellValue(item);
					}
				}else{
					emptyLineVisited=true;
				}
			}
			
			FileOutputStream out=new FileOutputStream(outputFile);
			wb.write(out);
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
		}
		
	}
	
	private String relationName;
	
	private String getRelationName(){
		return relationName;
	}
	
	private List<Attribute> attributeList=new java.util.ArrayList<Attribute>();
	
	private List<Attribute> getAttributeList(){
		return attributeList;
	}

	class Attribute{
		public String attrName;
		public String attrProperties;
	}
	
}
