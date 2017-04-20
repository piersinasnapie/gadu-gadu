package gadugadu;

import java.io.Serializable;

public class Message implements Serializable
{
    /**
     * port adresata
     */
    public int addresseeID;

    /**
     * port odbiorcy
     */
    public int receiverID;

    /**
     * typ enum wiadomości
     */
    public MessageType type;

    /**
     * lista użytkowników
     */
    public ListOfUsers listOfUsers = null;

    /**
     * kontekst wiadomości
     */
    String message;

    /**
     * Konstruktor inicjujący używany do podłączania oraz rozłączania użytkownika z serwerem
     * @param type typ wiadomości
     * @param listOfUsers aktualna lista użytkowników
     */
    public Message(MessageType type, ListOfUsers listOfUsers)
    {
        this.type = type;
        this.listOfUsers = listOfUsers;
    }

    /**
     * Konstruktor inicjujący używany do korespondencji między uzytkownikami
     * @param addresseeID port nadawcy
     * @param receiverID port odbiorcy
     * @param message kotekst wiadomości
     * @param type typ przesyłanej wiadomości
     */
    public Message(int addresseeID, int receiverID, String message, MessageType type)
    {
        this.addresseeID = addresseeID;
        this.receiverID = receiverID;
        this. message = message;
        this.type = type;
    }

    public void setMessage(String message) { this.message = message; }

    /**
     * Metoda zwracająca kotekst wiadomości. Może to być imię użytkownika, wiadomość do innego użytkownika
     * lub pusta wiadomość wysyłana do serwera, gdy użytkownik się rozłącza z serwerem.
     * @return kontekst wiadomości
     */
    public String getMessage() { return this.message; }

    /**
     * Metoda zwracająca kotekst wiadomości. Może to być imię użytkownika, wiadomość do innego użytkownika
     * lub pusta wiadomość wysyłana do serwera, gdy użytkownik się rozłącza z serwerem.
     * @return kontekst wiadomości
     */
    @Override
    public String toString()
    {
        return this.message;
    }
}
