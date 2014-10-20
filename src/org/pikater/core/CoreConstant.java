package org.pikater.core;

/**
 * 
 * Represents s set of constants used in Pikater core
 * 
 */
public class CoreConstant {

	/**
	 * Private constructors hide the public ones.
	 */
	private CoreConstant() {
	}

	public static final String BEANSLOCATION = "Beans.xml";
	
	public static enum DataType {
		TRAIN_DATA("Train"), TEST_DATA("Test"), VALID_DATA("Valid");

		private final String type;

		private DataType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	/**
	 * 
	 * Represents a slot direction, input or output
	 * 
	 */
	public static enum SlotDirection {
		INPUT, OUTPUT;

		public SlotDirection getOther() {

			if (this == INPUT) {
				return OUTPUT;

			} else {
				return INPUT;
			}
		}
	}

	/**
	 * 
	 * Represents types of data which is flowing through the slot
	 * 
	 */
	public static enum SlotCategory {
		DATAGENERAL, DATAAGENT, DATASEARCH, DATARECOMMEND, DATAEVALUATIONMETHOD, ERROR
	}

	/**
	 * 
	 * Represents types of the slot
	 * 
	 */
	public static enum SlotContent {

		DATA("data"), FILEDATA("fileData"), TRAININGDATA("trainingData"), TESTINGDATA(
				"testingData"), VALIDATIONDATA("validationData"), COMPUTEDDATA(
				"computedData"),

		COMPUTATIONAGENT("computingAgent"), ERRORS("error"), EVALUATIONMETHOD(
				"evaluationMethod"), RECOMMEND("recommender"), SEARCH("search");

		private final String slotName;

		private SlotContent(String slotName) {
			this.slotName = slotName;
		}

		public String getSlotName() {
			return slotName;
		}

		public SlotCategory getCategory() {

			switch (this) {
				case DATA:
				case COMPUTEDDATA:
				case FILEDATA:
				case TESTINGDATA:
				case TRAININGDATA:
				case VALIDATIONDATA:
					return SlotCategory.DATAGENERAL;

				case COMPUTATIONAGENT:
					return SlotCategory.DATAAGENT;
				case ERRORS:
					return SlotCategory.ERROR;
				case EVALUATIONMETHOD:
					return SlotCategory.DATAEVALUATIONMETHOD;
				case RECOMMEND:
					return SlotCategory.DATARECOMMEND;
				case SEARCH:
					return SlotCategory.DATASEARCH;

				default:
					throw new IllegalStateException();
			}
		}
	}

	/**
	 * 
	 * Represents modes of the Batch
	 * 
	 */
	public static enum Mode {
		DEFAULT, TRAINONLY, TESTONLY, TRAINTEST,
	}

	/**
	 * 
	 * Represents types of the Output
	 * 
	 */
	public static enum Output {
		DEFAULT, EVALUATIONONLY, PREDICTION;
	}

	/**
	 * 
	 * Represents types of the error in the terminology of machine learning
	 * 
	 */
	public static enum Error {
		ERRORRATE, KAPPASTATISTIC, MEANABSOLUTE, RELATIVEABSOLUTE, ROOTMEANSQUARED, ROOTRELATIVESQUARED;
	}

	public static final String INPUTFILENAME = "input.xml";
	public static final String DURATIONDATASETNAME = "lineardata.arff";
	public static final String MODEL = "model";
	public static final String DURATION = "duration";
	public static final String DURATIONLR = "durationLR";
	public static final String FILEURI = "fileURI";
}