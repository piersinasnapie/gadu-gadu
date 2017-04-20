package server;

import java.io.*;
import java.util.*;

public class ListOfUsers implements Serializable
{
    Map<Integer, User> listOfUsers;

   public ListOfUsers()
    {
        listOfUsers = new HashMap<>();
    }

    public String toString()
    {
        return  listOfUsers.toString();
    }

    public User getUser(Integer port) { return listOfUsers.get(port); }

    public void addUser(Integer port, User user)
    {
        listOfUsers.put(port,user);
    }

    public void removeUser(Integer port)
    {
        listOfUsers.remove(port);
    }
}
