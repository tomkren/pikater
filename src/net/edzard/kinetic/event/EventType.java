package net.edzard.kinetic.event;

public class EventType
{
	/**
	 * Can be used with any node except stage.
	 * @author Ed
	 */
	public enum Basic implements IEventTypeEnum
	{
		MOUSEENTER,
		MOUSELEAVE,
		MOUSEOVER,
		MOUSEOUT,
		MOUSEMOVE,
		MOUSEDOWN,
		MOUSEUP,
		CLICK,
		DBLCLICK,
		DRAGSTART,
		DRAGMOVE,
		DRAGEND;
		
		@Override
		public String setName(String name)
		{
			return EventType.toNamedNativeEvent(this.toNativeEvent(), name);
		}
		
		@Override
		public String toNativeEvent()
		{
			return this.toString().toLowerCase();
		}
	}
	
	/**
	 * Can be used with any node except stage. Touch device events only.
	 */
	public enum Touch implements IEventTypeEnum
	{
		TOUCHSTART,
		TOUCHMOVE,
		TOUCHEND,
		TAP,
		DBLTAP;
		
		@Override
		public String setName(String name)
		{
			return EventType.toNamedNativeEvent(this.toNativeEvent(), name);
		}
		
		@Override
		public String toNativeEvent()
		{
			return this.toString().toLowerCase();
		}
	}
	
	/**
	 * Kinetic stage events only. Don't use with other types.
	 */
	public enum StageBasic implements IEventTypeEnum
	{
		contentMouseover,
		contentMousemove,
		contentMouseout,
		contentMousedown,
		contentMouseup,
		contentClick,
		contentDblclick;
		
		@Override
		public String setName(String name)
		{
			return EventType.toNamedNativeEvent(this.toNativeEvent(), name);
		}
		
		@Override
		public String toNativeEvent()
		{
			return this.toString();
		}
	}
	
	/**
	 * Kinetic stage events only. Don't use with other types. Touch device events only. 
	 */
	public enum StageTouch implements IEventTypeEnum
	{
		contentTouchstart,
		contentTouchmove,
		contentTouchend,
		contentTap,
		contentDblTap;
		
		@Override
		public String setName(String name)
		{
			return EventType.toNamedNativeEvent(this.toNativeEvent(), name);
		}
		
		@Override
		public String toNativeEvent()
		{
			return this.toString();
		}
	}
	
	private static String toNamedNativeEvent(String eventType, String name)
	{
		StringBuilder sb = new StringBuilder(eventType);
		sb.append('.');
		sb.append(name);
		return sb.toString();
	}
}
