package server;

import java.io.*;
import java.util.*;

public class ListOfUsers implements Serializable
{
    Map<Integer,User> listOfUsers = null;

    ListOfUsers()
    {
        listOfUsers = new HashMap<>();
    }

    Collection<User> getUsers()
    {
        if (listOfUsers != null) return listOfUsers.values();
        else return null;
    }

    @Override
    public String toString() { return listOfUsers.toString(); }

    User getUser(Integer port) { return listOfUsers.get(port); }

    void addUser(Integer port,User user)
    {
        listOfUsers.put(port,user);
    }

    void removeUser(Integer port)
    {
        listOfUsers.remove(port);
    }

}
