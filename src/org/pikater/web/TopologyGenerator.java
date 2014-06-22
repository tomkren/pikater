package org.pikater.web;

import java.io.IOException;

import org.pikater.shared.RemoteServerInfo;
import org.pikater.shared.TopologyModel;
import org.pikater.shared.TopologyModel.ServerType;
import org.pikater.shared.XStreamHelper;
import org.pikater.shared.util.IOUtils;

import com.thoughtworks.xstream.XStream;

public final class TopologyGenerator
{
	public static void main(String[] args) throws IOException
	{
		TopologyModel model = new TopologyModel();
		model.addServer(ServerType.MASTER, new RemoteServerInfo(
				"u-pl1.ms.mff.cuni.cz",
				"19:dd:b4:3b:e3:e7:ab:44:e0:6a:eb:0e:0d:ae:d1:b1",
				"balcs7am",
				"",
				"/afs/ms/u/b/balcs7am/BIG/Softwerak")
		);
		model.addServer(ServerType.SLAVE, new RemoteServerInfo(
				"u-pl2.ms.mff.cuni.cz",
				"19:dd:b4:3b:e3:e7:ab:44:e0:6a:eb:0e:0d:ae:d1:b1",
				"balcs7am",
				"",
				"/afs/ms/u/b/balcs7am/BIG/Softwerak")
		);
		model.addServer(ServerType.SLAVE, new RemoteServerInfo(
				"u-pl3.ms.mff.cuni.cz",
				"19:dd:b4:3b:e3:e7:ab:44:e0:6a:eb:0e:0d:ae:d1:b1",
				"balcs7am",
				"",
				"/afs/ms/u/b/balcs7am/BIG/Softwerak")
		);
		
		XStream serializer = XStreamHelper.getSerializerWithProcessedAnnotations(TopologyModel.class);
		XStreamHelper.serializeToFile(IOUtils.joinPathComponents(IOUtils.getAbsoluteWEBINFCONFPath(), "jadeTopology1.xml"), model, serializer);
	}
}
