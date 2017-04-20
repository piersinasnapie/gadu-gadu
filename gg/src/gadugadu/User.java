package gadugadu;

import java.io.Serializable;

public class User implements Serializable
{
    /**
     * Nazwa użytkownika
     */
    String name;

    /**
     * Konstruktor inicjujący użytkownika imieniem
     * @param name imię użytkownika
     */
    public User(String name)
    {
        this.name = name;
    }

    /**
     * Metoda zwracająca imię obiektu klasu User
     * @return imię użytkownika
     */
    @Override
    public String toString() { return this.name; }
}
