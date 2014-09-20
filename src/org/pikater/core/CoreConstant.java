package org.pikater.core;

public class CoreConstant
{
	/**
	 * Private constructors hide the public ones.
	 */
	private CoreConstant()
	{
	}
	
	public static enum SlotDirection
	{
		INPUT,
		OUTPUT
	}
	
	public static enum SlotCategory
	{
		DATA_GENERAL,
		DATA_AGENT,
		DATA_SEARCH,
		DATA_RECOMMEND,
		DATA_EVALUATIONMETHOD,
		ERROR
	}
	
	public static enum SlotContent
	{
		DATA("data"),
		FILE_DATA("fileData"),
		TRAINING_DATA("trainingData"),
		TESTING_DATA("testingData"),
		VALIDATION_DATA("validationData"),
		COMPUTED_DATA("computedData"),
		
		COMPUTATION_AGENT("computingAgent"),
		ERRORS("error"),
		EVALUATION_METHOD("evaluationMethod"),
		RECOMMEND("recommender"),
		SEARCH("search");
		
		private final String slotName;
		
		private SlotContent(String slotName)
		{
			this.slotName = slotName;
		}
		
		public String getSlotName()
		{
			return slotName;
		}
		
		public SlotCategory getCategory()
		{
			switch(this)
			{
				case DATA:
				case COMPUTED_DATA:
				case FILE_DATA:
				case TESTING_DATA:
				case TRAINING_DATA:
				case VALIDATION_DATA:
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
	
	public static enum Mode
	{
		DEFAULT,
		TRAIN_ONLY,
		TEST_ONLY,
		TRAIN_TEST,
	}
	
	public static enum Output
	{
		DEFAULT,
		EVALUATION_ONLY,
		PREDICTION;
	}
	
	public static enum Error
	{
		ERROR_RATE,
		KAPPA_STATISTIC,
		MEAN_ABSOLUTE,
		RELATIVE_ABSOLUTE,
		ROOT_MEAN_SQUARED,
		ROOT_RELATIVE_SQUARED;
	}
	
	public static final String DURATION_DATASET_NAME = "lineardata.arff";
	public static final String MODEL = "model";
	public static final String DURATION = "duration";
	public static final String DURATIONLR = "durationLR";
	public static final String FILEURI = "fileURI";
}