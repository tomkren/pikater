package org.pikater.core.ontology.subtrees.agentinfo;

import org.pikater.core.CoreConstant;
import org.pikater.core.CoreConstant.SlotCategory;
import org.pikater.core.CoreConstant.SlotContent;
import org.pikater.shared.util.ICloneable;

import jade.content.Concept;

public class Slot implements Concept, ICloneable {
	private static final long serialVersionUID = -1146617082338754196L;

	private String name;
	private String description;
	private String categoryName;

	/**
	 * @deprecated Should only be used internally and by Jade.
	 */
	@Deprecated
	public Slot() {
	}

	public Slot(String name, SlotCategory category) {
		this.name = name;
		this.categoryName = category.name();
	}

	public Slot(String name, SlotCategory category, String description) {
		this(name, category);
		this.description = description;
	}

	public Slot(SlotContent contentType) {
		this(contentType.getSlotName(), contentType.getCategory());
	}

	public Slot(SlotContent contentType, String description) {
		this(contentType);
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public Slot clone() {
		Slot result;
		try {
			result = (Slot) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		result.setName(name);
		result.setDescription(description);
		result.setCategoryName(categoryName);
		return result;
	}

	/*
	 * Some convenience interface.
	 */
	public boolean isCompatibleWith(Slot otherSlot) {
		return true;
		// TODO: a hack around...
		// return slotType.equals(otherSlot.slotType) when the experiment data
		// streams are a bit more "standardized"
	}

	public boolean isErrorSlot() {
		return SlotCategory.valueOf(categoryName) == CoreConstant.SlotCategory.ERROR;
	}
}