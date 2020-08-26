package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.srv.ConnectionHandler;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {

    private ConcurrentHashMap<Integer, ConnectionHandler<T>> activeClients;

    public ConnectionsImpl(){
        activeClients = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<Integer, ConnectionHandler<T>> getActiveClients() {
        return activeClients;
    }

    public boolean send(int connectionId, T msg){
        if(activeClients.containsKey(connectionId)) {
            if (activeClients.get(connectionId) != null) {
                activeClients.get(connectionId).send(msg);
                return true;
            }
        }
            return false;
        }

    public void broadcast(T msg){
        Iterator it = activeClients.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            ((ConnectionHandler)pair.getValue()).send(msg);
        }
    }

    public void disconnect(int connectionId){
        activeClients.remove(connectionId);
    }

    public void addConnection(int connectionId){
        activeClients.putIfAbsent(connectionId, null);
    }

    public void addConnection(int connectionId, ConnectionHandler<T> handler){
        activeClients.putIfAbsent(connectionId, handler);
    }
}
