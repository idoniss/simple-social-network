package bgu.spl.net.impl;

import java.util.concurrent.*;
import java.util.*;

public class ResourcesHolder {

    private ConcurrentHashMap<String, String> registeredUsers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Boolean> loggedUsers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ArrayList<String>> whoIFollow = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ArrayList<String>> whoFollowsMe = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, String> IdAndUsername = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ArrayList<String>> postsAndPms = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> numOfPosts = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ArrayList<Pair<String, String>>> offlinePostNotifications = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, ArrayList<Pair<String, String>>> offlinePmNotifications = new ConcurrentHashMap<>();

    public ResourcesHolder(){}

    public boolean addUser(int connectionID, String user, String pass){
        if(registeredUsers.containsKey(user))
            return false;
        else {
            registeredUsers.putIfAbsent(user, pass);
            whoIFollow.put(user, new ArrayList<>());
            whoFollowsMe.put(user, new ArrayList<>());
            postsAndPms.put(user, new ArrayList<>());
            offlinePmNotifications.put(user, new ArrayList<>());
            offlinePostNotifications.put(user, new ArrayList<>());
            numOfPosts.put(user, 0);
            loggedUsers.put(user, false);
            return true;
        }
    }

    public boolean follow(int connectionID, String toFollow){
        if(isRegistered(toFollow)) {
            if (whoIFollow.get(IdAndUsername.get(connectionID)).contains(toFollow))
                return false;
            else {
                whoIFollow.get(IdAndUsername.get(connectionID)).add(toFollow);
                whoFollowsMe.get(toFollow).add(IdAndUsername.get(connectionID));
                return true;
            }
        }
        return false;
    }

    public void login(int connectionID, String user){
            IdAndUsername.putIfAbsent(connectionID, user);
            loggedUsers.replace(user, true);
    }

    public void logout(int connectionID, String user){
            IdAndUsername.remove(connectionID);
            loggedUsers.replace(user, false);
    }

    public Boolean isLoggedIn(String userName){
        if(userName == null)
            return false;
        else
            return loggedUsers.get(userName);
    }

    public boolean unfollow(String user, String toUnFollow){
        if(isRegistered(toUnFollow)) {
                if (!whoIFollow.get(user).contains(toUnFollow))
                    return false;
                else {
                    whoIFollow.get(user).remove(toUnFollow);
                    whoFollowsMe.get(toUnFollow).remove(user);
                    return true;
                }
        }
        return false;
    }

    public boolean isRegistered(String userName){
        return registeredUsers.containsKey(userName);
    }

    public String getPassword(String userName){
        return registeredUsers.get(userName);
    }

    public ArrayList<Pair<String, String>> getOfflinePostNotifications(String userName){
        return offlinePostNotifications.get(userName);
    }

    public ArrayList<Pair<String, String>> getOfflinePmNotifications(String userName){
        return offlinePmNotifications.get(userName);
    }

    public void sendOfflinePost(String toSend, String postingUser, String content){
        offlinePostNotifications.get(toSend).add(new Pair(postingUser, content));
    }

    public void sendOfflinePm(String toSend, String postingUser, String content){
        offlinePmNotifications.get(toSend).add(new Pair(postingUser, content));
    }

    public void sendPost(String postingUser, String content){
        addPost(postingUser, content);
    }

    public void addPost(String postingUser, String content) {
            postsAndPms.get(postingUser).add(content);
            int sum = numOfPosts.get(postingUser) + 1;
            numOfPosts.replace(postingUser, sum);
    }

    public void sendPm(String toSend, String postingUser, String content) {
        postsAndPms.get(postingUser).add(content);
    }

    public String getUserName(int connectionID){
        return IdAndUsername.get(connectionID);
    }

    public int getConnectionID(String user){
        for(Map.Entry<Integer, String> entry : IdAndUsername.entrySet()) {
            if(Objects.equals(user, entry.getValue()))
                return entry.getKey();
        }
        return -1;
    }

    public ArrayList<String> getFollowers(String userName){
        return whoFollowsMe.get(userName);
    }

    public ArrayList<String> getRegisteredUsers(){
        ArrayList<String> registers = new ArrayList<>();
        for (Map.Entry<String, String> entry : registeredUsers.entrySet()){
            registers.add(entry.getKey());
        }
        return registers;
    }

    public int getNumOfPost(String user){
            return numOfPosts.get(user);
    }

    public int getNumOfFollowers(String user){
        return whoFollowsMe.get(user).size();
    }

    public int getNumOfFollowing(String user){
        return whoIFollow.get(user).size();
    }
}
