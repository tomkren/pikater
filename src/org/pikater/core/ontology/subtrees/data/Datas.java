package org.pikater.core.ontology.subtrees.data;

import java.util.ArrayList;
import java.util.List;

import jade.content.Concept;

public class Datas implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3792833251974572524L;
	
	private List<Data> datas;
	
	private String output = "evaluation_only"; // "predictions"
	private String mode = "train_test"; // test_only, train_test, train_only
	
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
	
}
