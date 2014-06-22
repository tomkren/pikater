package org.pikater.web.vaadin.gui.server.components.upload;

import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;

public class MyMultiUpload extends MultiFileUpload
{
	private static final long serialVersionUID = 1274280572318620L;
	
    public MyMultiUpload(MyUploadStateWindow uploadStateWindow, UploadFinishedHandler onFinish, boolean multiple)
    {
        super(onFinish, uploadStateWindow, multiple);
    }
    
    // ----------------------------------------------------------------------
    // OVERRIDEN INTERFACE
    
    @Override
    public MyUploadStatePanel getUploadStatePanel()
    {
    	return (MyUploadStatePanel) super.getUploadStatePanel();
    }
    
    @Override
    protected MyUploadStatePanel createStatePanel(UploadStateWindow uploadStateWindow)
    {
    	return new MyUploadStatePanel((MyUploadStateWindow) uploadStateWindow);
    }
    
    // ----------------------------------------------------------------------
    // OTHER PUBLIC INTERFACE
    
    public void addFileUploadEventsCallback(IFileUploadEvents callbacks)
    {
    	getUploadStatePanel().addUploadStartListener(callbacks);
    }
}