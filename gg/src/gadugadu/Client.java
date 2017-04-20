package gadugadu;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client
{
    private Socket mySocket;
    private String userName;
    private String messageLine;
    int port;
    ObjectOutputStream output;

    ListOfUsers listOfUsers = null;

    Client(int port) throws IOException
    {
        this.port = port;
        mySocket = new Socket("localhost",port);
    }

    Client(String hostName, int port) throws IOException
    {
        this.port = port;
        mySocket = new Socket(hostName, port);
    }

    void sendMessage(MessageType messageType) throws IOException, ClassNotFoundException
    {
        switch (messageType)
        {
            case CONNECT:
                userName = nameTextField.getText();
                System.out.println("Client: " + mySocket.getLocalPort() + " , " + userName +" - Połączyłem się do servera.");
                output = new ObjectOutputStream(mySocket.getOutputStream());
                Message message = new Message(mySocket.getPort(), 0, userName, messageType);
                output.writeObject(message);
                break;

            case SEND_MESSAGE:
                messageLine = messageTextField.getText();

                output = new ObjectOutputStream(mySocket.getOutputStream());

                Integer receiverPort = null;
                for(Integer i : listOfUsers.listOfUsers.keySet())
                {
                    String currentName = listOfUsers.getUser(i).toString();
                    if(currentName.equals(chosenName))
                    {
                        receiverPort = i;
                        break;
                    }
                }
                message = new Message(mySocket.getLocalPort(), receiverPort , messageLine, messageType);
                output.writeObject(message);
                messageTextArea.append("Me: " + messageLine + '\n');
                break;

            case DISCONNECT:
                messageLine = null;
                output = new ObjectOutputStream(mySocket.getOutputStream());
                message = new Message(mySocket.getPort(), 6758, messageLine, messageType);
                output.writeObject(message);
                System.out.println("Rozłączyłem się");

                break;
        }
    }

    void waitForMessages()
    {
        Thread reading = new Thread(() ->
        {
            boolean isWorking = true;
            try
            {
                while (isWorking)
                {
                    if (!mySocket.isClosed())
                    {
                        ObjectInputStream inputStream = new ObjectInputStream(mySocket.getInputStream());
                        Message msg = (Message) inputStream.readObject();

                        switch (msg.type)
                        {
                            case CONNECT:
                                listOfUsers = msg.listOfUsers;
                                System.out.println("Lista użytkowników: ");
                                if (listModel != null)
                                {
                                    listModel.removeAllElements();
                                }
                                for (User u : listOfUsers.listOfUsers.values())
                                {
                                    listModel.addElement(u.toString());
                                    System.out.println(u.toString());
                                }
                                break;

                            case SEND_MESSAGE:
                                if(messageTextArea!=null)
                                {
                                    messageTextArea.append(listOfUsers.getUser(msg.addresseeID).name + ": " + msg.toString() + '\n');
                                }
                                break;

                            case DISCONNECT:
                                listOfUsers = msg.listOfUsers;
                                System.out.println("Lista użytkowników: ");
                                if (listModel != null)
                                {
                                    listModel.removeAllElements();
                                }
                                for (User u : listOfUsers.listOfUsers.values())
                                {
                                    listModel.addElement(u.toString());
                                    System.out.println(u.toString());
                                }
                                break;
                        }
                    }
                    else break;
                }
            }
            catch (ClassNotFoundException e) { e.printStackTrace(); }
            catch (IOException e) { isWorking = false; }
        });
        reading.start();
    }

    // Window components

    JTextField nameTextField;
    JFrame frame;
    DefaultListModel<String> listModel;
    JList<String> userList = null;
    JTextField messageTextField;
    JTextArea messageTextArea;
    String chosenName;

    void setGUI()
    {
        frame = new JFrame();
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Gadu-Gadu");
        setLoginPanel();
    }

    void setLoginPanel()
    {
        JPanel loginPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel();
        JButton connectButton = new JButton("connect");
        nameTextField = new JTextField();
        nameTextField.setRequestFocusEnabled(true);
        nameTextField.addKeyListener(new KeyAdapter()
        {
            /**
             * Invoked when a key has been pressed.
             *
             * @param e
             */
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    userName = nameTextField.getText();
                    try
                    {
                        sendMessage(MessageType.CONNECT);
                    }
                    catch (IOException | ClassNotFoundException e1) { e1.printStackTrace(); }
                    setContactsPanel();
                }
                super.keyPressed(e);
                return;
            }
        });
        loginPanel.setPreferredSize(new Dimension(180,50));

        connectButton.addActionListener(e ->
        {
            userName = nameTextField.getText();
            try
            {
                sendMessage(MessageType.CONNECT);
            }
            catch (IOException | ClassNotFoundException e1) { e1.printStackTrace(); }
            setContactsPanel();
            return;
        });

        loginPanel.add(nameLabel, BorderLayout.WEST);
        loginPanel.add(nameTextField, BorderLayout.CENTER);
        loginPanel.add(connectButton, BorderLayout.SOUTH);
        frame.add(loginPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void setContactsPanel()
    {
        listModel = new DefaultListModel<>();
        userList = new JList<>(listModel);
        userList.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if(e.getClickCount() == 2)
                {
                    openChatWindow();
                }
                super.mouseClicked(e);
            }
        });

        JPanel loginPanel = new JPanel(new BorderLayout());
        loginPanel.setPreferredSize(new Dimension(230,380));
        loginPanel.add(userList, BorderLayout.CENTER);
        frame.setContentPane(loginPanel);
        frame.setTitle("User: " + userName);
        frame.addWindowListener(new WindowAdapter()
        {
            /**
             * Invoked when a window is in the process of being closed.
             * The close operation can be overridden at this point.
             *
             * @param e
             */
            @Override
            public void windowClosing(WindowEvent e)
            {
                try
                {
                    sendMessage(MessageType.DISCONNECT);
                    mySocket.close();
                }
                catch (IOException | ClassNotFoundException e1) { e1.printStackTrace(); }
                super.windowClosing(e);
            }
        });
        frame.revalidate();
        frame.pack();
    }

    private void openChatWindow()
    {
        JFrame chatFrame = new JFrame();
        chosenName = userList.getSelectedValue();
        chatFrame.setResizable(false);
        chatFrame.setTitle(chosenName);
        chatFrame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                chatFrame.dispose();
                super.windowClosing(e);
            }
        });

        messageTextField = new JTextField();
        messageTextField.setRequestFocusEnabled(true);
        messageTextField.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                {
                    try
                    {
                        sendMessage(MessageType.SEND_MESSAGE);
                        messageTextField.setText(null);
                    }
                    catch (IOException | ClassNotFoundException e1) { e1.printStackTrace(); }
                }
                super.keyPressed(e);
            }
        });
        messageTextArea = new JTextArea();
        messageTextArea.setPreferredSize(new Dimension(400,300));
        messageTextArea.setEditable(false);

        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setPreferredSize(new Dimension(500,330));
        messagePanel.add(messageTextArea, BorderLayout.NORTH);
        messagePanel.add(messageTextField, BorderLayout.SOUTH);

        chatFrame.add(messagePanel);
        chatFrame.setVisible(true);
        chatFrame.pack();
    }

    public static void main(String[] args) throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String ip = br.readLine();

        Client client = new Client(ip,6758);
        client.waitForMessages();
        client.setGUI();
    }
}

