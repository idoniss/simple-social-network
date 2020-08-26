package bgu.spl.net.impl;

import bgu.spl.net.api.bidi.Connections;

public abstract class Message {
    void process(Connections connections, int connectionID, ResourcesHolder resourcesHolder){};
}
