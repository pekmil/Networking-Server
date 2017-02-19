package server.socket;

import messages.Message;

@FunctionalInterface
public interface MessageHandler {

    public Message handleMessage(Message input) throws Exception;

}
