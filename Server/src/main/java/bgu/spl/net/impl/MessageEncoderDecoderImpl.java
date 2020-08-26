package bgu.spl.net.impl;

import bgu.spl.net.api.MessageEncoderDecoder;
import java.util.Arrays;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {

    private final byte[] opcodeBytes = new byte[2];
    private int lengthOpcodeIndex = 0;
    private byte[] messageBytes = new byte[1 << 10];
    private int lengthMessageIndex = 0;
    private short opcode;
    private int numOfZeros = -1;
    private final byte[] numOfFollowing = new byte[2];
    private int followCounter = 0;
    private boolean finishedOpcode = false;
    private boolean knowIfFollowOrUnFollow = false;
    private byte followOrUnFollow;
    private boolean flag = false;

    public Message decodeNextByte(byte nextByte) {
        if (lengthOpcodeIndex < 2) {
            opcodeBytes[lengthOpcodeIndex] = nextByte;
            lengthOpcodeIndex++;
        }
        if (lengthOpcodeIndex == 2) {
            opcode = bytesToShort(opcodeBytes);
            finishedOpcode = true;
            switch (Short.valueOf(opcode)) {
                case 1: {
                    numOfZeros = 2;
                    lengthOpcodeIndex++;
                    break;
                }
                case 2: {
                    numOfZeros = 2;
                    lengthOpcodeIndex++;
                    break;
                }
                case 3: {
                    lengthOpcodeIndex++;
                    return popString();
                }
                case 4: {
                    if(lengthOpcodeIndex == 2 && !flag) {
                        flag = true;
                        break;
                    }
                    else {
                        if (!knowIfFollowOrUnFollow) {
                            followOrUnFollow = nextByte;
                            knowIfFollowOrUnFollow = true;
                            break;
                        } else {
                            if (followCounter < 2) {
                                numOfFollowing[followCounter] = nextByte;
                                followCounter++;
                            }
                            if (followCounter == 2) {
                                numOfZeros = (int) bytesToShort(numOfFollowing);
                                followCounter++;
                                lengthOpcodeIndex++;
                            }
                            break;
                        }
                    }
                }
                case 5: {
                    numOfZeros = 1;
                    lengthOpcodeIndex++;
                    break;
                }
                case 6: {
                    numOfZeros = 2;
                    lengthOpcodeIndex++;
                    break;
                }
                case 7: {
                    return popString();
                }
                case 8: {
                    numOfZeros = 1;
                    lengthOpcodeIndex++;
                    break;
                }
            }
        }
        else{
            if (lengthOpcodeIndex > 2)
                pushByte(nextByte);
        }
         if (numOfZeros == 0) {
             return popString();
         }
         return null;
}

    private void pushByte(byte nextByte) {
        if(nextByte == '\0')
            numOfZeros--;
        if (lengthMessageIndex >= messageBytes.length) {
            messageBytes = Arrays.copyOf(messageBytes, lengthMessageIndex * 2);
        }
        messageBytes[lengthMessageIndex++] = nextByte;
    }

    public Message popString(){
        flag = false;
        lengthOpcodeIndex = 0;
        lengthMessageIndex = 0;
        numOfZeros = -1;
        followCounter = 0;
        knowIfFollowOrUnFollow = false;
        finishedOpcode = false;
        String message;
        String[] parts;
        String username;
        String password;
        switch (opcode) {
            case 1:
                message = new String(messageBytes);
                parts = message.split("\0");
                username = parts[0];
                password = parts[1];
                opcode = 0;
                messageBytes = new byte[1 << 10];
                return (new RegisterMessage(username, password));

            case 2:
                message = new String(messageBytes);
                parts = message.split("\0");
                username = parts[0];
                password = parts[1];
                messageBytes = new byte[1 << 10];
                return (new LoginMessage(username, password));

            case 3:
                messageBytes = new byte[1 << 10];
                return new LogoutMessage();

            case 4: {
                if (followOrUnFollow == '0') {
                    message = new String(Arrays.copyOfRange(messageBytes, 0, messageBytes.length));
                    parts = message.split("\0");
                    short numOfUsers = bytesToShort(numOfFollowing);
                    messageBytes = new byte[1 << 10];
                    return new FollowMessage(numOfUsers, parts);
                } else {
                    message = new String(Arrays.copyOfRange(messageBytes, 0, messageBytes.length));
                    parts = message.split("\0");
                    short numOfUsers = bytesToShort(numOfFollowing);
                    messageBytes = new byte[1 << 10];
                    return new UnFollowMessage(numOfUsers, parts);
                }
            }

            case 5:
                message = new String(messageBytes);
                parts = message.split("\0");
                messageBytes = new byte[1 << 10];
                parts[0] = parts[0].substring(5);
                return new PostMessage(parts[0]);

            case 6:
                message = new String(messageBytes);
                parts = message.split("\0");
                messageBytes = new byte[1 << 10];
                return new PrivateMessage(parts[0], parts[1]);

            case 7: {
                messageBytes = new byte[1 << 10];
                return new UserListMessage();
            }

            case 8: {
                message = new String(messageBytes);
                parts = message.split("\0");
                messageBytes = new byte[1 << 10];
                return new StatMessage(parts[0]);
            }
        }
        return null;
    }

    public short bytesToShort(byte[] byteArr){
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    public static byte[] shortToBytes(short num){
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;

    }

    public byte[] encode(Message message){
        String result = message.toString();
       return result.getBytes();
    }
}
