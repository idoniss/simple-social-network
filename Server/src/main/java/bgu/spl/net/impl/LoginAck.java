package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class LoginAck extends Message {

    private final short loginOpcode = 2;

    public LoginAck(){ }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}

    public String toString(){
        String result = new String(MessageEncoderDecoderImpl.shortToBytes((short)10));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(loginOpcode));
        return result;
    }
}