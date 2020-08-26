#include "ConnectionHandler.h"
 
using boost::asio::ip::tcp;

using std::cin;
using std::cout;
using std::cerr;
using std::endl;
using std::string;
 
ConnectionHandler::ConnectionHandler(string host, short port):
host_(host), port_(port), io_service_(), socket_(io_service_){}
    
ConnectionHandler::~ConnectionHandler() {
    close();
}

//ConnectionHandler::ConnectionHandler(const ConnectionHandler& other){
//    host_ = other.host_;
//    port_ = other.port_;
//    io_service_ = other.io_service_;
//    socket_ = other.socket_;
//}
//
bool ConnectionHandler::connect() {
    std::cout << "Starting connect to " 
        << host_ << ":" << port_ << std::endl;
    try {
		tcp::endpoint endpoint(boost::asio::ip::address::from_string(host_), port_); // the server endpoint
		boost::system::error_code error;
		socket_.connect(endpoint, error);
		if (error)
			throw boost::system::system_error(error);
    }
    catch (std::exception& e) {
        std::cerr << "Connection failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getBytes(char bytes[], unsigned int bytesToRead) {
    size_t tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToRead > tmp ) {
			tmp += socket_.read_some(boost::asio::buffer(bytes+tmp, bytesToRead-tmp), error);			
        }
		if(error)
			throw boost::system::system_error(error);
    } catch (std::exception& e) {
        std::cerr << "recv failed (Error: " << e.what() << ')' << std::endl;
        return false;
    }
    return true;
}

bool ConnectionHandler::sendBytes(const char bytes[], int bytesToWrite) {
    int tmp = 0;
	boost::system::error_code error;
    try {
        while (!error && bytesToWrite > tmp ) {
			tmp += socket_.write_some(boost::asio::buffer(bytes + tmp, bytesToWrite - tmp), error);
        }
		if(error)
			throw system_error(error);
    } catch (exception& e) {
        cerr << "recv failed (Error: " << e.what() << ')' << endl;
        return false;
    }
    return true;
}
 
bool ConnectionHandler::getLine(string& line) {
    return getFrameAscii(line);
}

bool ConnectionHandler::sendLine(string& line) {
    return sendFrameAscii(line);
}
 
