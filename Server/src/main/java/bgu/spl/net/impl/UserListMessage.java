package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import java.util.ArrayList;

public class UserListMessage extends Message {

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
        if(resourcesHolder.isLoggedIn(resourcesHolder.getUserName(connectionID))){
            ArrayList<String> registers = resourcesHolder.getRegisteredUsers();
            UserlistAck ack = new UserlistAck((short)registers.size(), registers);
            connections.send(connectionID, ack);
        }
        else{
            ErrorMessage error = new ErrorMessage((short)7);
            connections.send(connectionID, error);
        }
    }

}
