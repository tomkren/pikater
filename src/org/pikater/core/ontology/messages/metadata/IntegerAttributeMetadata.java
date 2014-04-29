/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.ontology.messages.metadata;

/**
 *
 * @author Kuba
 */
public class IntegerAttributeMetadata extends NumericalAttributeMetadata {

	private static final long serialVersionUID = 2610042360742781585L;

	@Override
    public  String getType()
    {
        return "Integer";
    }
    
}
