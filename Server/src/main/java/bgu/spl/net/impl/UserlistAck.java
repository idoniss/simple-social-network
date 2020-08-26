package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

import java.util.ArrayList;

public class UserlistAck extends Message {

    private final short UserlistOpcode = 7;
    private short numOfUsers;
    private ArrayList<String> userNameList;
    private String usersStrings;

    public UserlistAck(short NumOfUsers, ArrayList<String> UserNameList){
        numOfUsers = NumOfUsers;
        userNameList = UserNameList;
        usersStrings = new String();
        for(int i=0; i<userNameList.size(); i++){
            usersStrings += userNameList.get(i) + '\0';
        }
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){}

    public String toString(){
        String result = new String(MessageEncoderDecoderImpl.shortToBytes((short)10));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(UserlistOpcode));
        result += new String(MessageEncoderDecoderImpl.shortToBytes(numOfUsers));
        result += usersStrings;
        return result;
    }
}