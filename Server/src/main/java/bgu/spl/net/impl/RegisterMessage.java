package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class RegisterMessage extends Message {

    private String userName;
    private String password;

    public RegisterMessage(String username, String pass){
        userName = username;
        password = pass;
    }

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
        if(resourcesHolder.addUser(connectionID, userName, password)){
            RegisterAck ack = new RegisterAck();
            connections.send(connectionID, ack);
        }
        else{
            ErrorMessage error = new ErrorMessage((short)1);
            connections.send(connectionID, error);
        }


    }
}

