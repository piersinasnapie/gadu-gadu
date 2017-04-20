package gadugadu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ListOfUsers implements Serializable
{
    /**
     * Mapa przechowująca port oraz nazwę użytkownika
     */
    Map<Integer,User> listOfUsers;

    /**
     * Konstruktor domyślny, tworzy mapę użytkowników
     */
    public ListOfUsers()
    {
        listOfUsers = new HashMap<>();
    }

    /**
     * Metoda wypisująca listę wszystkich użytkowników w postaci {int,String}
     * @return lista użytkowników
     */
    public String toString()
    {
        return  listOfUsers.toString();
    }

    /**
     * Metoda zwracająca obiekt użytkownika(wartość) z mapy, za podaniem odpowiedniego portu(klucza)
     * @param port klucz w mapie haszującej
     * @return użytkownik przypisany pod kluczem
     */
    public User getUser(Integer port) { return listOfUsers.get(port); }

    /**
     * Dodaje użytkownika o podanym porcie
     * @param port port użytkownika/gniazda
     * @param user użytkownik
     */
    public void addUser(Integer port,User user)
    {
        listOfUsers.put(port,user);
    }

    /**
     * Usuwa użytkownika po podaniu  portu(klucza)
     * @param port klucz w mapie haszującej
     */
    public void removeUser(Integer port)
    {
        listOfUsers.remove(port);
    }
}
