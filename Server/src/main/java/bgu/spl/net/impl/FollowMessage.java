package bgu.spl.net.impl;

import java.util.ArrayList;
import bgu.spl.net.api.bidi.Connections;

public class FollowMessage extends Message {

    private short numOfUsers;
    private ArrayList<String> usersList = new ArrayList<>();

    public FollowMessage(short NumOfUsers, String[] list){
        numOfUsers = NumOfUsers;
        for(int i=0; i<NumOfUsers; i++){
            usersList.add(list[i]);
        }
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
        if(resourcesHolder.getUserName(connectionID) != null) {
                ArrayList<String> followSucceeded = new ArrayList<>();
                for (int i = 0; i < usersList.size(); i++) {
                    if (resourcesHolder.follow(connectionID, usersList.get(i))) {
                        followSucceeded.add(usersList.get(i));
                    }
                }
                if(followSucceeded.size() > 0) {
                    FollowAck ack = new FollowAck(((short) followSucceeded.size()), followSucceeded);
                    connections.send(connectionID, ack);
                }
                else{
                    ErrorMessage error = new ErrorMessage((short)4);
                    connections.send(connectionID, error);
                }
        }
        else{
            ErrorMessage error = new ErrorMessage((short)4);
            connections.send(connectionID, error);
        }

    }
}
