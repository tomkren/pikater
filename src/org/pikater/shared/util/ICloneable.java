package org.pikater.shared.util;

/**
 * A special interface that subcedes {@link Cloneable}. Redefines
 * the original clone method to one that:
 * <ul>
 * <li> Allows better type safety when dealing with cloning.
 * <li> Hides the original clone method's {@link CloneNotSupportedException}
 * so that it won't propagate to subclasses where no such check is needed
 * if custom clone implementation (this interface) is provided.
 * </ul>
 * 
 * As a convenience, this interface extends the original one
 * and thus takes the responsibility for programmers to implement
 * it in root ancestor types.
 * 
 * @author SkyCrawl
 */
public interface ICloneable extends Cloneable
{
	ICloneable clone(); 
}