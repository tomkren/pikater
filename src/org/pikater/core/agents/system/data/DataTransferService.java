package org.pikater.core.agents.system.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.pikater.core.agents.system.Agent_DataManager;

import jade.domain.FIPAService;

public class DataTransferService extends FIPAService {
	/** Connects to a DataManager listening on given host+port and loads the data into the file with the given hash */
	public static void doClientFileTransfer(String hash, String host, int port) throws IOException {
		File temp = new File(Agent_DataManager.dataFilesPath + "temp" + System.getProperty("file.separator") + hash);

		System.out.println("Loading "+hash+" from "+host+":"+port);

		try (
			Socket socket = new Socket(host, port);
			InputStream in = socket.getInputStream();
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(temp));
		) {
			byte[] buf = new byte[100*1024];
			int read;
			while ((read = in.read(buf, 0, buf.length)) > 0) {
				out.write(buf, 0, read);
			}
			out.close();

			temp.renameTo(new File(Agent_DataManager.dataFilesPath + hash));

			System.out.println("Data loaded");
		}
	}
	
	/** Waits for a connection to the given serverSocket (blocking), uploads the file with the given hash to it and closes the socket. */
	public static void handleUploadConnection(ServerSocket serverSocket, String hash) throws IOException {
		Socket socket = null;
		try {
			File source = new File(Agent_DataManager.dataFilesPath + hash);
			try {
				socket = serverSocket.accept();
			} catch (SocketTimeoutException e) {
				System.out.println("No request arrived");
				return;
			}

			System.out.println("Sending data to "+socket.getRemoteSocketAddress());

			try (
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
				BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
				) {
				byte[] buf = new byte[100 * 1024];
				int n;
				while ((n = in.read(buf)) > 0) {
					out.write(buf, 0, n);
				}

				serverSocket.close();
			}
		} finally {
			if (socket != null)
				socket.close();
		}
	}
}
