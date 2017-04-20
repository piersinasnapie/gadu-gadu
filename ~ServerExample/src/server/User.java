package server;

import java.io.*;

public class User implements Serializable
{
    String name;

    User(String name)
    {
        this.name = name;
    }

    public String getName() { return this.name; }

    @Override
    public String toString() { return this.name; }
}
