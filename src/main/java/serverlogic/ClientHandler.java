package serverlogic;

import datapacks.RequestPackage;
import datapacks.ResponsePackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SocketChannel;

public class ClientHandler implements Runnable {
	private final SocketChannel socketChannel;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private int ioCounter = 0;

	public ClientHandler(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
		logger.info("new client");
	}

	@Override
	public void run() {
		logger.info("started execution in thread #{}", Thread.currentThread());
		Socket socket = socketChannel.socket();
		while (true) {
			try {
				RequestPackage requestPackage = getRequest(socket);
				ResponsePackage responsePackage = processRequest(requestPackage);
				sendResponse(responsePackage, socket);
			} catch (SocketException e) {
				logger.info("Client disconnected");
				return;
			} catch (IOException | ClassCastException | ClassNotFoundException e) {
				logger.error(e.getMessage());
				ioCounter++;
				if (ioCounter > 100) {
					logger.error("something is wrong, the client will be disconnected");
					return;
				}
			}
		}
	}

	private ResponsePackage processRequest(RequestPackage requestPackage) {
		if (requestPackage == null) {
			logger.error("processRequest: failed to process, null request package");
			return new ResponsePackage(
					true,
					"processRequest: something unexpected happened",
					null
			);
		}
		logger.info("processRequest: response generated");
		return requestPackage.command().run(
				requestPackage.username(),
				requestPackage.password(),
				requestPackage.args()
		);
	}

	private RequestPackage getRequest(Socket socket) throws IOException, ClassNotFoundException {
		ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
		logger.info("reading request");
		return (RequestPackage) objectInputStream.readObject();
	}

	private void sendResponse(ResponsePackage responsePackage, Socket socket) throws IOException {
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
		objectOutputStream.writeObject(responsePackage);
		objectOutputStream.flush();
		logger.info("Response successfully sent");
	}
}
