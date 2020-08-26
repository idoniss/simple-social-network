package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class StatMessage extends Message {

    private String userName;

    public StatMessage(String userName) {
        this.userName = userName;
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
       if(resourcesHolder.isLoggedIn(resourcesHolder.getUserName(connectionID))){
            if(resourcesHolder.isRegistered(userName)){
                StatAck ack = new StatAck((short)resourcesHolder.getNumOfPost(userName),
                        (short)resourcesHolder.getNumOfFollowers(userName),
                        (short)resourcesHolder.getNumOfFollowing(userName));
                connections.send(connectionID, ack);
            }
            else{
                ErrorMessage error = new ErrorMessage((short)8);
                connections.send(connectionID, error);
            }
       }
       else{
           ErrorMessage error = new ErrorMessage((short)8);
           connections.send(connectionID, error);
       }
    }
}
