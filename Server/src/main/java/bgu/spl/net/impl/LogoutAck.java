package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class LogoutAck extends Message {

    private final short LogoutOpcode = 3;

    public LogoutAck(){ }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}

    public String toString(){
        String result = new String(MessageEncoderDecoderImpl.shortToBytes((short)10));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(LogoutOpcode));
        return result;
    }
}