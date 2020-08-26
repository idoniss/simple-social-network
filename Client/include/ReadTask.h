#ifndef BGSCLIENT_READTASK_H
#define BGSCLIENT_READTASK_H

#include <iostream>
#include <ConnectionHandler.h>
#include <thread>

using namespace std;
class ReadTask{
private :
ConnectionHandler* connectionHandler;
bool* loggedIn;
thread* writeThread;

public:
    ReadTask(ConnectionHandler* handler, bool* logged, thread* th2);
    void run();
};

#endif //BGSCLIENT_READTASK_H
