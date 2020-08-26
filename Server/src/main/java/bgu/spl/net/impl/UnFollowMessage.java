package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

import java.util.ArrayList;

public class UnFollowMessage extends Message {

    private short numOfUsers;
    private ArrayList<String> usersList = new ArrayList<>();

    public UnFollowMessage(short NumOfUsers, String[] list){
        numOfUsers = NumOfUsers;
        for(int i=0; i<NumOfUsers; i++){
            usersList.add(list[i]);
        }
    }

//    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
//        String userName = resourcesHolder.getUserName(connectionID);
//        if(resourcesHolder.isLoggedIn(userName)){
//            ArrayList<String> unfollowSucceeded = new ArrayList<>();
//            for(int i=0; i<usersList.size(); i++) {
//                if (resourcesHolder.unfollow(userName, connectionID, usersList.get(i))){
//                    unfollowSucceeded.add(usersList.get(i));
//                }
//            }
//            FollowAck ack = new FollowAck(((short)unfollowSucceeded.size()), unfollowSucceeded);
//            connections.send(connectionID, ack);
//        }
//        else{
//            ErrorMessage error = new ErrorMessage((short)4);
//            connections.send(connectionID, error);
//        }
//
//    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
        if(resourcesHolder.getUserName(connectionID) != null) {
            ArrayList<String> unfollowSucceeded = new ArrayList<>();
            for (int i = 0; i < usersList.size(); i++) {
                if (resourcesHolder.unfollow(resourcesHolder.getUserName(connectionID), usersList.get(i))) {
                    unfollowSucceeded.add(usersList.get(i));
                }
            }
            FollowAck ack = new FollowAck(((short) unfollowSucceeded.size()), unfollowSucceeded);
            connections.send(connectionID, ack);
        }
        else{
            ErrorMessage error = new ErrorMessage((short)4);
            connections.send(connectionID, error);
        }

    }
}

