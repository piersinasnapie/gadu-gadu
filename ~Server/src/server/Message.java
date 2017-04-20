package server;

import java.io.*;

public class Message implements Serializable
{
    public int addresseeID;
    public int receiverID;
    public MessageType type;
    public ListOfUsers listOfUsers = null;

    String message;

    public Message(MessageType type, ListOfUsers listOfUsers)
    {
        this.type = type;
        this.listOfUsers = listOfUsers;
    }

    public Message(int addresseeID, int receiverID, String message, MessageType type)
    {
        this.addresseeID = addresseeID;
        this.receiverID = receiverID;
        this. message = message;
        this.type = type;
    }

    public String getMessage() { return this.message; }

    @Override
    public String toString()
    {
        return this.message;
    }
}
