/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.ontology.subtrees.metadata.attributes;

/**
 *
 * @author Kuba
 */
public class CategoricalAttributeMetadata extends AttributeMetadata {

	private static final long serialVersionUID = 5050710635914157333L;

	private int _numberOfCategories;

    public int getNumberOfCategories() {
        return _numberOfCategories;
    }

    public void setNumberOfCategories(int _numberOfCategories) {
        this._numberOfCategories = _numberOfCategories;
    }
    
    @Override
    public  String getType()
    {
        return "Categorical";
    }
    
}
