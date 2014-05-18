/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.ontology.subtrees.metadata.attributes;

/**
 *
 * @author Kuba
 */
public class RealAttributeMetadata extends NumericalAttributeMetadata {

	private static final long serialVersionUID = -4215218284827298376L;

	@Override
    public  String getType()
    {
        return "Real";
    }
}
