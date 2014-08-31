package org.pikater.web.quartzjobs;

import java.io.File;
import java.io.IOException;

import org.pikater.core.agents.gateway.WebToCoreEntryPoint;
import org.pikater.shared.database.exceptions.DataSetConverterCellException;
import org.pikater.shared.database.exceptions.DataSetConverterException;
import org.pikater.shared.database.jpa.JPAUser;
import org.pikater.shared.database.jpa.daos.DAOs;
import org.pikater.shared.database.jpa.status.JPADatasetSource;
import org.pikater.shared.database.util.DataSetConverter;
import org.pikater.shared.database.util.DataSetConverter.InputType;
import org.pikater.shared.logging.web.PikaterLogger;
import org.pikater.shared.quartz.jobs.base.ImmediateOneTimeJob;
import org.pikater.web.config.WebAppConfiguration;
import org.pikater.web.vaadin.gui.server.components.popups.dialogs.GeneralDialogs;
import org.quartz.JobBuilder;
import org.quartz.JobExecutionException;

public class UploadedDatasetHandler extends ImmediateOneTimeJob
{
	public UploadedDatasetHandler()
	{
		super(4); // number of arguments
	}
	
	@Override
	public boolean argumentCorrect(int index, Object arg)
	{
		switch (index)
		{
			case 0:
				return arg instanceof JPAUser;
			case 1:
			case 2:
				return arg instanceof String;
			case 3:
				return arg instanceof File;
			
			default:
				return false;
		}
	}

	@Override
	public void buildJob(JobBuilder builder)
	{
		// builder.withIdentity("RemoveExpiredTrainedModels", "Jobs");
	}

	@Override
	protected void execute() throws JobExecutionException
	{
		// information from GUI that can not be computed
		JPAUser owner = getArg(0);
		String optionalARFFHeaders = getArg(1);
		String description = getArg(2);
		File uploadedFile = getArg(3);
		
		// the conversion
		File convertedFile = null;
		try
		{
			convertedFile= this.convert(uploadedFile, optionalARFFHeaders);
			int newDatasetID = DAOs.dataSetDAO.storeNewDataSet(convertedFile,description, owner.getId(),JPADatasetSource.USER_UPLOAD);
			if(WebAppConfiguration.isCoreEnabled())
			{
				try
				{
					WebToCoreEntryPoint.notify_newDataset(newDatasetID);
				}
				catch (Throwable t)
				{
					PikaterLogger.logThrowable("Could not send notification about a new dataset to core.", t);
					GeneralDialogs.warning("Failed to notify core", "Your dataset has been saved and designated "
							+ "for metadata computation but notification was not successfully passed to pikater core.");
				}
			}
			else
			{
				GeneralDialogs.info("Core not available at this moment", "Your dataset has been saved and designated "
						+ "for metadata computation but the actual computation may be pending until a running pikater core picks your agent up.");
			}
			
		} catch (IOException e) {
			throw new JobExecutionException(e);
		} catch (DataSetConverterCellException e){
			throw new JobExecutionException(e);
		} catch (DataSetConverterException e) {
			throw new JobExecutionException(e);
		}
		finally{
			if(uploadedFile.exists()){
				uploadedFile.delete();
			}
			if((convertedFile!=null)&&convertedFile.exists()){
				convertedFile.delete();
			}
		}
	}
	
	
	/**
	 * Converts the input file to an ARFF file
	 * Input file can be in ARFF,CSV,XLS,XLSX formats.
	 * if header is null , than only data section is generated
	 * @param file the input file
	 * @return File object representing the file converted to ARFF format 
	 * @throws IOException 
	 * @throws DataSetConverterException 
	 * @throws Exception
	 */
	private File convert(File file,String header) throws IOException, DataSetConverterException{
		String path = file.getAbsolutePath().toLowerCase();
		
		File convertedFile=new File(file.getAbsolutePath()+".convert");
		
		if(path.endsWith("txt")){
			if(header==null){
				return file;
			}else{
				DataSetConverter.joinARFFFileWithHeader(file, header, convertedFile);
				return convertedFile;
			}
		}else{
			DataSetConverter.InputType inputType= InputType.INVALID;
			if(path.endsWith("xls")){
				convertedFile=new File(file.getAbsolutePath().substring(0, path.lastIndexOf("xls"))+"arff");
				inputType=InputType.XLS;
			}else if(path.endsWith("xlsx")){
				convertedFile=new File(file.getAbsolutePath().substring(0, path.lastIndexOf("xlsx"))+"arff");
				inputType=InputType.XLSX;
			}else if(path.endsWith("csv")){
				convertedFile=new File(file.getAbsolutePath().substring(0, path.lastIndexOf("csv"))+"arff");
				inputType=InputType.CSV;
			}else{
				//System.err.println("Not supported input file format!\nPlease use ARFF,XLS or XLSX formats");
				return null;
			}
			
			if(inputType==InputType.INVALID){
				return null;
			}else{
				DataSetConverter.spreadSheetToArff(inputType, file, header, convertedFile);
				return convertedFile;
			}
		}
	}
}