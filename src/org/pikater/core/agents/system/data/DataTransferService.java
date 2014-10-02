package org.pikater.core.agents.system.data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

import org.pikater.core.CoreConfiguration;
import org.pikater.shared.logging.core.ConsoleLogger;

import jade.domain.FIPAService;

public class DataTransferService extends FIPAService {
	/** Connects to a DataManager listening on given host+port and loads the data into the file with the given hash */
	public static void doClientFileTransfer(String hash, String host, int port) throws IOException {
		ConsoleLogger.log(Level.INFO, "Loading "+hash+" from "+host+":"+port);

		try (
			Socket socket = new Socket(host, port);
		) {
			Path temp = Paths.get(CoreConfiguration.getDataFilesPath() + "temp" + System.getProperty("file.separator") + hash);
			Files.copy(socket.getInputStream(), temp, StandardCopyOption.REPLACE_EXISTING);
			Files.move(temp, Paths.get(CoreConfiguration.getDataFilesPath() + hash), StandardCopyOption.ATOMIC_MOVE);

			//System.out.println("Data loaded");
		}
	}
	
	/** Waits for a connection to the given serverSocket (blocking), uploads the file with the given hash to it and closes the socket. */
	public static void handleUploadConnection(ServerSocket serverSocket, String hash) throws IOException {
		Socket socket = null;
		try {
			try {
				socket = serverSocket.accept();
			} catch (SocketTimeoutException e) {
				ConsoleLogger.log(Level.INFO, "No request arrived");
				return;
			}

			//System.out.println("Sending data to "+socket.getRemoteSocketAddress());

			Files.copy(Paths.get(CoreConfiguration.getDataFilesPath() + hash), socket.getOutputStream());

			serverSocket.close();
		} finally {
			if (socket != null)
				socket.close();
		}
	}
}