bool ConnectionHandler::getFrameAscii(string& frame) {
    short opcode = -1;
    string opcodeStr;
    int opcodeCounter = 0;
    int msgOpcodeCounter = 0;
    short msgOpcode = -1;
    char byte;
    stringstream answer;
    vector<char> opcodeBytes;
    vector<char> msgOpcodeBytes;
    int numOfZeros = -1;
    vector<char> textVec;
    // Stop when we encounter the null character. 
    // Notice that the null character is not appended to the frame string.
    try {
		do{
		    while(opcode == -1) {
                getBytes(&byte, 1);
                opcodeBytes.push_back(byte);
                opcodeCounter++;
                if(opcodeCounter == 2){
                    opcode = bytesToShort(opcodeBytes);
                }
            }
            switch(opcode){
		        case 9:{
		            answer << "NOTIFICATION ";
		            numOfZeros = 2;
		            char notificationType;
		            getBytes(&notificationType,1);
		            int pmOrPublic = notificationType -'0';
		            switch (pmOrPublic){
		                case 0 : {
		                    answer << ("PM ");
		                    break;
		                }
		                case 1 : {
		                    answer << ("Public ");
		                    break;
		                }
		            }
		            string postingUser;
		            while(numOfZeros == 2){
		                getBytes(&byte, 1);
                        if(byte != '\0') {
                            postingUser.append(1, byte);
                        }
                        else{
                            numOfZeros--;
		                }
		            }
		            answer << postingUser << " ";
		            string content;
		            while(numOfZeros == 1){
		                getBytes(&byte, 1);
                        if(byte != '\0') {
                            content.append(1, byte);
                        }
                        else{
                            numOfZeros--;
                        }
		            }
                    answer << (content);
		            break;
		        }
		        case 10 : {
		            answer << "ACK ";
                    while(msgOpcode == -1) {
                        getBytes(&byte, 1);
                        msgOpcodeBytes.push_back(byte);
                        msgOpcodeCounter++;
                        if(msgOpcodeCounter == 2){
                            msgOpcode = bytesToShort(msgOpcodeBytes);
                        }
                    }
                    answer << msgOpcode << " ";
                    switch(msgOpcode) {
                    case 1 : case 2 : case 3 : case 5 : case 6 :{
                        numOfZeros = 0;
                        cutLastChar(answer);
                        break;
                        }
                        case 4 : {
                            numOfZeros = 0;
                            short numOfUsers = -1;
                            int numOfUsersToShortCounter = 0;
                            vector<char> numOfUsersBytes;
                            while(numOfUsers == -1) {
                                getBytes(&byte, 1);
                                numOfUsersBytes.push_back(byte);
                                numOfUsersToShortCounter++;
                                if(numOfUsersToShortCounter == 2){
                                    numOfUsers = bytesToShort(numOfUsersBytes);
                                }
                            }
                            answer << numOfUsers << " ";
                            string name;
                            while(numOfUsers > 0){
                                getBytes(&byte, 1);
                                if(byte == '\0'){
                                    answer << name << " ";
                                    name.clear();
                                    numOfUsers--;
                                }
                                else
                                    name.append(1, byte);
                            }
                            cutLastChar(answer);
                            break;
                        }
                        case 7 : {
                            numOfZeros = 1;
                            short numOfUsers = -1;
                            int numOfUsersToShortCounter = 0;
                            vector<char> numOfUsersBytes;
                            while(numOfUsers == -1) {
                                getBytes(&byte, 1);
                                numOfUsersBytes.push_back(byte);
                                numOfUsersToShortCounter++;
                                if(numOfUsersToShortCounter == 2){
                                    numOfUsers = bytesToShort(numOfUsersBytes);
                                }
                            }
                            answer << numOfUsers << " ";
                            string name;
                            while(numOfUsers > 0){
                                getBytes(&byte, 1);
                                if(byte == '\0'){
                                    answer << name << " ";
                                    numOfUsers--;
                                }
                                else
                                    name.append(1, byte);
                            }
                            cutLastChar(answer);
                            break;
                        }
                        case 8 : {
                            numOfZeros = 1;
                            short numOfPosts = -1;
                            int numOfPostsToShortCounter = 0;
                            vector<char> numOfPostsBytes;
                            while(numOfPosts == -1) {
                                getBytes(&byte, 1);
                                numOfPostsBytes.push_back(byte);
                                numOfPostsToShortCounter++;
                                if(numOfPostsToShortCounter == 2){
                                    numOfPosts = bytesToShort(numOfPostsBytes);
                                }
                            }
                            answer << numOfPosts << " ";
                            short numOfFollowers = -1;
                            int numOfFollowersToShortCounter = 0;
                            vector<char> numOfFollowersBytes;
                            while(numOfFollowers == -1) {
                                getBytes(&byte, 1);
                                numOfFollowersBytes.push_back(byte);
                                numOfFollowersToShortCounter++;
                                if(numOfFollowersToShortCounter == 2){
                                    numOfFollowers = bytesToShort(numOfFollowersBytes);
                                }
                            }
                            answer << numOfFollowers << " ";
                            short numOfFollowings = -1;
                            int numOfFollowingsToShortCounter = 0;
                            vector<char> numOfFollowingsBytes;
                            while(numOfFollowings == -1) {
                                getBytes(&byte, 1);
                                numOfFollowingsBytes.push_back(byte);
                                numOfFollowingsToShortCounter++;
                                if(numOfFollowingsToShortCounter == 2){
                                    numOfFollowings = bytesToShort(numOfFollowingsBytes);
                                }
                            }
                            answer << numOfFollowings << " ";
                            cutLastChar(answer);
                            break;
                        }


                    }
                    break;
		        }
		        case 11 : {
		            numOfZeros = 0;
		            answer << "ERROR ";
                    while(msgOpcode == -1) {
                        getBytes(&byte, 1);
                        msgOpcodeBytes.push_back(byte);
                        msgOpcodeCounter++;
                        if(msgOpcodeCounter == 2){
                            msgOpcode = bytesToShort(msgOpcodeBytes);
                        }
                    }
                    answer << msgOpcode;
                    break;
		        }

		    }
        }
        while (numOfZeros == -1);
    } catch (exception& e) {
        cerr << "recv failed (Error: " << e.what() << ')' << endl;
        return false;
    }
    frame = answer.str();
    return true;
}

void ConnectionHandler::cutLastChar(stringstream& stream){
    string str;
    str = stream.str();
    str = str.substr(0, str.length()-1);
    stream.str(str);
}

void ConnectionHandler::fromVectorToArray(vector<char>& vec, char arr[], stringstream& toSend){
    arr[0] = vec[0];
    arr[1] = vec[1];
    toSend << arr;
}
 
