package server.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOG_REQ = Logger.getLogger(ApplicationMain.LOG_REQ_NAME);
    private static final Logger LOG_ERR = Logger.getLogger(ApplicationMain.LOG_ERR_NAME);

    private ServerSocket srv;
    private final int port;

    private static final int CLIENT_POOL_SIZE = 100;
    private final ExecutorService clientPool = Executors.newFixedThreadPool(CLIENT_POOL_SIZE);
    private final List<Future<Void>> clientList;

    private final Protocol protocol;

    public Server(int port, Protocol protocol){
        this.port = port;
        this.clientList = Collections.synchronizedList(new ArrayList<>(CLIENT_POOL_SIZE));
        this.protocol = protocol;
    }

    public void start(){
        try(ServerSocket server = new ServerSocket(port)){
            this.srv = server;
            LOG_REQ.info("Server listening on " + server.getInetAddress() + ":" + server.getLocalPort());
            while(true){
                try{
                    Socket client = server.accept();
                    startClientHandler(client);
                }
                catch(IOException ioe){
                    LOG_ERR.log(Level.SEVERE, "Accepting client connection failed!", ioe);
                }
                catch(RuntimeException re){
                    LOG_ERR.log(Level.SEVERE, "Unexpected runtime exception during client connection!", re);
                }
            }
        }
        catch(IOException ioe){
            LOG_ERR.log(Level.SEVERE, "Couldn't start server!", ioe);
        }
        catch(RuntimeException re){
            LOG_ERR.log(Level.SEVERE, "Unexpected runtime exception!", re);
        }
        finally{
            clientPool.shutdownNow();
        }
    }

    private void startClientHandler(Socket client) throws IOException {
        Callable<Void> handler = new ClientHandler(client, protocol);
        Future<Void> clientFuture = clientPool.submit(handler);
        this.clientList.add(clientFuture);
    }

    public boolean isOpen(){
        return this.srv != null && this.srv.isBound() && !this.srv.isClosed();
    }

}
