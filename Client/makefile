CFLAGS:=-c -Wall -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -pthread

all: BGSClient
	g++ -o bin/BGSClient bin/ConnectionHandler.o bin/echoClient.o bin/ReadTask.o bin/WriteTask.o $(LDFLAGS)

BGSClient: bin/ConnectionHandler.o bin/echoClient.o bin/ReadTask.o bin/WriteTask.o
	
bin/ConnectionHandler.o: src/ConnectionHandler.cpp
	g++ $(CFLAGS) -o bin/ConnectionHandler.o src/ConnectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/ReadTask.o: src/ReadTask.cpp
	g++ $(CFLAGS) -o bin/ReadTask.o src/ReadTask.cpp

bin/WriteTask.o: src/WriteTask.cpp
	g++ $(CFLAGS) -o bin/WriteTask.o src/WriteTask.cpp

.PHONY: clean
clean:
	rm -f bin/*
