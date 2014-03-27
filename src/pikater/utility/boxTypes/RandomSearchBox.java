package pikater.utility.boxTypes;

import jade.domain.FIPAAgentManagement.Search;
import pikater.utility.boxTypes.BoxesModel.BoxCategory;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.Interval;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UDoubleInterval;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UDoubleParameter;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UIntegerInterval;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UIntegerParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.ParametersProviderSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class RandomSearchBox extends Box
{
	public RandomSearchBox()
	{
		super("Random-Searcher", "pikater/Agent_RandomSearch", BoxCategory.SEARCHER.name(), Search.class, "picture1.jpg", "Searcher is using to find values of parameters for "
				+ "computing agents. For search the solution is used random generator.");
		
		// XSTREAM should have no problems with this:
		// Interval<Double> interval = new Interval<Double>(0.0, 0.1);
		
		UDoubleInterval intervalE = new UDoubleInterval();
		intervalE.setMin(0.0);
		intervalE.setMax(1.0);
		UDoubleParameter boxParam_E = new UDoubleParameter();
		boxParam_E.setName("E");
		boxParam_E.setValue(0.01);
		boxParam_E.setRange(intervalE);
		addParameter(boxParam_E);

		UIntegerInterval intervalM = new UIntegerInterval();
		intervalM.setMin(1);
		intervalM.setMax(100000);
		UIntegerParameter boxParam_M = new UIntegerParameter();
		boxParam_M.setValue(10);
		boxParam_M.setName("M");
		boxParam_M.setRange(intervalM);
		addParameter(boxParam_M);

		UniversalSlot boxparametersOutputSlots = new ParametersProviderSlot();
		boxparametersOutputSlots.setSlotName("Found parameters");
		addOutputSlot(boxparametersOutputSlots);
	}
}
