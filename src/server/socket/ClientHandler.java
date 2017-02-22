package server.socket;

import messages.Message;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

class ClientHandler implements Callable<Void> {

    private static final Logger LOG_REQ = Logger.getLogger(ApplicationMain.LOG_REQ_NAME);
    private static final Logger LOG_ERR = Logger.getLogger(ApplicationMain.LOG_ERR_NAME);

    private final Socket connection;

    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    private final Protocol protocol;

    ClientHandler(Socket connection, Protocol protocol) throws IOException {
        this.connection = connection;
        this.output = new ObjectOutputStream(connection.getOutputStream());
        this.input = new ObjectInputStream(connection.getInputStream());
        this.protocol = protocol;
    }

    @Override
    public Void call() throws Exception {
        LOG_REQ.info("Client connected: " + this.connection.getRemoteSocketAddress());
        try{
            while(true){
                try{
                    Message msg = (Message) input.readObject();
                    if(protocol.isClosingSignal(msg)){
                        LOG_REQ.info("Closing signal arrive from: " + this.connection.getRemoteSocketAddress());
                        break;
                    }
                    Message response = protocol.process(msg);
                    output.writeObject(response);
                    output.flush();
                    output.reset();
                }
                catch(IOException ioe){
                    LOG_ERR.log(Level.SEVERE, "Error during client message processing, closing client connection!", ioe);
                    break;
                }
            }
        }
        catch(RuntimeException re){
            LOG_ERR.log(Level.SEVERE, "Unexpected runtime exception!", re);
        }
        finally{
            closeConnection();
        }
        return null;
    }

    private void closeConnection(){
        LOG_REQ.info("Closing client connection: " + connection.getRemoteSocketAddress());
        try {
            if(input != null) input.close();
        }
        catch(IOException ioe){
            LOG_ERR.log(Level.SEVERE, "Error during inputstream closing!", ioe);
        }
        try {
            if(output != null) output.close();
        }
        catch(IOException ioe){
            LOG_ERR.log(Level.SEVERE, "Error during outputstream closing!", ioe);
        }
    }

}
