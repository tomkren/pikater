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

/**
 * 
 * Represents the service for Data-Transfer
 *
 */
public class DataTransferService extends FIPAService {
	
	/**
	 * Connects to a DataManager listening on given host+port and loads
	 * the data into the file with the given hash
	 * @param hash
	 * @param host
	 * @param port
	 * @throws IOException
	 */
	public static void doClientFileTransfer(String hash, String host, int port
			) throws IOException {
		
		ConsoleLogger.log(Level.INFO, "Loading " + hash + " from " + host +
				":" + port);

		try (
			Socket socket = new Socket(host, port);
		) {
			String pathString = CoreConfiguration.getDataFilesPath() +
					"temp" + System.getProperty("file.separator") + hash;
			
			Path tempPath = Paths.get(pathString);
			Files.copy(socket.getInputStream(), tempPath,
					StandardCopyOption.REPLACE_EXISTING);
			
			Path newPath =
					Paths.get(CoreConfiguration.getDataFilesPath() + hash);
			Files.move(tempPath, newPath, StandardCopyOption.ATOMIC_MOVE);

		}
	}
	
	/**
	 * Waits for a connection to the given serverSocket (blocking), uploads the
	 * file with the given hash to it and closes the socket.
	 * @param serverSocket
	 * @param hash
	 * @throws IOException
	 */
	public static void handleUploadConnection(ServerSocket serverSocket,
			String hash) throws IOException {
		
		Socket socket = null;
		try {
			try {
				socket = serverSocket.accept();
			} catch (SocketTimeoutException e) {
				ConsoleLogger.log(Level.INFO, "No request arrived");
				return;
			}

			Path path = Paths.get(CoreConfiguration.getDataFilesPath() + hash);
			Files.copy(path, socket.getOutputStream());

			serverSocket.close();
		} finally {
			if (socket != null)
				socket.close();
		}
	}
}
