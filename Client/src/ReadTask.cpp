#include "ReadTask.h"

ReadTask::ReadTask(ConnectionHandler* handler, bool* logged, thread* write) :
connectionHandler(handler), loggedIn(logged), writeThread(write){}

void ReadTask::run() {
    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        cin.getline(buf, bufsize);
        string line(buf);
        if (!connectionHandler -> sendLine(line)) {
            break;
        }
        if(line == "LOGOUT" && *loggedIn) {
            writeThread -> join();
            break;
        }
    }
}