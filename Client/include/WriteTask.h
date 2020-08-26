#ifndef BGSCLIENT_WRITETASK_H
#define BGSCLIENT_WRITETASK_H

#include <iostream>
#include "ConnectionHandler.h"

using namespace std;
class WriteTask{
private :
ConnectionHandler* connectionHandler;
bool* loggedIn;
public:
    WriteTask(ConnectionHandler* handler, bool* logged);
    void run();
};

#endif //BGSCLIENT_WRITETASK_H
