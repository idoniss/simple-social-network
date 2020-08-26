package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.impl.BidiMessagingProtocolImpl;
import bgu.spl.net.impl.MessageEncoderDecoderImpl;
import bgu.spl.net.impl.ResourcesHolder;
import bgu.spl.net.srv.Server;
import java.io.*;
import java.net.Socket;

public class TPCMain {
    public static void main(String[] args) throws IOException {
        ResourcesHolder data = new ResourcesHolder();
    Server.threadPerClient(Integer.parseInt(args[0]),
            () -> new BidiMessagingProtocolImpl(data),
            () -> new MessageEncoderDecoderImpl()).serve();
    }
}
