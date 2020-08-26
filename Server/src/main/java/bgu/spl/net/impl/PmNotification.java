package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class PmNotification extends Message {

    private String postingUser;
    private String content;

    public PmNotification(String postingUser, String content) {
        this.postingUser = postingUser;
        this.content = content;
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}
    public String toString(){
        String result;
        result = new String(MessageEncoderDecoderImpl.shortToBytes((short)9));
        result = result + '0' + postingUser +'\0' + content + '\0';
        return result;
    }
}
