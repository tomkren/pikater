package org.pikater.web.experiment.box;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.options.LogicalUnit;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.parameters.EnumeratedValueParameter;
import org.pikater.shared.experiment.parameters.RangedValueParameter;
import org.pikater.shared.experiment.parameters.ValueParameter;
import org.pikater.shared.experiment.resources.ParamResource;
import org.pikater.shared.util.Interval;
import org.pikater.web.experiment.box.LeafBox.ParameterVisibility;

public class BoxGenerator {
	
	public static LeafBox getChooseXValueBox(){
		LogicalUnit lu=new LogicalUnit();
		lu.setAgentName("Choose X Values Agent");
		lu.setDescription("...");
		lu.setDisplayName("Choose X Values");
		lu.setType(BoxType.SEARCHER);
		lu.setIsBox(true);
		//lu.setOntology(null);
		//lu.setPicture("");
		
		LeafBox lb=new LeafBox(lu);
		
		//# default number of values to try for each option (default 5)
		//$ N int 1 1 r 1 2000 5 number_of_values_to_try Default number of values to try for each option
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Number of Values to Try", "Number of values to try for each option"),
				new RangedValueParameter<Integer>(new Integer(5), new Interval<Integer>(1, 2000) , true));
		return lb;
	}
	
	/**
	 * NOT IMPLEMENTED YET 
	 * Returns null!!!!
	 * @return
	 */
	public static LeafBox getCrossValidationBox(){
		LogicalUnit lu=new LogicalUnit();
		//lu.setAgentName("Choose X Values Agent");
		lu.setDescription("....");
		lu.setDisplayName("Cross Validation");
		//lu.setType(BoxType.SEARCHER);
		lu.setIsBox(true);
		lu.setOntology(null);
		lu.setPicture("");
		
		//# default number of folds
		//$ F int 1 1 r 1 100 5 folds Folds
		lu.addParameter(new RangedValueParameter<Integer>(5, new Interval<Integer>(1, 100), true));
		return null;
		//return new LeafBox(lu);
	}
	
	
	public static LeafBox getEASearchBox(){
		LogicalUnit lu=new LogicalUnit();
		lu.setAgentName("Agent_EASearch");
		lu.setDescription("....");
		lu.setDisplayName("EASearch");
		lu.setType(BoxType.SEARCHER);
		lu.setIsBox(true);
		//lu.setOntology(null);
		//lu.setPicture("");
		
		LeafBox lb=new LeafBox(lu);
		
		/**
		# minimum error rate (default 0.1)
		$ E float 1 1 r 0 1 0.1 error_rate Error rate
		**/
		RangedValueParameter<Float> par=new RangedValueParameter<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Minimum Error Rate", "Minimum error rate (default 0.1)"),
				par);
		/**
		# maximal number of generations (default 10)
		$ M int 1 1 r 1 1000 10 generations Generations
		**/
		RangedValueParameter<Integer> par2=new RangedValueParameter<Integer>(10, new Interval<Integer>(1, 1000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Maximal Number of Generations", "Maximal number of generations (default 10)"),
				par2);
		/**
		# Mutation rate (default 0.2)
		$ T float 1 1 r 0 1 0.2 mutation_rate Mutation rate
		**/
		RangedValueParameter<Float> par3=new RangedValueParameter<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Mutation Rate", "Mutation rate (default 0.2)"),
				par3);
		/**
		# Crossover probability (default 0.5)
		$ X float 1 1 r 0 1  0.5 xover_rate Xover rate
		**/
		RangedValueParameter<Float> par4=new RangedValueParameter<Float>(0.5f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Crossover Probability", "Crossover probability (default 0.5)"),
				par4);
		/**
		# population size (default 10)
		$ P int 1 1 r 1 100 10 pop_size Population
		**/
		RangedValueParameter<Integer> par5=new RangedValueParameter<Integer>(10, new Interval<Integer>(1, 100), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Population Size", "Population size (default 10)"),
				par5);
		/**
		# Maximum nubmer of option evaluations
		$ I int 1 1 r 1 100000 100 maxEval Maximum evaluations
		**/
		RangedValueParameter<Integer> par6=new RangedValueParameter<Integer>(100, new Interval<Integer>(1,100000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Maximum Number of Option Evaluations", "Maximum number of option evaluations (default 100)"),
				par6);
		/**
		# Mutation rate per field in individual (default 0.2)
		$ F float 1 1 r 0 1 0.2 mutation_rate_per_field Mutation rate per field in individual
		**/
		RangedValueParameter<Float> par7=new RangedValueParameter<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Mutation Rate per Field in Individual", "Mutation rate per field in individual (default 0.2)"),
				par7);
		/**
		# The percentage of elite individuals (default 0.1)
		$ L float 1 1 r 0 1 0.1 eliteSize Elite size
		**/
		RangedValueParameter<Float> par8=new RangedValueParameter<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("The Percentage of Elite Individuals", "The percentage of elite individuals (default 0.1)"),
				par8);
		
		return lb;
	}
	
	public static LeafBox getGASearchBox(){
		LogicalUnit lu=new LogicalUnit();
		lu.setAgentName("Agent_GASearch");
		lu.setDescription("....");
		lu.setDisplayName("GASearch");
		lu.setType(BoxType.SEARCHER);
		lu.setIsBox(true);
		
		LeafBox lb=new LeafBox(lu);
		/**
		# minimum error rate (default 0.1)
		$ E float 1 1 r 0 1 0.1 error_rate Error rate
		**/
		RangedValueParameter<Float> par1=new RangedValueParameter<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Minimum Error Rate", "Minimum error rate (default 0.1)"),
				par1);
		/**
		# maximal number of generations (default 10)
		$ M int 1 1 r 1 1000 10 generations Generations
		**/
		RangedValueParameter<Integer> par2=new RangedValueParameter<Integer>(10, new Interval<Integer>(1,1000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Maximal Number of Genereations", "Maximal number of generations (default 10)"),
				par2);
		/**
		# Mutation rate (default 0.2)
		$ T float 1 1 r 0 1 0.2 mutation_rate Mutation rate
		**/
		RangedValueParameter<Float> par3=new RangedValueParameter<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Mutation Rate", "Mutation rate (default 0.2)"),
				par3);
		/**
		# Crossover probability (default 0.5)
		$ X float 1 1 r 0 1  0.5 xover_rate Xover rate
		**/
		RangedValueParameter<Float> par4=new RangedValueParameter<Float>(0.5f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Crossover Probability", "Crossover probability (default 0.5)"),
				par4);
		/**
		# population size (default 5)
		$ P int 1 1 r 1 100 5 pop_size Population
		**/
		RangedValueParameter<Integer> par5=new RangedValueParameter<Integer>(5, new Interval<Integer>(1,100), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Population Size", "Population size (default 5)"),
				par5);
		/**
		# Size of tournament in selection (default 2)
		$ S int 1 1 r 1 100 2 tournament_size Tournament size
		**/
		RangedValueParameter<Integer> par6=new RangedValueParameter<Integer>(2, new Interval<Integer>(1,100), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Size of Tournament in Selection", "Size of tournament in selection (default 2)"),
				par6);
		
		
		return lb;
	}
	
	public static LeafBox getGridSearchBox(){
		LogicalUnit lu=new LogicalUnit();
		lu.setAgentName("Agent_GridSearch");
		lu.setDescription("....");
		lu.setDisplayName("GridSearch");
		lu.setType(BoxType.SEARCHER);
		lu.setIsBox(true);
		
		LeafBox lb=new LeafBox(lu);
		/**
		# maximum block size (default 10)
		$ B int 1 1 r 0 100000 10 maxBlock Block size
		**/
		RangedValueParameter<Integer> par1=new RangedValueParameter<Integer>(10, new Interval<Integer>(0,100000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Maximum Block Size", "Maximum block size (default 10)"),
				par1);
		/**
		# default number of tries (default 10)
		$ N int 1 1 r 0 1000 10 defaultTries Default tries
		**/
		RangedValueParameter<Integer> par2=new RangedValueParameter<Integer>(10, new Interval<Integer>(0,1000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Default Number of Tries", "Default number of tries (default 10)"),
				par2);
		
		/**
		# zero for logarithmic steps (default 0.000000001)
		$ Z float 1 1 r 0 1000 0.000000001 logZero Zero for logarithmic steps
		**/
		RangedValueParameter<Float> par3=new RangedValueParameter<Float>(0.000000001f, new Interval<Float>(0.0f, 1000.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Zero for Logarithmic Steps", "Zero for logarithmic steps (default 0.000000001)"),
				par3);
		
		return lb;		
	}
	
	/**
	 * NOT FULLY IMPLEMENTED
	 * I couldn't find the agent
	 * @return
	 */
	public static LeafBox getJ48Box(){
		LogicalUnit lu=new LogicalUnit();
		
		LeafBox lb=new LeafBox(lu);
		/**
		# Use unpruned tree.
		$ U boolean
		**/
		ValueParameter<Boolean> par1=new ValueParameter<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Use Unpruned Tree", "Use unpruned tree (default false)"),
				par1);
		/**
		# Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).
		# $ C float 1 1 r 0.0001 0.6
		$ C float 1 1 s null, null, null, 0.0001, 0.1, 0.2, 0.25, 0.3, 0.4 
		**/
		EnumeratedValueParameter<Float> par2=new EnumeratedValueParameter<Float>(
				0.25f,
				new ArrayList<Float>(Arrays.asList(new Float[] {null,null,null,0.0001f,0.1f,0.2f,0.25f,0.3f,0.4f}))
				);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Confidence Threshold for Pruning", "Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning)."),
				par1);
		/**
		# Set minimum number of instances per leaf. (Default: 2)
		$ M int 1 1 r 1 10
		**/
		RangedValueParameter<Integer> par3=new RangedValueParameter<Integer>(2, new Interval<Integer>(1, 10), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Minimum Number of Instances per Leaf", "Set minimum number of instances per leaf. (Default: 2)"),
				par3);
		/**
		# Use reduced error pruning. No subtree raising is performed.
		$ R boolean
		**/
		ValueParameter<Boolean> par4=new ValueParameter<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Reduced Error Pruning", "Use reduced error pruning. No subtree raising is performed."),
				par4);
		/**
		# Set number of folds for reduced error pruning. One fold is used as the pruning set. (Default: 3)
		$ N int 1 1 s null, 1, 2, 3, 4, 5
		**/
		EnumeratedValueParameter<Integer> par5=new EnumeratedValueParameter<Integer>(
				3,
				new ArrayList<Integer>(Arrays.asList(new Integer[] {null,1,2,3,4,5}))
				);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Number of Folds for Reduced Error Pruning", "Set number of folds for reduced error pruning. One fold is used as the pruning set. (Default: 3)"),
				par5);
		/**
		# Use binary splits for nominal attributes.
		$ B boolean
		**/
		ValueParameter<Boolean> par6=new ValueParameter<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Binary Splits for Nominal Attributes", "Use binary splits for nominal attributes."),
				par6);
		/**
		# Don't perform subtree raising.
		$ S boolean
		**/
		ValueParameter<Boolean> par7=new ValueParameter<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Don't perform subtree raising.", "Don't perform subtree raising."),
				par7);
		/**
		# If set, Laplace smoothing is used for predicted probabilites.
		$ A boolean
		**/
		ValueParameter<Boolean> par8=new ValueParameter<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Laplace Smoothing for Predicted Probabilites", "If set, Laplace smoothing is used for predicted probabilites."),
				par8);
		/**
		# The seed for reduced-error pruning.
		$ Q int 1 1 r 1 MAXINT
		**/
		RangedValueParameter<Integer> par9=new RangedValueParameter<Integer>(1, new Interval<Integer>(1, Integer.MAX_VALUE), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Reduced-error Pruning Seed", "The seed for reduced-error pruning. (default 1)"),
				par9);
		return lb;
	}
}
