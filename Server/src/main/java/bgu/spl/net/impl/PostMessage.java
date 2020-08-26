package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

import java.util.ArrayList;

public class PostMessage extends Message{

    private String content;
    private ArrayList<String> hashtags = new ArrayList<>();

    public PostMessage(String content) {
        this.content = content;
        for(int i=0; i<content.length(); i++){
            if(content.charAt(i)=='@'){
                String user;
                for(int j=i+1; j<=content.length(); j++){
                    if(j == content.length() || content.charAt(j)==' '){
                        user = content.substring(i+1, j);
                        if(!hashtags.contains(user)) {
                            hashtags.add(user);
                        }
                        break;
                    }
                }
            }
        }
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
        if(resourcesHolder.getUserName(connectionID) != null) {
            String userName = resourcesHolder.getUserName(connectionID);
            resourcesHolder.sendPost(userName, content);
            for (int i = 0; i < hashtags.size(); i++) {
                PublicNotification notification =
                        new PublicNotification(userName, content);
                synchronized (resourcesHolder.isLoggedIn(hashtags.get(i))) {
                    if (resourcesHolder.isLoggedIn(hashtags.get(i))) {
                        connections.send(resourcesHolder.getConnectionID(hashtags.get(i)), notification);
                    } else {
                        resourcesHolder.sendOfflinePost(hashtags.get(i), userName, content);
                    }
                }
            }
            ArrayList<String> followers = resourcesHolder.getFollowers(userName);
            for (int i = 0; i < followers.size(); i++) {
                if (!hashtags.contains(followers.get(i))) {
                    PublicNotification notification =
                            new PublicNotification(resourcesHolder.getUserName(connectionID), content);
                    if (resourcesHolder.isLoggedIn(followers.get(i))) {
                        connections.send(resourcesHolder.getConnectionID(followers.get(i)), notification);
                    } else
                        resourcesHolder.sendOfflinePost(followers.get(i), userName, content);
                }
            }
            PostAck ack = new PostAck();
            connections.send(connectionID, ack);
        }
        else{
            ErrorMessage error = new ErrorMessage((short)5);
            connections.send(connectionID,error);
        }
    }
}
