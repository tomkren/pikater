package org.pikater.core.ontology.subtrees.data;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.data.types.DataTypes;
import org.pikater.core.ontology.subtrees.metadata.Metadata;

import jade.content.Concept;

public class Datas implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3792833251974572524L;
	
	private List<Data> datas;
	private Metadata metadata;
	
	private String output = CoreConstant.Output.EVALUATION_ONLY.get(); // "predictions"
	private String mode = CoreConstant.Mode.TRAIN_TEST.get(); // test_only, train_test, train_only
	
	public Datas() {
		this.datas = new ArrayList<Data>();
	}

	public List<Data> getDatas() {
		return datas;
	}
	public void setDatas(List<Data> datas) {
		this.datas = datas;
	}
	public void addData(Data data) {
		if (data == null) {
			throw new IllegalArgumentException("Argument data can't be null");
		}
		this.datas.add(data);
	}
	public Data exportData(String dataType) {
		for (Data dataI : this.datas) {
			if (dataI.getDataType().equals(dataType)) {
				return dataI;
			}
		}
		return null;
	}

	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}

	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
	public void importInternalTrainFileName(String internalTrainFileName) {
		Data trainData = exportData(DataTypes.TRAIN_DATA);
		if (trainData == null) {
			trainData = new Data("",
					internalTrainFileName,
					DataTypes.TRAIN_DATA
					);
			addData(trainData);
		} else {
			trainData.setInternalFileName(internalTrainFileName);
		}
	}
	public String exportInternalTrainFileName() {
		return exportData(DataTypes.TRAIN_DATA).getInternalFileName();
	}

	public void importExternalTrainFileName(String externalTrainFileName) {
		Data trainData = exportData(DataTypes.TRAIN_DATA);
		if (trainData == null) {
			trainData = new Data(externalTrainFileName,
					"",
					DataTypes.TRAIN_DATA
					);
			addData(trainData);
		} else {
			trainData.setExternalFileName(externalTrainFileName);
		}
	}
	public String exportExternalTrainFileName() {
		return exportData(DataTypes.TRAIN_DATA).getExternalFileName();
	}
	
	public void importInternalTestFileName(String internalTestFileName) {
		Data testData = exportData(DataTypes.TEST_DATA);
		if (testData == null) {
			testData = new Data("",
					internalTestFileName,
					DataTypes.TEST_DATA
					);
			addData(testData);
		} else {
			testData.setInternalFileName(internalTestFileName);
		}
	}
	public String exportInternalTestFileName() {
		return exportData(DataTypes.TEST_DATA).getInternalFileName();
	}

	public void importExternalTestFileName(String externalTestFileName) {
		Data testData = exportData(DataTypes.TEST_DATA);
		if (testData == null) {
			testData = new Data(externalTestFileName,
					"",
					DataTypes.TEST_DATA
					);
			addData(testData);
		} else {
			testData.setExternalFileName(externalTestFileName);
		}
	}
	public String exportExternalTestFileName() {
		return exportData(DataTypes.TEST_DATA).getExternalFileName();
	}
	
	public void importInternalValidFileName(String internalValidFileName) {
		Data validData = exportData(DataTypes.VALID_DATA);
		if (validData == null) {
			validData = new Data("",
					internalValidFileName,
					DataTypes.VALID_DATA
					);
			addData(validData);
		} else {
			validData.setInternalFileName(internalValidFileName);
		}
	}
	public String exportInternalValidFileName() {
		return exportData(DataTypes.VALID_DATA).getInternalFileName();
	}

	public void importExternalValidFileName(String externalValidFileName) {
		Data validData = exportData(DataTypes.VALID_DATA);
		if (validData == null) {
			validData = new Data(externalValidFileName,
					"",
					DataTypes.VALID_DATA
					);
			addData(validData);
		} else {
			validData.setExternalFileName(externalValidFileName);
		}
	}
	public String exportExternalValidFileName() {
		return exportData(DataTypes.VALID_DATA).getExternalFileName();
	}
}
