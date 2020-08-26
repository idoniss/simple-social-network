package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

import java.util.ArrayList;

public class LoginMessage extends Message {

    private String userName;
    private String password;

    public LoginMessage(String username, String password){
        userName = username;
        this.password = password;
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
        if(resourcesHolder.isRegistered(userName)){
            synchronized (resourcesHolder.isLoggedIn(userName)) {
                if (!resourcesHolder.isLoggedIn(userName)) {
                    if (resourcesHolder.getPassword(userName).equals(password)) {
                        resourcesHolder.login(connectionID, userName);
                        LoginAck ack = new LoginAck();
                        connections.send(connectionID, ack);
                        String userName = resourcesHolder.getUserName(connectionID);
                        ArrayList<Pair<String, String>> offlinePosts =
                                resourcesHolder.getOfflinePostNotifications(userName);
                        ArrayList<Pair<String, String>> offlinePms =
                                resourcesHolder.getOfflinePmNotifications(userName);
                        for (int i = 0; i < offlinePosts.size(); i++) {
                            PublicNotification notification = new PublicNotification(offlinePosts.get(i).getFirst(),
                                    offlinePosts.get(i).getSecond());
                            connections.send(connectionID, notification);
                        }
                        for (int i = 0; i < offlinePms.size(); i++) {
                            PmNotification notification = new PmNotification(offlinePms.get(i).getFirst(),
                                    offlinePms.get(i).getSecond());
                            connections.send(connectionID, notification);
                        }
                    } else {
                        ErrorMessage error = new ErrorMessage((short) 2);
                        connections.send(connectionID, error);
                    }
                } else {
                    ErrorMessage error = new ErrorMessage((short) 2);
                    connections.send(connectionID, error);
                }
            }
        }
        else{
            ErrorMessage error = new ErrorMessage((short)2);
            connections.send(connectionID, error);
        }
    }
}
