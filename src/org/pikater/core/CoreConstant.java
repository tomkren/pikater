package org.pikater.core;

public class CoreConstant
{
	/**
	 * Private constructors hide the public ones.
	 */
	private CoreConstant()
	{
	}
	
	public static enum Slot
	{
		SLOT_DATA("data"),
		SLOT_FILE_DATA("fileData"),
		SLOT_TRAINING_DATA("trainingData"),
		SLOT_TESTING_DATA("testingData"),
		SLOT_VALIDATION_DATA("validationData"),
		SLOT_COMPUTED_DATA("computedData"),
		
		SLOT_COMPUTATION_AGENT("computingAgent"),
		SLOT_ERRORS("error"),
		SLOT_EVALUATION_METHOD("evaluationMethod"),
		SLOT_RECOMMEND("recommender"),
		SLOT_SEARCH("search");
		
		private final String constant;
		
		private Slot(String constant)
		{
			this.constant = constant;
		}
		
		public String get()
		{
			return constant;
		}
	}
	
	public static enum Mode
	{
		DEFAULT("mode"),
		TRAIN_ONLY("train_only"),
		TEST_ONLY("test_only"),
		TRAIN_TEST("train_test");
		
		private final String constant;
		
		private Mode(String constant)
		{
			this.constant = constant;
		}
		
		public String get()
		{
			return constant;
		}
	}
	
	public static enum Output
	{
		DEFAULT("output"),
		EVALUATION_ONLY("evaluation_only"),
		PREDICTION("predictions");
		
		private final String constant;
		
		private Output(String constant)
		{
			this.constant = constant;
		}
		
		public String get()
		{
			return constant;
		}
	}
	
	public static enum Error
	{
		ERROR_RATE("error_rate"),
		KAPPA_STATISTIC("kappa_statistic"),
		MEAN_ABSOLUTE("mean_absolute_error"),
		RELATIVE_ABSOLUTE("relative_absolute_error"),
		ROOT_MEAN_SQUARED("root_mean_squared_error"),
		ROOT_RELATIVE_SQUARED("root_relative_squared_error");
		
		private final String constant;
		
		private Error(String constant)
		{
			this.constant = constant;
		}
		
		public String get()
		{
			return constant;
		}
	}
	
	public static enum Misc
	{
		DURATION_DATASET_NAME("lineardata.arff"),
		MODEL("model"),
		DURATION("duration"),
		DURATIONLR("durationLR"),
		FILEURI("fileURI");
		
		private final String constant;
		
		private Misc(String constant)
		{
			this.constant = constant;
		}
		
		public String get()
		{
			return constant;
		}
	}
}