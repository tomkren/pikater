package org.pikater.core.ontology.subtrees.data;

import java.util.regex.Pattern;

import org.pikater.core.ontology.subtrees.metadata.Metadata;

import jade.content.Concept;

public class Data implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4653771087310692679L;

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

	public void setLabel_file_name(String label_file_name) {
		this.labelFileName = label_file_name;
	}
	public String getLabel_file_name() {
		return labelFileName;
	}
	public void setExternal_label_file_name(String external_label_file_name) {
		this.externalLabelFileName = external_label_file_name;
	}
	public String getExternal_label_file_name() {
		return externalLabelFileName;
	}
	
	public void setTrain_file_name(String train_file_name) {
		this.internalTrainFileName = train_file_name;
	}

	public String getTrain_file_name() {
		return internalTrainFileName;
	}

	public void setTest_file_name(String test_file_name) {
		this.internalTestFileName = test_file_name;
	}

	public String getTest_file_name() {
		return internalTestFileName;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setOutput(String _output) {
		this.output = _output;
	}

	public String getOutput() {
		return output;
	}

	public void setGui_id(int _gui_id) {
		this.guiId = _gui_id;
	}

	public int getGui_id() {
		return guiId;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getMode() {
		return mode;
	}

	public void setExternal_train_file_name(String _external_train_file_name) {
		this.externalTrainFileName = _external_train_file_name;
	}

	public String getExternal_train_file_name() {
		return externalTrainFileName;
	}

	public void setExternal_test_file_name(String _external_test_file_name) {
		this.externalTestFileName = _external_test_file_name;
	}

	public String getExternal_test_file_name() {
		return externalTestFileName;
	}
	
	public String removePath(String filename){				
		return filename.split(Pattern.quote(System.getProperty("file.separator")))[2];
	}

}
