package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {

    private Connections<Message> connections = new ConnectionsImpl<>();
    private int connectionID;
    private boolean shouldTerminate = false;
    private ResourcesHolder data;

    public BidiMessagingProtocolImpl(ResourcesHolder data){
        this.data = data;
    }

    public void start(int connectionId, Connections<Message> connections){
       this.connections = connections;
       connectionID = connectionId;
    }

    public void process(Message message){
        message.process(connections, connectionID, data);
    }

    public boolean shouldTerminate(){
        return shouldTerminate;
    }
}
