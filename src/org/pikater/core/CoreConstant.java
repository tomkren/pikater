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

	public static enum DataType {
		TRAIN_DATA("Train"), TEST_DATA("Test"), VALID_DATA("Valid"),  LABEL_DATA("Label");

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
		DATA_GENERAL, DATA_AGENT, DATA_SEARCH, DATA_RECOMMEND, DATA_EVALUATIONMETHOD, ERROR
	}

	/**
	 * 
	 * Represents types of the slot
	 * 
	 */
	public static enum SlotContent {

		DATA("data"), FILE_DATA("fileData"), TRAINING_DATA("trainingData"), TESTING_DATA(
				"testingData"), VALIDATION_DATA("validationData"), DATA_TO_LABEL("dataToLabel"),
		COMPUTED_DATA("computedData"),

		COMPUTATION_AGENT("computingAgent"), ERRORS("error"), EVALUATION_METHOD(
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
				case COMPUTED_DATA:
				case FILE_DATA:
				case TESTING_DATA:
				case TRAINING_DATA:
				case VALIDATION_DATA:
				case DATA_TO_LABEL:
					return SlotCategory.DATA_GENERAL;

				case COMPUTATION_AGENT:
					return SlotCategory.DATA_AGENT;
				case ERRORS:
					return SlotCategory.ERROR;
				case EVALUATION_METHOD:
					return SlotCategory.DATA_EVALUATIONMETHOD;
				case RECOMMEND:
					return SlotCategory.DATA_RECOMMEND;
				case SEARCH:
					return SlotCategory.DATA_SEARCH;

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
		TRAIN_ONLY, TEST_ONLY, LABEL_ONLY, TRAIN_TEST, TRAIN_TEST_LABEL, TRAIN_LABEL
	}

	/**
	 * 
	 * Represents types of the Output
	 * 
	 */
	public static enum Output {
		EVALUATION_ONLY, LABEL_ONLY, EVALUATION_LABEL;
	}

	/**
	 * 
	 * Represents types of the error in the terminology of machine learning
	 * 
	 */
	public static enum Error {
		ERROR_RATE, KAPPA_STATISTIC, MEAN_ABSOLUTE, RELATIVE_ABSOLUTE, ROOT_MEAN_SQUARED, ROOT_RELATIVE_SQUARED;
	}

	public static final String DURATION_DATASET_NAME = "lineardata.arff";
	public static final String MODEL = "model";
	public static final String DURATION = "duration";
	public static final String DURATIONLR = "durationLR";
	public static final String FILEURI = "fileURI";
}