package server.socket;

import messages.Message;
import messages.messageboard.ErrorMessage;

import java.util.HashMap;
import java.util.Map;

public abstract class Protocol {

    private final Map<Class, MessageHandler> handlers;

    public Protocol(){
        this.handlers = new HashMap<>();
    }

    protected void registerHandler(Class type, MessageHandler handler){
        handlers.put(type, handler);
    }

    public Message process(Message input){
        if(handlers.containsKey(input.getClass())){
            try {
                return handlers.get(input.getClass()).handleMessage(input);
            } catch (Exception e) {
                return new ErrorMessage(e.getMessage());
            }
        }
        else{
            return new ErrorMessage("Unsupported operation!");
        }
    }

    public abstract boolean isClosingSignal(Message input);

}
