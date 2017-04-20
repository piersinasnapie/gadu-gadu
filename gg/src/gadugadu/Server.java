package gadugadu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class Server extends Thread
{
    private static Server mainServer = null;

    private final int maxConnections = 50;
    public final static int port = 6758;
    public static String ipAdress;

    ServerSocket serverSocket;

    ListOfUsers listOfUsers;
    Map<Integer,Socket> sockets;
    Map<Integer,String> userMessages;

    private Server(int port) throws IOException
    {
        serverSocket = new ServerSocket(port, maxConnections);
        listOfUsers = new ListOfUsers();
        sockets = new HashMap<>();
        userMessages = new HashMap<>();
    }

    static Server getServer() throws IOException
    {
        if ( mainServer == null ) mainServer = new Server(port);
        return mainServer;
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println("server.Server running.");
            System.out.println(InetAddress.getLocalHost().getCanonicalHostName());
        }
        catch (UnknownHostException e) { e.printStackTrace(); }
        try
        {
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                sockets.put(clientSocket.getPort(),clientSocket);
                serviceConnection(clientSocket);
            }
        }
        catch (ClassNotFoundException | IOException e) { e.printStackTrace(); }
    }

    void serviceConnection(Socket socket) throws ClassNotFoundException
    {
        Thread servicing = new Thread(() ->
        {
            System.out.println("Servicing client: " + socket.getPort());
            boolean isFinished = false;
            ObjectInputStream inputStream;
            ObjectOutputStream outputStream;
            try
            {
                while (!isFinished)
                {
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    Message msg = null;

                    msg = (Message) inputStream.readObject();

                    switch (msg.type)
                    {
                        case CONNECT:
                            User newUser = new User(msg.getMessage());
                            listOfUsers.addUser(socket.getPort(), newUser);
                            if(!sockets.isEmpty())
                            {
                                for (Socket s : sockets.values())
                                {
                                    outputStream = new ObjectOutputStream(s.getOutputStream());
                                    Message response = new Message(MessageType.CONNECT, listOfUsers);
                                    outputStream.writeObject(response);
                                }
                                System.out.println("++++++ server.Server połączył się z: " + socket.getPort() + " ++++++");
                                System.out.println(listOfUsers.toString());
                            }
                            break;

                        case DISCONNECT:
                            listOfUsers.removeUser(socket.getPort());
                            if(!sockets.isEmpty())
                            {
                                for (Socket s : sockets.values())
                                {
                                    outputStream = new ObjectOutputStream(s.getOutputStream());
                                    Message response = new Message(MessageType.DISCONNECT, listOfUsers);
                                    outputStream.writeObject(response);
                                }
                                System.out.println("†††††† server.Server rozłączył się z: " + socket.getPort() + " ††††††");
                            }
                            isFinished = true;
                            break;

                        case SEND_MESSAGE:
                            System.out.print("server.Server odebrał od: " + socket.getPort() + " wiadomość: " + msg.toString() + '\n');
                            userMessages.put(msg.receiverID, msg.getMessage());
                            msg.setMessage(userMessages.get(msg.receiverID).toString());
                            outputStream = new ObjectOutputStream(sockets.get(msg.receiverID).getOutputStream());
                            outputStream.writeObject(msg);

                            System.out.print("server.Server wysłał do: " + sockets.get(msg.receiverID).getPort() + " wiadomość: " + msg.toString() + '\n');
                            break;
                    }
                }
            }
            catch (IOException | ClassNotFoundException e) { e.printStackTrace(); }
        });
        servicing.start();
    }

    public static void main(String[] args) throws IOException
    {
        Server.getServer().start();
    }
}
