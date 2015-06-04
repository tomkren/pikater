package org.pikater.core.ontology.subtrees.data;

import java.util.ArrayList;
import java.util.List;

import org.pikater.core.CoreConstant;
import org.pikater.core.ontology.subtrees.metadata.Metadata;

import jade.content.Concept;

public class Datas implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3792833251974572524L;
	
	private List<Data> datas;
	private Metadata metadata;
	
	// private String output = CoreConstant.Output.EVALUATION_ONLY.name();
	// private String mode = CoreConstant.Mode.TRAIN_TEST.name();

	private String output = CoreConstant.Output.EVALUATION_LABEL.name();
	private String mode = CoreConstant.Mode.TRAIN_TEST_LABEL.name();

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
		Data trainData = exportData(CoreConstant.DataType.TRAIN_DATA.getType());
		if (trainData == null) {
			trainData = new Data("",
					internalTrainFileName,
					CoreConstant.DataType.TRAIN_DATA.getType()
					);
			addData(trainData);
		} else {
			trainData.setInternalFileName(internalTrainFileName);
		}
	}
	public String exportInternalTrainFileName() {
		return exportData(CoreConstant.DataType.TRAIN_DATA.getType()).getInternalFileName();
	}

	public void importExternalTrainFileName(String externalTrainFileName) {
		Data trainData = exportData(CoreConstant.DataType.TRAIN_DATA.getType());
		if (trainData == null) {
			trainData = new Data(externalTrainFileName,
					"",
					CoreConstant.DataType.TRAIN_DATA.getType()
					);
			addData(trainData);
		} else {
			trainData.setExternalFileName(externalTrainFileName);
		}
	}
	public String exportExternalTrainFileName() {
		return exportData(CoreConstant.DataType.TRAIN_DATA.getType()).getExternalFileName();
	}
	
	public void importInternalTestFileName(String internalTestFileName) {
		Data testData = exportData(CoreConstant.DataType.TEST_DATA.getType());
		if (testData == null) {
			testData = new Data("",
					internalTestFileName,
					CoreConstant.DataType.TEST_DATA.getType()
					);
			addData(testData);
		} else {
			testData.setInternalFileName(internalTestFileName);
		}
	}
	public String exportInternalTestFileName() {
		return exportData(CoreConstant.DataType.TEST_DATA.getType()).getInternalFileName();
	}

	public void importExternalTestFileName(String externalTestFileName) {
		Data testData = exportData(CoreConstant.DataType.TEST_DATA.getType());
		if (testData == null) {
			testData = new Data(externalTestFileName,
					"",
					CoreConstant.DataType.TEST_DATA.getType()
					);
			addData(testData);
		} else {
			testData.setExternalFileName(externalTestFileName);
		}
	}
	public String exportExternalTestFileName() {
		return exportData(CoreConstant.DataType.TEST_DATA.getType()).getExternalFileName();
	}
	
	public void importInternalValidFileName(String internalValidFileName) {
		Data validData = exportData(CoreConstant.DataType.VALID_DATA.getType());
		if (validData == null) {
			validData = new Data("",
					internalValidFileName,
					CoreConstant.DataType.VALID_DATA.getType()
					);
			addData(validData);
		} else {
			validData.setInternalFileName(internalValidFileName);
		}
	}
	public String exportInternalValidFileName() {
		return exportData(CoreConstant.DataType.VALID_DATA.getType()).getInternalFileName();
	}

	public void importExternalValidFileName(String externalValidFileName) {
		Data validData = exportData(CoreConstant.DataType.VALID_DATA.getType());
		if (validData == null) {
			validData = new Data(externalValidFileName,
					"",
					CoreConstant.DataType.VALID_DATA.getType()
					);
			addData(validData);
		} else {
			validData.setExternalFileName(externalValidFileName);
		}
	}
	public String exportExternalValidFileName() {
		return exportData(CoreConstant.DataType.VALID_DATA.getType()).getExternalFileName();
	}

	public void importInternalLabelFileName(String internalLabelFileName) {
		Data dataTolabel = exportData(CoreConstant.DataType.LABEL_DATA.getType());
		if (dataTolabel == null) {
			dataTolabel = new Data("",
					internalLabelFileName,
					CoreConstant.DataType.LABEL_DATA.getType()
			);
			addData(dataTolabel);
		} else {
			dataTolabel.setInternalFileName(internalLabelFileName);
		}
	}
	public String exportInternalLabelFileName() {
		return exportData(CoreConstant.DataType.LABEL_DATA.getType()).getInternalFileName();
	}

	public void importExternalLabelFileName(String externalLabelFileName) {
		Data dataTolabel = exportData(CoreConstant.DataType.LABEL_DATA.getType());
		if (dataTolabel == null) {
			dataTolabel = new Data(externalLabelFileName,
					"",
					CoreConstant.DataType.LABEL_DATA.getType()
			);
			addData(dataTolabel);
		} else {
			dataTolabel.setExternalFileName(externalLabelFileName);
		}
	}
	public String exportExternalLabelFileName() {
		return exportData(CoreConstant.DataType.LABEL_DATA.getType()).getExternalFileName();
	}
}
