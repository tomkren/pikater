package org.pikater.web.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.pikater.core.CoreConstant.SlotCategory;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfo;
import org.pikater.core.ontology.subtrees.agentInfo.AgentInfos;
import org.pikater.core.ontology.subtrees.agentInfo.Slot;
import org.pikater.core.ontology.subtrees.newOption.NewOptions;
import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.restrictions.RangeRestriction;
import org.pikater.core.ontology.subtrees.newOption.restrictions.SetRestriction;
import org.pikater.core.ontology.subtrees.newOption.values.BooleanValue;
import org.pikater.core.ontology.subtrees.newOption.values.DoubleValue;
import org.pikater.core.ontology.subtrees.newOption.values.FloatValue;
import org.pikater.core.ontology.subtrees.newOption.values.IntegerValue;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkRange;
import org.pikater.core.ontology.subtrees.newOption.values.QuestionMarkSet;
import org.pikater.core.ontology.subtrees.newOption.values.interfaces.IValueData;
import org.pikater.web.experiment.server.BoxType;

public class AgentInfoCollection implements Iterable<AgentInfo>
{
	private final Set<AgentInfo> boxes;
	
	private AgentInfoCollection()
	{
		this.boxes = new HashSet<AgentInfo>(); 
	}
	
	@Override
	public Iterator<AgentInfo> iterator()
	{
		return boxes.iterator();
	}
	
	private boolean addDefinition(AgentInfo info)
	{
		return boxes.add(info);
	}
	
	/**
	 * This method is guaranteed to return the request object or throw an exception
	 * otherwise.
	 * @param ontologyClassName
	 * @param agentClassName
	 * @return
	 */
	public AgentInfo getUnique(String ontologyClassName, String agentClassName)
	{
		List<AgentInfo> result = new ArrayList<AgentInfo>();
		for(AgentInfo info : boxes)
		{
			if(info.isIdentifiedBy(ontologyClassName, agentClassName))
			{
				result.add(info);
			}
		}
		switch(result.size())
		{
			case 0:
				throw new IllegalStateException(String.format("No agent info was found for ontology class "
						+ "name '%s' and agent class name '%s'.", ontologyClassName, agentClassName));
			case 1:
				return result.get(0);
			default:
				throw new IllegalStateException("Duplicate AgentInfo found by ontology and agent class names.");
		}
	}
	
	public List<AgentInfo> getListByType(BoxType type)
	{
		List<AgentInfo> result = new ArrayList<AgentInfo>(); 
		for(AgentInfo info : boxes)
		{
			if(type.toOntologyClass().getName().equals(info.getOntologyClassName()))
			{
				result.add(info);
			}
		}
		return result;
	}

	//---------------------------------------------------------
	// SOME STATIC METHODS TO CONSTRUCT FROM DATA
	
	public static AgentInfoCollection getFrom(AgentInfos boxesFromCore)
	{
		AgentInfoCollection agentInfoCollection = new AgentInfoCollection();
		for(AgentInfo info : boxesFromCore.getAgentInfos())
		{
			agentInfoCollection.addDefinition(info);
		}
		return agentInfoCollection;
	}
	
	public static AgentInfoCollection getDummyBoxes()
	{
		AgentInfoCollection agentInfoCollection = new AgentInfoCollection();
		for(BoxType type : BoxType.values())
		{
			AgentInfo agentInfo = new AgentInfo();
			agentInfo.setOntologyClassName(type.toOntologyClass().getName());
			agentInfo.setAgentClassName(AgentInfoCollection.class.getName());
			agentInfo.setDescription(String.format("Some kind of a '%s' box.", type.name()));

			String name = null;
			switch(type)
			{
				case CHOOSE:
					name = "DummyChooser";
					break;
				case COMPUTE:
					name = "DummyComputeAgent";
					break;
				case PROCESS_DATA:
					name = "DummyDataProcessor";
					break;
				case OPTION:
					name = "DummyOptionModifier";
					break;
				case INPUT:
					name = "DumyInputBox";
					break;
				case MISC:
					name = "DummyBox";
					break;
				case OUTPUT:
					name = "DummyOutputBox";
					break;
				case SEARCH:
					name = "DummySearchAgent";
					break;
				case COMPOSITE:
					name = "DummyComposite";
					break;
				default:
					throw new IllegalStateException("Unknown box type: " + type.name());
			}
			agentInfo.setName(name);

			NewOptions options = new NewOptions();
			options.addOption(new NewOption("IntRange", new IntegerValue(5), new RangeRestriction(new IntegerValue(2), new IntegerValue(10))));
			options.addOption(new NewOption("IntSet", new IntegerValue(5), new SetRestriction(false, new ArrayList<IValueData>(Arrays.asList(
					new IntegerValue(2),
					new IntegerValue(3),
					new IntegerValue(5),
					new IntegerValue(10))))
					));
			options.addOption(new NewOption("Double", new DoubleValue(1)));
			options.addOption(new NewOption("Boolean", new BooleanValue(true)));
			options.addOption(new NewOption("Float", new FloatValue(1)));

			options.addOption(new NewOption("QuestionMarkRange", new QuestionMarkRange(new IntegerValue(5), new IntegerValue(10), 3)));
			options.addOption(new NewOption(
					"QuestionMarkSet",
					new QuestionMarkSet(3, new ArrayList<IValueData>(Arrays.asList(
							new IntegerValue(5),
							new IntegerValue(6),
							new IntegerValue(9),
							new IntegerValue(10),
							new IntegerValue(11)))) /*,
						new SetRestriction(false, new ArrayList<IValueData>(Arrays.asList(
								new IntegerValue(5),
								new IntegerValue(6),
								new IntegerValue(7),
								new IntegerValue(8),
								new IntegerValue(9),
								new IntegerValue(10),
								new IntegerValue(11)
						))))); */
					));

			Slot slotInput_test = new Slot("testSlot", SlotCategory.DATA_GENERAL, "Test input slot.");
			Slot slotOutput_test = new Slot("testSlot", SlotCategory.DATA_GENERAL, "Test output slot.");

			agentInfo.setOptions(options);
			agentInfo.setInputSlots(Arrays.asList(slotInput_test));
			agentInfo.setOutputSlots(Arrays.asList(slotOutput_test));
			agentInfoCollection.addDefinition(agentInfo);
		}
		return agentInfoCollection;
	}
}