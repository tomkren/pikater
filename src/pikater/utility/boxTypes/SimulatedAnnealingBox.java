package pikater.utility.boxTypes;

import pikater.utility.boxTypes.BoxesModel.BoxCategory;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UDoubleInterval;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UDoubleParameter;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UIntegerInterval;
import pikater.utility.pikaterDatabase.tables.experiments.parameters.UIntegerParameter;
import pikater.utility.pikaterDatabase.tables.experiments.slots.ParametersProviderSlot;
import pikater.utility.pikaterDatabase.tables.experiments.slots.UniversalSlot;

public class SimulatedAnnealingBox extends Box
{
	public SimulatedAnnealingBox()
	{
		super("SimulatedAnnealing-Searcher", "pikater/Agent_SimulatedAnnealing", BoxCategory.SEARCHER.name(), null, "picture2.jpg", "Searcher is using to find values "
				+ "of parameters for computing agents. For search the solution is used method simulated annaling.");
		
		UDoubleInterval intervalE = new UDoubleInterval();
		intervalE.setMin(0.0);
		intervalE.setMax(1.0);
		UDoubleParameter boxParam_E = new UDoubleParameter();
		boxParam_E.setName("E");
		boxParam_E.setValue(0.1);
		boxParam_E.setRange(intervalE);
		addParameter(boxParam_E);

		UIntegerInterval intervalM = new UIntegerInterval();
		intervalM.setMin(1);
		intervalM.setMax(1000);
		UIntegerParameter boxParam_M = new UIntegerParameter();
		boxParam_M.setName("M");
		boxParam_M.setValue(50);
		boxParam_M.setRange(intervalM);
		addParameter(boxParam_M);

		UDoubleInterval intervalT = new UDoubleInterval();
		intervalT.setMin(0.0);
		intervalT.setMax(100.0);
		UDoubleParameter boxParam_T = new UDoubleParameter();
		boxParam_T.setName("T");
		boxParam_T.setValue(1);
		boxParam_T.setRange(intervalT);
		addParameter(boxParam_T);

		UDoubleInterval intervalS = new UDoubleInterval();
		intervalS.setMin(0.0);
		intervalS.setMax(1.0);
		UDoubleParameter boxParam_S = new UDoubleParameter();
		boxParam_S.setName("S");
		boxParam_S.setValue(0.5);
		boxParam_S.setRange(intervalS);
		addParameter(boxParam_S);

		UniversalSlot boxparametersOutputSlots = new ParametersProviderSlot();
		boxparametersOutputSlots.setSlotName("Found parameters");
		addOutputSlot(boxparametersOutputSlots);
	}
}
