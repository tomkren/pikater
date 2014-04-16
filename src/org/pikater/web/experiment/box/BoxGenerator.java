package org.pikater.web.experiment.box;

import java.util.ArrayList;
import java.util.Arrays;

import org.pikater.core.agents.experiment.computing.Agent_WekaCA;
import org.pikater.core.options.LogicalUnitDescription;
import org.pikater.shared.experiment.Box;
import org.pikater.shared.experiment.BoxType;
import org.pikater.shared.experiment.options.ArrayOption;
import org.pikater.shared.experiment.options.RangeOption;
import org.pikater.shared.experiment.options.ValueOption;
import org.pikater.shared.experiment.resources.ParamResource;
import org.pikater.shared.util.Interval;
import org.pikater.web.experiment.box.LeafBox.ParameterVisibility;

public class BoxGenerator {
	
	public static LeafBox getChooseXValueBox(){
		Box lu=new Box();
		lu.setName("Choose X Values Agent");
		lu.setDescription("Search which Choose X Values");
		lu.setType(BoxType.SEARCH);
		lu.setPicture("picture3.jpg");

		
		LeafBox lb = new LeafBox(lu);
		
		//# default number of values to try for each option (default 5)
		//$ N int 1 1 r 1 2000 5 number_of_values_to_try Default number of values to try for each option
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Number of Values to Try", "Number of values to try for each option"),
				new RangeOption<Integer>(1, 2000));
		return lb;
	}
	
	/**
	 * NOT IMPLEMENTED YET 
	 * Returns null!!!!
	 * @return
	 */
	public static LeafBox getCrossValidationBox(){
		Box lu = new Box();
		lu.setName("Cross Validation");
		lu.setAgentClass(Agent_WekaCA.class);
		lu.setDescription("Computing agent used for training neural networks deterministic library WEKA. As a training method is used default Cross Validation WEKA method.");
		lu.setType(BoxType.METHOD);
		lu.setOntology(null);
		lu.setPicture("picture2.jpg");
		
		LeafBox lb = new LeafBox(lu);
		
		//# default number of folds
		//$ F int 1 1 r 1 100 5 folds Folds
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("",""),
				new RangeOption<Integer>(1, 100)
				);

		return lb;
	}
	
	
	public static LeafBox getEASearchBox(){
		Box lu = new Box();
		lu.setName("EASearch");
		lu.setDescription("Searcher using Evolution algorithm");
		lu.setType(BoxType.SEARCH);
		lu.setPicture("picture1.jpg");
		
		LeafBox lb=new LeafBox(lu);
		
		/**
		# minimum error rate (default 0.1)
		$ E float 1 1 r 0 1 0.1 error_rate Error rate
		**/
		RangeOption<Float> par=new RangeOption<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Minimum Error Rate", "Minimum error rate (default 0.1)"),
				par);
		/**
		# maximal number of generations (default 10)
		$ M int 1 1 r 1 1000 10 generations Generations
		**/
		RangeOption<Integer> par2=new RangeOption<Integer>(10, new Interval<Integer>(1, 1000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Maximal Number of Generations", "Maximal number of generations (default 10)"),
				par2);
		/**
		# Mutation rate (default 0.2)
		$ T float 1 1 r 0 1 0.2 mutation_rate Mutation rate
		**/
		RangeOption<Float> par3=new RangeOption<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Mutation Rate", "Mutation rate (default 0.2)"),
				par3);
		/**
		# Crossover probability (default 0.5)
		$ X float 1 1 r 0 1  0.5 xover_rate Xover rate
		**/
		RangeOption<Float> par4=new RangeOption<Float>(0.5f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Crossover Probability", "Crossover probability (default 0.5)"),
				par4);
		/**
		# population size (default 10)
		$ P int 1 1 r 1 100 10 pop_size Population
		**/
		RangeOption<Integer> par5=new RangeOption<Integer>(10, new Interval<Integer>(1, 100), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Population Size", "Population size (default 10)"),
				par5);
		/**
		# Maximum nubmer of option evaluations
		$ I int 1 1 r 1 100000 100 maxEval Maximum evaluations
		**/
		RangeOption<Integer> par6=new RangeOption<Integer>(100, new Interval<Integer>(1,100000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Maximum Number of Option Evaluations", "Maximum number of option evaluations (default 100)"),
				par6);
		/**
		# Mutation rate per field in individual (default 0.2)
		$ F float 1 1 r 0 1 0.2 mutation_rate_per_field Mutation rate per field in individual
		**/
		RangeOption<Float> par7=new RangeOption<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Mutation Rate per Field in Individual", "Mutation rate per field in individual (default 0.2)"),
				par7);
		/**
		# The percentage of elite individuals (default 0.1)
		$ L float 1 1 r 0 1 0.1 eliteSize Elite size
		**/
		RangeOption<Float> par8=new RangeOption<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("The Percentage of Elite Individuals", "The percentage of elite individuals (default 0.1)"),
				par8);
		
		return lb;
	}
	
	public static LeafBox getGASearchBox(){
		Box lu = new Box();
		lu.setName("GASearch");
		lu.setDescription("Searcher using Genetic algorithm");
		lu.setType(BoxType.SEARCH);
		lu.setPicture("picture15.jpg");
		
		LeafBox lb = new LeafBox(lu);
		/**
		# minimum error rate (default 0.1)
		$ E float 1 1 r 0 1 0.1 error_rate Error rate
		**/
		RangeOption<Float> par1=new RangeOption<Float>(0.1f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Minimum Error Rate", "Minimum error rate (default 0.1)"),
				par1);
		/**
		# maximal number of generations (default 10)
		$ M int 1 1 r 1 1000 10 generations Generations
		**/
		RangeOption<Integer> par2=new RangeOption<Integer>(10, new Interval<Integer>(1,1000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Maximal Number of Genereations", "Maximal number of generations (default 10)"),
				par2);
		/**
		# Mutation rate (default 0.2)
		$ T float 1 1 r 0 1 0.2 mutation_rate Mutation rate
		**/
		RangeOption<Float> par3=new RangeOption<Float>(0.2f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Mutation Rate", "Mutation rate (default 0.2)"),
				par3);
		/**
		# Crossover probability (default 0.5)
		$ X float 1 1 r 0 1  0.5 xover_rate Xover rate
		**/
		RangeOption<Float> par4=new RangeOption<Float>(0.5f, new Interval<Float>(0.0f, 1.0f), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Crossover Probability", "Crossover probability (default 0.5)"),
				par4);
		/**
		# population size (default 5)
		$ P int 1 1 r 1 100 5 pop_size Population
		**/
		RangeOption<Integer> par5=new RangeOption<Integer>(5, new Interval<Integer>(1,100), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Population Size", "Population size (default 5)"),
				par5);
		/**
		# Size of tournament in selection (default 2)
		$ S int 1 1 r 1 100 2 tournament_size Tournament size
		**/
		RangeOption<Integer> par6=new RangeOption<Integer>(2, new Interval<Integer>(1,100), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Size of Tournament in Selection", "Size of tournament in selection (default 2)"),
				par6);
		
		
		return lb;
	}
	
	public static LeafBox getGridSearchBox(){
		Box lu=new Box();
		lu.setName("GridSearch");
		lu.setDescription("GridSearch description ....");
		lu.setType(BoxType.SEARCH);
		
		LeafBox lb = new LeafBox(lu);
		/**
		# maximum block size (default 10)
		$ B int 1 1 r 0 100000 10 maxBlock Block size
		**/
		RangeOption<Integer> par1=new RangeOption<Integer>(10, new Interval<Integer>(0,100000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Maximum Block Size", "Maximum block size (default 10)"),
				par1);
		/**
		# default number of tries (default 10)
		$ N int 1 1 r 0 1000 10 defaultTries Default tries
		**/
		RangeOption<Integer> par2=new RangeOption<Integer>(10, new Interval<Integer>(0,1000), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Default Number of Tries", "Default number of tries (default 10)"),
				par2);
		
		/**
		# zero for logarithmic steps (default 0.000000001)
		$ Z float 1 1 r 0 1000 0.000000001 logZero Zero for logarithmic steps
		**/
		RangeOption<Float> par3=new RangeOption<Float>(0.000000001f, new Interval<Float>(0.0f, 1000.0f), true);
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
		Box lu=new Box();
		
		LeafBox lb=new LeafBox(lu);
		/**
		# Use unpruned tree.
		$ U boolean
		**/
		ValueOption<Boolean> par1=new ValueOption<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Use Unpruned Tree", "Use unpruned tree (default false)"),
				par1);
		/**
		# Set confidence threshold for pruning. (Default: 0.25) (smaller values incur more pruning).
		# $ C float 1 1 r 0.0001 0.6
		$ C float 1 1 s null, null, null, 0.0001, 0.1, 0.2, 0.25, 0.3, 0.4 
		**/
		ArrayOption<Float> par2=new ArrayOption<Float>(
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
		RangeOption<Integer> par3=new RangeOption<Integer>(2, new Interval<Integer>(1, 10), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Minimum Number of Instances per Leaf", "Set minimum number of instances per leaf. (Default: 2)"),
				par3);
		/**
		# Use reduced error pruning. No subtree raising is performed.
		$ R boolean
		**/
		ValueOption<Boolean> par4=new ValueOption<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Reduced Error Pruning", "Use reduced error pruning. No subtree raising is performed."),
				par4);
		/**
		# Set number of folds for reduced error pruning. One fold is used as the pruning set. (Default: 3)
		$ N int 1 1 s null, 1, 2, 3, 4, 5
		**/
		ArrayOption<Integer> par5=new ArrayOption<Integer>(
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
		ValueOption<Boolean> par6=new ValueOption<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Binary Splits for Nominal Attributes", "Use binary splits for nominal attributes."),
				par6);
		/**
		# Don't perform subtree raising.
		$ S boolean
		**/
		ValueOption<Boolean> par7=new ValueOption<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Don't perform subtree raising.", "Don't perform subtree raising."),
				par7);
		/**
		# If set, Laplace smoothing is used for predicted probabilites.
		$ A boolean
		**/
		ValueOption<Boolean> par8=new ValueOption<Boolean>(false);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Laplace Smoothing for Predicted Probabilites", "If set, Laplace smoothing is used for predicted probabilites."),
				par8);
		/**
		# The seed for reduced-error pruning.
		$ Q int 1 1 r 1 MAXINT
		**/
		RangeOption<Integer> par9=new RangeOption<Integer>(1, new Interval<Integer>(1, Integer.MAX_VALUE), true);
		lb.addParameter(
				ParameterVisibility.USER_EDITABLE,
				new ParamResource("Reduced-error Pruning Seed", "The seed for reduced-error pruning. (default 1)"),
				par9);
		return lb;
	}
}
