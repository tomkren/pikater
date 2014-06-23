package org.pikater.web.vaadin.gui.server.components.upload;

import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.server.StreamVariable;
import com.vaadin.server.StreamVariable.StreamingEndEvent;
import com.vaadin.server.StreamVariable.StreamingErrorEvent;
import com.vaadin.server.StreamVariable.StreamingStartEvent;
import com.wcs.wcslib.vaadin.widget.multifileupload.component.SmartMultiUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStatePanel;

public class MyUploadStatePanel extends UploadStatePanel
{
	private static final long serialVersionUID = 2373915541113299145L;
	
	private final Collection<IFileUploadEvents> fileUploadEventsCallbacks;
	private final MyUploadReceiver uploadReceiver;

	public MyUploadStatePanel(MyUploadStateWindow window, MyUploadReceiver uploadReceiver)
    {
        super(window, uploadReceiver);
        
        this.uploadReceiver = uploadReceiver;
        
        this.fileUploadEventsCallbacks = new ArrayList<IFileUploadEvents>();
    }
	
	// ------------------------------------------------------------
	// INHERITED INTERFACE
	
	@Override
	public void setMultiUpload(SmartMultiUpload multiUpload)
	{
		super.setMultiUpload(multiUpload);
	}
	
	@Override
	public void streamingStarted(StreamingStartEvent event)
	{
		// TODO: check whether maximum number of uploads has been reached and if so, wait until a slot is open
		super.streamingStarted(event); // always call this first, otherwise unexpected problems may occur
		for(IFileUploadEvents callbacks : fileUploadEventsCallbacks)
		{
			callbacks.uploadStarted(event);
		}
	}
	
	@Override
	public void streamingFailed(StreamingErrorEvent event)
	{
		super.streamingFailed(event); // always call this first, otherwise unexpected problems may occur
		for(IFileUploadEvents callbacks : fileUploadEventsCallbacks)
		{
			callbacks.uploadFailed(event);
		}
	}
	
	@Override
	public void streamingFinished(StreamingEndEvent event)
	{
		super.streamingFinished(event); // always call this first, otherwise unexpected problems may occur
		for(IFileUploadEvents callbacks : fileUploadEventsCallbacks)
		{
			callbacks.uploadFinished(event, uploadReceiver.getUploadedFileHandler());
		}
	}

	/**
	 * This can be used to slow down the file upload speed since the thread receiving
	 * input bytes can be put to sleep for a period of time.
	 */
    @Override
    public void onProgress(StreamVariable.StreamingProgressEvent event)
    {
    	// System.out.println("Bytes received: " + event.getBytesReceived());
    	/*
        try
        {
            Thread.sleep((int) uploadSpeed);
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(WidgetTestApplication.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        super.onProgress(event);
    }
    
    // ------------------------------------------------------------
 	// OTHER PUBLIC INTERFACE
 	
 	public void addUploadStartListener(IFileUploadEvents callbacks)
 	{
 		this.fileUploadEventsCallbacks.add(callbacks);
 	}
}