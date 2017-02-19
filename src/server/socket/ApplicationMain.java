package server.socket;

import server.socket.messageboard.MessageBoardProtocol;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ApplicationMain {

    static final String LOG_REQ_NAME = "server-request";
    static final String LOG_ERR_NAME = "error";

    private static final int LOG_FILESIZE_LIMIT = 50_000_000; //byte
    private static final int LOG_FILE_COUNT = 3;
    private static final boolean LOG_FILE_APPEND = true;

    private static final Logger LOG_REQ = Logger.getLogger(LOG_REQ_NAME);
    private static final String LOG_REQ_FILE = "c:/temp/NetworkingServer/server-request-%g.log";
    private static final Logger LOG_ERR = Logger.getLogger(LOG_ERR_NAME);
    private static final String LOG_ERR_FILE = "c:/temp/NetworkingServer/error-%g.log";

    private static final int SERVER_PORT = 3333;

    public static void main(String[] args){
        try {
            initLogs();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            System.err.println("Error initializing log files! - " + ioe.getMessage());
            System.exit(-1);
        }
        Server server = new Server(SERVER_PORT, new MessageBoardProtocol());
        server.start();
    }

    private static void initLogs() throws IOException {
        SimpleFormatter formatter = new SimpleFormatter();

        FileHandler reqfh = new FileHandler(LOG_REQ_FILE, LOG_FILESIZE_LIMIT, LOG_FILE_COUNT, LOG_FILE_APPEND);
        reqfh.setFormatter(formatter);
        reqfh.setLevel(Level.INFO);
        LOG_REQ.addHandler(reqfh);
        LOG_REQ.setUseParentHandlers(true);

        FileHandler errfh = new FileHandler(LOG_ERR_FILE, LOG_FILESIZE_LIMIT, LOG_FILE_COUNT, LOG_FILE_APPEND);
        errfh.setFormatter(formatter);
        errfh.setLevel(Level.WARNING);
        LOG_ERR.addHandler(errfh);
        LOG_ERR.setUseParentHandlers(true);
    }

}
