/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.pikater.core.ontology.messages.metadata;

/**
 *
 * @author Kuba
 */
public class RealAttributeMetadata extends NumericalAttributeMetadata {
    @Override
    public  String getType()
    {
        return "Real";
    }
}
