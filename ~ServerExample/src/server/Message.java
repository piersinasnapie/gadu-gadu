package server;

import java.io.*;

public class Message implements Serializable
{
    int addresseeID;
    int receiverID;
    MessageType type;
    ListOfUsers listOfUsers = null;

    String message;

    Message(MessageType type, ListOfUsers listOfUsers)
    {
        this.type = type;
        this.listOfUsers = listOfUsers;
    }

    Message(int addresseeID, int receiverID, String message, MessageType type)
    {
        this.addresseeID = addresseeID;
        this.receiverID = receiverID;
        this. message = message;
        this.type = type;
    }

    String getMessage() { return this.message; }

    @Override
    public String toString()
    {
        return this.message;
    }
}
