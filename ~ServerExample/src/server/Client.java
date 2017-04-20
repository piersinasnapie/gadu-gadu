package server;

import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client
{
    JFrame frame;

    private Socket mySocket;
    private String hostName = "localhost";
    private int port;

    ListOfUsers listOfUsers = null;
    DefaultListModel<String> listModel;

    String userName;
    String messageLine;

    Client(int port) throws IOException
    {
        this.port = port;
        mySocket = new Socket(hostName, port);
    }

    void sendMessage(MessageType messageType) throws IOException, ClassNotFoundException
    {
        switch (messageType)
        {
            case CONNECT:
                System.out.println("User: " + mySocket.getLocalPort() + " , " + userName + " - connected to the server.");
                ObjectOutputStream output = new ObjectOutputStream(mySocket.getOutputStream());
                Message message = new Message(mySocket.getPort(), 0, userName, messageType);
                output.writeObject(message);
                break;

            case SEND_MESSAGE:
                while (true)
                {
                    String[] array = messageLine.split(",");

                    System.out.println(listOfUsers.getUser(mySocket.getLocalPort()).name + ": " + array[0]);
                    output = new ObjectOutputStream(mySocket.getOutputStream());
                    message = new Message(mySocket.getLocalPort(), Integer.parseInt(array[1]), array[0], messageType);
                    output.writeObject(message);
                }

            case DISCONNECT:
                messageLine = null;
                output = new ObjectOutputStream(mySocket.getOutputStream());
                message = new Message(mySocket.getPort(), 0, messageLine, messageType);
                output.writeObject(message);
                System.out.println("Disconnected from server.");
                break;
        }
    }

    void waitForMessages()
    {
        Thread reading = new Thread(() ->
        {
            try
            {
                while (true)
                {
                    ObjectInputStream inputStream = new ObjectInputStream(mySocket.getInputStream());

                    Message msg = (Message) inputStream.readObject();

                    switch (msg.type)
                    {
                        case CONNECT:
                            userName = msg.getMessage();
                            listOfUsers = msg.listOfUsers;
                            System.out.println("Lista użytkowników: ");
                            for (User u : listOfUsers.getUsers())
                            {
                                listModel.addElement( u.getName() );
                            }
                            break;

                        case SEND_MESSAGE:
                            System.out.println(listOfUsers.getUser(msg.addresseeID).name + ": " + msg.toString());
                            break;

//                        case DISCONNECT:
//                            MainStage.activeUserList.getItems().remove( userName );
////                            mySocket.close();
//                            return;
                    }
                }
            }
            catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
        });
        reading.setDaemon(true);
        reading.start();
    }
}


