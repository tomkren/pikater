package org.pikater.web.vaadin.gui.server.components.upload;

import com.vaadin.server.StreamVariable;
import com.vaadin.server.StreamVariable.StreamingEndEvent;
import com.vaadin.server.StreamVariable.StreamingErrorEvent;
import com.vaadin.server.StreamVariable.StreamingStartEvent;
import com.wcs.wcslib.vaadin.widget.multifileupload.component.SmartMultiUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStatePanel;

public class MyUploadStatePanel extends UploadStatePanel
{
	private static final long serialVersionUID = 2373915541113299145L;
	
	private UploadState uploadState;

	public MyUploadStatePanel(MyUploadStateWindow window)
    {
        super(window);
    }
	
	public void setUploadStateInstance(UploadState uploadState)
	{
		this.uploadState = uploadState;
	}
	
	@Override
	public void setMultiUpload(SmartMultiUpload multiUpload)
	{
		super.setMultiUpload(multiUpload);
	}
	
	@Override
	public void streamingStarted(StreamingStartEvent event)
	{
		this.uploadState.onStreamingStart();
		super.streamingStarted(event);
	}
	
	@Override
	public void streamingFailed(StreamingErrorEvent event)
	{
		this.uploadState.onStreamingEnd();
		super.streamingFailed(event);
	}
	
	@Override
	public void streamingFinished(StreamingEndEvent event)
	{
		this.uploadState.onStreamingEnd();
		super.streamingFinished(event);
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
}