package server;

import java.io.*;

public class User implements Serializable
{
    String name;

    public User(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() { return this.name; }
}
