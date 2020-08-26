package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class PmAck extends Message {

    private final short pmOpcode = 6;

    public PmAck(){ }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}

    public String toString(){
        String result = new String(MessageEncoderDecoderImpl.shortToBytes((short)10));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(pmOpcode));
        return result;
    }
}