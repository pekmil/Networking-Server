package server.socket.messageboard;

import messages.Message;
import messages.messageboard.*;
import server.socket.Protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageBoardProtocol extends Protocol {

    private final List<BoardMessage> messages;

    private final AckMessage ack = new AckMessage();

    public MessageBoardProtocol(){
        this.messages = Collections.synchronizedList(new ArrayList<>());
        initMessageHandlers();
    }

    private void initMessageHandlers(){
        registerHandler(AddMessage.class, (msg) -> {
            messages.add(((AddMessage)msg).getMessage());
            return ack;
        });
        registerHandler(ListMessage.class, (msg) -> {
            ((ListMessage)msg).setBoardMessages(messages);
            return msg;
        });
    }

    @Override
    public boolean isClosingSignal(Message input) {
        return (input instanceof CloseMessage);
    }

}
