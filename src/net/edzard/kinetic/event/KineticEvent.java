package net.edzard.kinetic.event;

import net.edzard.kinetic.Shape;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The wrapper class for Kinetic native events. Used in KineticGWT event handling system.
 * 
 * @author SkyCrawl
 */
public class KineticEvent extends JavaScriptObject
{
	/**
	 * Protected Ctor.
	 * Makes JSNI (GWT) happy
	 */
	protected KineticEvent() {}
	
	/**
	 * Get the event target (the node that triggered this event). Useful for handling events in ancestors.
	 * @return The original event target
	 */
	public native final Shape getEventTarget() /*-{
		return this.targetNode;
	}-*/;
	
	/**
	 * Gets the indicator whether this event has been set to be processed by user code before.
	 * @return has this event been to set to be processed?
	 */
	public native final boolean isProcessed() /*-{
		return this.processed !== undefined;
	}-*/;
	
	/**
	 * Set this event to be processed. This is useful in horizontal event stacking (e.g. node.on('click.event1 click.event2', some_handler)).
	 * Stores a special non-standard attribute to this event. This method is not a part of KineticJS.
	 */
	public native final void setProcessed() /*-{
		this.processed = true;
	}-*/;
	
	/**
	 * Gets the identification of what has processed this event in the past, indicating this event should not be processed further.
	 * @return
	 */
	public native final String getProcessedBy() /*-{
		return this.processedBy;
	}-*/;
	
	/**
	 * Method identical to "setProcessed()", except that some identification of what has processed the event is passed here.
	 * 
	 * @param processedBy some identification of what has processed this event
	 */
	public final void setProcessed(String processedBy)
	{
		setProcessed();
		setProcessedBy(processedBy);
	}
	
	private native final void setProcessedBy(String processedBy) /*-{
		this.processedBy = processedBy;
	}-*/;
	
	/**
	 * Tells Kinetic environment to stop this event from bubbling up in the node hierarchy.
	 * RED ALERT: this is currently bugged in KineticJS and is also a pending issue for future releases.
	 */
	@Deprecated
	public native final void stopVerticalPropagation() /*-{
		this.cancelBubble = true;
	}-*/;
}