bool ConnectionHandler::sendFrameAscii(const std::string& frame) {
    istringstream toSend(frame);
    string current;
    vector<string> seperatedString;
    vector<char> messageByte;
    short opcode;
    while(toSend >> current) {
        seperatedString.push_back(current);
    }
    string command = seperatedString[0];
        if(command == "REGISTER"){
        opcode = 1;
        string username = seperatedString[1];
        string password = seperatedString[2];
        shortToBytes(opcode, messageByte);
        for(unsigned int i=0; i<username.size(); i++){
            messageByte.push_back(username.at(i));
        }
        messageByte.push_back('\0');
        for(unsigned int i=0; i<password.size(); i++){
            messageByte.push_back(password.at(i));
        }
        messageByte.push_back('\0');
    }
    else if(command == "LOGIN"){
        opcode = 2;
        string username = seperatedString[1];
        string password = seperatedString[2];
        shortToBytes(opcode, messageByte);
        for(unsigned int i=0; i<username.size(); i++){
            messageByte.push_back(username.at(i));
        }
        messageByte.push_back('\0');
        for(unsigned int i=0; i<password.size(); i++){
            messageByte.push_back(password.at(i));
        }
        messageByte.push_back('\0');
    }
    else if(command == "LOGOUT"){
        opcode = 3;
        shortToBytes(opcode, messageByte);
    }
    else if(command == "FOLLOW") {
        opcode = 4;
        shortToBytes(opcode, messageByte);
        char followOrunfollow = seperatedString[1].at(0);
        messageByte.push_back(followOrunfollow);
        short numOfusers = (short) stoi(seperatedString[2]);
        vector<char> numOfUsersVec;
        shortToBytes(numOfusers, numOfUsersVec);
        messageByte.push_back(numOfUsersVec[0]);
        messageByte.push_back(numOfUsersVec[1]);
        for (unsigned short i=0; i < numOfusers; i++) {
            string toFollow = seperatedString[3+i];
            for(unsigned int j=0; j<toFollow.size(); j++){
                messageByte.push_back(toFollow.at(j));
            }
            messageByte.push_back('\0');
        }
    }
    else if(command == "POST"){
        opcode = 5;
        shortToBytes(opcode, messageByte);
        string content;
        for(unsigned int i=0; i < seperatedString.size(); i++){
            content += seperatedString[i] + " ";
        }
        content = content.substr(0, content.size()-1);
        for(unsigned int i=0; i<content.size(); i++){
            messageByte.push_back(content.at(i));
        }
        messageByte.push_back('\0');
    }
    else if(command == "PM"){
        opcode = 6;
        shortToBytes(opcode, messageByte);
        string username = seperatedString[1];
        for(unsigned int i=0; i<username.size(); i++){
            messageByte.push_back(username.at(i));
        }
        messageByte.push_back('\0');
        string content;
        for(unsigned int i=2; i< seperatedString.size(); i++){
            content += seperatedString[i] +" ";
        }
        content = content.substr(0, content.size()-1);
        for(unsigned int i=0; i<content.size(); i++){
            messageByte.push_back(content.at(i));
        }
        messageByte.push_back('\0');
    }
    else if(command == "USERLIST"){
        opcode = 7;
        shortToBytes(opcode, messageByte);
    }
    else{
        opcode = 8;
        shortToBytes(opcode, messageByte);
        string username = seperatedString[1];
        for(unsigned int i=0; i<username.size(); i++){
            messageByte.push_back(username.at(i));
        }
        messageByte.push_back('\0');
    }
    char msg[messageByte.size()];
    for(unsigned int i=0; i<messageByte.size(); i++){
        msg[i]=messageByte[i];
    }
	bool result=sendBytes(msg, messageByte.size());
    messageByte.clear();
	return result;
}
 
// Close down the connection properly.
void ConnectionHandler::close() {
    try{
        socket_.close();
    } catch (...) {
        cout << "closing failed: connection already closed" << endl;
    }
}

void ConnectionHandler::shortToBytes(short num, vector<char>& bytesVec)
{
    bytesVec.push_back((num >> 8) & 0xFF);
    bytesVec.push_back(num & 0xFF);
}

short ConnectionHandler::bytesToShort(vector<char>& bytesVec)
{
    short result = (short)((bytesVec[0] & 0xff) << 8);
    result += (short)(bytesVec[1] & 0xff);
    bytesVec.clear();
    return result;
}