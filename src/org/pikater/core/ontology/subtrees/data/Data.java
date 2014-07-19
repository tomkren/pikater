package org.pikater.core.ontology.subtrees.data;

import java.util.List;
import java.util.regex.Pattern;

import org.pikater.core.ontology.subtrees.metadata.Metadata;

import jade.content.Concept;

public class Data implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4653771087310692679L;

	private List<String> dataFileNames;
	private String internalTrainFileName;
	private String externalTrainFileName;
	private String internalTestFileName;
	private String externalTestFileName;
	private String labelFileName;
	private Metadata metadata;  // for training data file
	private String externalLabelFileName;
	private String output = "evaluation_only"; // "predictions"
	private String mode = "train_test"; // test_only, train_test, train_only

	private int guiId; // not included in ontology

	public String getLabelFileName() {
		return labelFileName;
	}
	public void setLabelFileName(String labelFileName) {
		this.labelFileName = labelFileName;
	}
	
	public String getExternal_label_file_name() {
		return externalLabelFileName;
	}
	public void setExternalLabelFileName(String externalLabelFileName) {
		this.externalLabelFileName = externalLabelFileName;
	}

	public String getTrainFileName() {
		return internalTrainFileName;
	}
	public void setTrainFileName(String trainFileName) {
		this.internalTrainFileName = trainFileName;
	}

	public String getTestFileName() {
		return internalTestFileName;
	}
	public void setTestFileName(String testFileName) {
		this.internalTestFileName = testFileName;
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
	public void setOutput(String _output) {
		this.output = _output;
	}

	public int getGui_id() {
		return guiId;
	}
	public void setGui_id(int _gui_id) {
		this.guiId = _gui_id;
	}

	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getExternal_train_file_name() {
		return externalTrainFileName;
	}
	public void setExternal_train_file_name(String _external_train_file_name) {
		this.externalTrainFileName = _external_train_file_name;
	}

	public String getExternal_test_file_name() {
		return externalTestFileName;
	}
	public void setExternal_test_file_name(String _external_test_file_name) {
		this.externalTestFileName = _external_test_file_name;
	}
	
	public String removePath(String filename){				
		return filename.split(Pattern.quote(System.getProperty("file.separator")))[2];
	}
	public List<String> getDataFileNames() {
		return dataFileNames;
	}
	public void setDataFileNames(List<String> dataFileNames) {
		this.dataFileNames = dataFileNames;
	}

}
