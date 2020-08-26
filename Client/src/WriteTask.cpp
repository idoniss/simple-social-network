#include "WriteTask.h"
WriteTask::WriteTask(ConnectionHandler* handler, bool* logged) : connectionHandler(handler), loggedIn(logged){}

void WriteTask::run() {
    while (1) {
        string answer;
        if (!connectionHandler -> getLine(answer)) {
            break;
        }
        cout << answer << endl;
        if(answer == "ACK 2") {
            *loggedIn = true;
        }
        if (answer == "ACK 3") {
            break;
        }
    }
}