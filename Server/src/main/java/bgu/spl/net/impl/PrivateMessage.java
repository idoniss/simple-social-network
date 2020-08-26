package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class PrivateMessage extends Message{

    private String userName;
    private String content;

    public PrivateMessage(String userName, String content) {
        this.userName = userName;
        this.content = content;
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
        if(resourcesHolder.getUserName(connectionID) != null){
            if(resourcesHolder.isRegistered(userName)){
                synchronized (resourcesHolder.isLoggedIn(userName)) {
                    if (resourcesHolder.isLoggedIn(userName)) {
                        PmNotification notification = new PmNotification(userName, content);
                        resourcesHolder.sendPm(userName, resourcesHolder.getUserName(connectionID), content);
                        connections.send(resourcesHolder.getConnectionID(userName), notification);
                    } else {
                        resourcesHolder.sendOfflinePm(userName, resourcesHolder.getUserName(connectionID), content);
                    }
                }
            }
            else{
                ErrorMessage error = new ErrorMessage((short)6);
                connections.send(connectionID,error);
            }
            PmAck ack = new PmAck();
            connections.send(connectionID, ack);
        }
        else{
            ErrorMessage error = new ErrorMessage((short)6);
            connections.send(connectionID,error);
        }
    }
}
