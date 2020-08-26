package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class PostAck extends Message {

    private final short postOpcode = 5;

    public PostAck(){}

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}

    public String toString(){
        String result = new String(MessageEncoderDecoderImpl.shortToBytes((short)10));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(postOpcode));
        return result;
    }
}