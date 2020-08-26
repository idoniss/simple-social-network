package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class ErrorMessage extends Message {


    private final short msgOpcode;

    public ErrorMessage(short msgOpcode){
        this.msgOpcode = msgOpcode;
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}

    public String toString(){
        String result = new String(MessageEncoderDecoderImpl.shortToBytes((short)11));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(msgOpcode));
        return result;
    }
}
