package org.pikater.web.vaadin.gui.server.components.upload;

import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;

public class MyMultiUpload extends MultiFileUpload
{
	private static final long serialVersionUID = 1274280572318620L;
	
    public MyMultiUpload(MyUploadStateWindow uploadStateWindow, boolean multiple)
    {
        super(null, uploadStateWindow, multiple);
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
    	return new MyUploadStatePanel((MyUploadStateWindow) uploadStateWindow, new MyUploadReceiver());
    }
    
    // ----------------------------------------------------------------------
    // OTHER PUBLIC INTERFACE
    
    public void addFileUploadEventsCallback(IFileUploadEvents callbacks)
    {
    	getUploadStatePanel().addUploadStartListener(callbacks);
    }
}