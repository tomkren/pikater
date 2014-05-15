package org.pikater.web.vaadin.gui.server.components.upload;

import com.wcs.wcslib.vaadin.widget.multifileupload.ui.MultiFileUpload;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadFinishedHandler;
import com.wcs.wcslib.vaadin.widget.multifileupload.ui.UploadStateWindow;

public class MyMultiUpload extends MultiFileUpload
{
	private static final long serialVersionUID = 1274280572318620L;
	
	public MyMultiUpload(MyUploadStateWindow uploadStateWindow, UploadFinishedHandler handler, UploadState uploadState)
    {
        super(handler, uploadStateWindow);
        
        ((MyUploadStatePanel) getUploadStatePanel()).setUploadStateInstance(uploadState);
    }

    public MyMultiUpload(MyUploadStateWindow uploadStateWindow, UploadFinishedHandler handler, UploadState uploadState, boolean multiple)
    {
        super(handler, uploadStateWindow, multiple);
        
        ((MyUploadStatePanel) getUploadStatePanel()).setUploadStateInstance(uploadState);
    }
    
    @Override
    protected MyUploadStatePanel createStatePanel(UploadStateWindow uploadStateWindow)
    {
        return new MyUploadStatePanel((MyUploadStateWindow) uploadStateWindow);
    }
}
