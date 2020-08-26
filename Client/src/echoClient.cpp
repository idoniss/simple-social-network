#include <stdlib.h>
#include "ConnectionHandler.h"
#include "ReadTask.h"
#include "WriteTask.h"
#include <thread>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/
int main (int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    string host = argv[1];
    short port = atoi(argv[2]);
    ConnectionHandler* connectionHandler = new ConnectionHandler(host, port);
    if (!connectionHandler -> connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    bool* loggedIn = new bool(false);
    WriteTask writeTask(connectionHandler, loggedIn);
    thread* writeThread = new thread(&WriteTask::run, &writeTask);
    ReadTask readTask(connectionHandler, loggedIn, writeThread);
    thread* readThread = new thread(&ReadTask::run, &readTask);
    readThread->join();
    delete connectionHandler;
    delete readThread;
    delete writeThread;
    delete loggedIn;
    return 0;
}
