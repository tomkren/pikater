package org.pikater.core.ontology.subtrees.batchDescription;




import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.util.Log;
import org.pikater.core.ontology.subtrees.newOption.NewOption;
import org.pikater.shared.experiment.universalformat.UniversalOntology;

/**
 * Created by Stepan on 20.4.14.
 */
public class DataProcessing implements IDataProvider {

	private static final long serialVersionUID = -2418323249803736416L;

	private int id = -1;
	private List<NewOption> options =
			new ArrayList<NewOption>();
    
	private List<ErrorDescription> errors =
			new ArrayList<ErrorDescription>();
	private List<DataSourceDescription> dataSources =
			new ArrayList<DataSourceDescription>();

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public List<NewOption> getOptions() {
		return options;
	}
	public void setOptions(List<NewOption> options) {
		this.options = options;
	}

	public List<ErrorDescription> getErrors() {
		return errors;
	}
	public void setErrors(List<ErrorDescription> errors) {
		this.errors = errors;
	}

    public List<DataSourceDescription> getDataSources() {
        return dataSources;
    }
    public void setDataSources(List<DataSourceDescription> dataSources) {
        this.dataSources = dataSources;
    }
    public void addDataSources(DataSourceDescription dataSources) {
    	
    	if (dataSources == null) {
    		throw new IllegalArgumentException("Argument dataSources can't be null");
    	}
        this.dataSources.add(dataSources);
    }

	
	@Override
	public List<NewOption> exportAllOptions() {
		return getOptions();
	}
	@Override
	public void importAllOptions(List<NewOption> options) {
		setOptions(options);
	}
	
	@Override
	public List<ErrorDescription> exportAllErrors() {
		return getErrors();
	}
	@Override
	public void importAllErrors(List<ErrorDescription> errors) {
		setErrors(errors);
		
	}
	
	@Override
	public List<DataSourceDescription> exportAllDataSourceDescriptions() {		
		return getDataSources();
	}
	@Override
	public void importAllDataSourceDescriptions(
			List<DataSourceDescription> dataSourceDescriptions) {
		getDataSources();
	}
	
	public int generateIDs(int lastUsedId) {
		
		if (this.getId() == -1) {
			this.setId(lastUsedId++);
		}
		return lastUsedId;
	}

	public UniversalOntology exportUniversalOntology() {
		
		UniversalOntology ontologyInfo = new UniversalOntology();
		ontologyInfo.setId(getId());
		ontologyInfo.setType(getClass());
		ontologyInfo.setOptions(exportAllOptions());
		ontologyInfo.setErrors(exportAllErrors());
		//ontologyInfo.addInputSlots(null);
		
		return ontologyInfo;
	}
	public static DataProcessing importUniversalOntology(UniversalOntology uOntology) {
		
		Constructor<?> cons = null;
		try {
			cons = uOntology.getType().getConstructor();
		} catch (NoSuchMethodException e) {
			Log.error(e.getMessage(), e);
		} catch (SecurityException e) {
			Log.error(e.getMessage(), e);
		}
		
		Object object = null;
		try {
			object = cons.newInstance();
		} catch (InstantiationException e) {
			Log.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			Log.error(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			Log.error(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			Log.error(e.getMessage(), e);
		}
		
		DataProcessing dataProcess = (DataProcessing) object;
		dataProcess.setId(uOntology.getId());
		dataProcess.setOptions(
				new ArrayList<NewOption>(uOntology.getOptions()));
		dataProcess.setErrors(
				new ArrayList<ErrorDescription>(uOntology.getErrors()));
		
		return dataProcess;
	}
	
}
