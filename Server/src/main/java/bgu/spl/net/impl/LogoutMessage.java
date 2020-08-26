package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public class LogoutMessage extends Message{

    public LogoutMessage(){}

    public void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){
        String userName = resourcesHolder.getUserName(connectionID);
        synchronized (resourcesHolder.isLoggedIn(userName)) {
            if (userName != null) {
                resourcesHolder.logout(connectionID, resourcesHolder.getUserName(connectionID));
                LogoutAck ack = new LogoutAck();
                connections.send(connectionID, ack);
                connections.disconnect(connectionID);

            } else {
                ErrorMessage error = new ErrorMessage((short) 3);
                connections.send(connectionID, error);
            }
        }
    }
}
