package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class RegisterAck extends Message {

    private final short registerOpcode = 1;
    private final short ackOp;

    public RegisterAck(){
        ackOp = 10;
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}

    public String toString(){
        String ackOpcode = new String(MessageEncoderDecoderImpl.shortToBytes(ackOp));
        String msgOpcode= new String(MessageEncoderDecoderImpl.shortToBytes(registerOpcode));
        return ackOpcode+msgOpcode;
    }
}