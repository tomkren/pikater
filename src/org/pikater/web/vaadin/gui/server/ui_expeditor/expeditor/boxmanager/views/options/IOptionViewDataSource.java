package org.pikater.web.vaadin.gui.server.ui_expeditor.expeditor.boxmanager.views.options;

import org.pikater.core.ontology.subtrees.newOption.base.NewOption;
import org.pikater.core.ontology.subtrees.newOption.base.Value;
import org.pikater.core.ontology.subtrees.newOption.restrictions.TypeRestriction;
import org.pikater.shared.experiment.webformat.server.BoxInfoServer;

public interface IOptionViewDataSource
{
	BoxInfoServer getBox();
	NewOption getOption();
	Value getValue();
	TypeRestriction getAllowedTypes();
}