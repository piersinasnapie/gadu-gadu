package server;

import java.io.*;

public class Main
{
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException
    {
        Client client = new Client(Server.port);
        client.waitForMessages();

        client.sendMessage(MessageType.CONNECT);
        client.sendMessage(MessageType.SEND_MESSAGE);
    }
}
