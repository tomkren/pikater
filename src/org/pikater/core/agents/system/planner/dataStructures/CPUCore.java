package org.pikater.core.agents.system.planner.dataStructures;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import jade.core.AID;

public class CPUCore {

	private volatile AID aID;
	private volatile int coreID;
	
	public CPUCore (AID aID, int coreID) {
		this.aID = aID;
		this.coreID = coreID;
	}

	public AID getAID() {
		return aID;
	}

	public int getCoreID() {
		return coreID;
	}

    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(aID.getHap()).
            append(coreID).
            toHashCode();
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof CPUCore))
             return false;
         if (obj == this)
             return true;

         CPUCore rhs = (CPUCore) obj;
         return new EqualsBuilder().
             append(aID.getHap(), rhs.getAID().getName()). //getHap()).
             append(coreID, rhs.getCoreID()).
             isEquals();
     }
	
}
