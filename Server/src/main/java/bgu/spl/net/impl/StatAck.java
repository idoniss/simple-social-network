package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class StatAck extends Message {

    private final short pmOpcode = 8;
    private short numPosts;
    private short numFollowers;
    private short numFollowing;

    public StatAck(short NumOfPost, short NumOfFollowers, short NumOfFollowing){
        numPosts = NumOfPost;
        numFollowers = NumOfFollowers;
        numFollowing = NumOfFollowing;
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}

    public String toString(){
        String result = new String(MessageEncoderDecoderImpl.shortToBytes((short)10));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(pmOpcode));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(numPosts));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(numFollowers));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(numFollowing));
        return result;
    }
}