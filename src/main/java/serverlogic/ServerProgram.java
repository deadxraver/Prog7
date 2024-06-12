package serverlogic;

import commands.client.*;
import elements.MovieCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;

public class ServerProgram {

	private ForkJoinPool forkJoinPool;
	protected volatile MovieCollection movieCollection;
	protected Logger logger;
	private ServerSocketChannel serverSocketChannel;
	private int port;

	public ServerProgram() {
		this(Integer.parseInt(ResourceBundle.getBundle("connection_details_en").getString("port")));
	}

	public ServerProgram(int port) {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("database_en");
		logger = LoggerFactory.getLogger(this.getClass());
		this.forkJoinPool = ForkJoinPool.commonPool();
		this.port = port;
		this.movieCollection = DBManipulation.getCollection();
	}

	public void run() {
		prepare();

		while (true) {
			ServerConsole.readConsole();
			try {
				SocketChannel socketChannel = serverSocketChannel.accept();
				if (socketChannel == null) continue;
				logger.info("client found");
				ClientHandler clientHandler = new ClientHandler(socketChannel);
				Thread t = new Thread(clientHandler);
				forkJoinPool.submit(t);
			} catch (IOException ignored) {
			}
		}
	}

	private void prepare() {
		try {
			forkJoinPool = ForkJoinPool.commonPool();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(port));
			serverSocketChannel.configureBlocking(false);
			prepareCommands();
		} catch (IOException e) {
			logger.error(e.getMessage());
			System.exit(1);
		}
		logger.info("prepare: preparation successful");
	}







	private void prepareCommands() {
		new Add(movieCollection);
		new AddIfMax((movieCollection));
		new Clear(movieCollection);
		new MaxByMpaaRating(movieCollection);
		new PrintFieldAscendingOperator(movieCollection);
		new RemoveAllByOscarsCount(movieCollection);
		new RemoveById(movieCollection);
		new RemoveHead(movieCollection);
		new RemoveLower(movieCollection);
		new Show(movieCollection);
		new Update(movieCollection);
	}
}
