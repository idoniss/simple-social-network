package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

import java.util.ArrayList;

public class FollowAck extends Message {

    private final short followOpcode = 4;
    private short numOfUsers;
    private ArrayList<String> userNameList;
    private String usersStrings;

    public FollowAck(short NumOfUsers, ArrayList<String> UserNameList){
        numOfUsers = NumOfUsers;
        userNameList = UserNameList;
        usersStrings = new String();
        for(int i=0; i<userNameList.size(); i++){
            usersStrings += userNameList.get(i) + '\0';
        }
    }

    public void process(Connections connections, int connectionId, ResourcesHolder resourcesHolder){}

    public String toString(){
        String result = new String(MessageEncoderDecoderImpl.shortToBytes((short)10));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(followOpcode));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(numOfUsers));
        result += usersStrings;
        return result;
    }
}