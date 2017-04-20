package gadugadu;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;

import java.io.*;

public class MainStage extends Application
{
    private Stage mainWindow;

    private String userName;

    private Client client;

    // log in components
    private Scene nameScene;
    private Label nameLabel;
    private TextField nameTextField;
    private TextField portTextField;
    private Button portButton;
    private Button logInButton;
    private BorderPane logInLayout;


    // contacts componenets
    private Scene contactsScene;
    private BorderPane contactsLayout;
    private Button disconnectButton;
    private Button chatButton;
    private Button connectButton;

    // chat window
    private Stage chatWindow;
    private TextField chatTextField;
    private TextArea textArea;
    private Scene chatScene;

    @Override
    public void start(Stage primaryStage)
    {
        mainWindow = primaryStage;
        mainWindow.setOnCloseRequest(event ->
            mainWindow.close()
        );

        setLoginScene();
        tryToLogIn();
        setContactsScene();
    }


    private void setLoginScene()
    {
        logInLayout = new BorderPane();
        logInButton = new Button("Log in");
        nameLabel = new Label("Name: ");
        nameTextField = new TextField();

        logInLayout.setTop(nameLabel);
        logInLayout.setLeft(nameTextField);
        logInLayout.setBottom(logInButton);

        nameScene = new Scene(logInLayout,180,80);

        mainWindow.setTitle("Gadu-Gadu");
        mainWindow.setScene(nameScene);
        mainWindow.show();
    }

    private void tryToLogIn()
    {
        nameTextField.setOnKeyPressed(event ->
        {
            if ( event.getCode() == KeyCode.ENTER )
                try
                {
                    logIn();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
        });
        logInButton.setOnAction(event ->
        {
            try
            {
                logIn();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        });
    }

    private void logIn() throws IOException, ClassNotFoundException
    {
        userName = nameTextField.getText();
        if( userName.length() >= 4 && userName.length() <=12 ) // poprawne dane logowania
        {
            client.sendMessage(MessageType.CONNECT);
            mainWindow.setTitle("Gadu-Gadu");
            mainWindow.setScene(contactsScene);
            mainWindow.show();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Logging in error");
            alert.setHeaderText("Name incorrect");
            alert.setContentText("Name length must be between 4-12 letters.");
            alert.showAndWait();
        }
    }


    private void setContactsScene()
    {
        contactsLayout = new BorderPane();
        setDisconnectButton();
        setChatButton();
        contactsScene = new Scene(contactsLayout,230,392);
        chatButton.requestFocus();
    }

    private void setDisconnectButton()
    {
        disconnectButton = new Button("Disconnect");
        disconnectButton.setOnAction(event ->
        {
            System.out.println("User: " + userName + " disconnected.");
            mainWindow.close();
            if ( chatWindow != null )
                chatWindow.close();
        });
        contactsLayout.setBottom(disconnectButton);
    }

    private void setChatButton()
    {
        chatButton = new Button("Chat");
        chatButton.setOnAction(event ->
                {
                    makeChatWindow();
                }
        );
        contactsLayout.setCenter(chatButton);
    }

    private void makeChatWindow()
    {
        chatWindow = new Stage();

        BorderPane chatLayout = new BorderPane();
        Label chatLabel = new Label("Text: ");

        setMessageWindowActions();

        chatLayout.setBottom(chatLabel);
        chatLayout.setTop(textArea);
        chatLayout.setBottom(chatTextField);


        chatWindow.setTitle("Other Username");
        chatScene = new Scene(chatLayout,500,330);
        chatWindow.setScene(chatScene);
        chatTextField.requestFocus();
        chatWindow.show();
    }

    private void setMessageWindowActions()
    {
        textArea = new TextArea();
        textArea.setPrefSize(400,300);
        textArea.setEditable(false);

        chatTextField = new TextField();
        chatTextField.setOnKeyPressed(event ->
        {
            String message = "Me: " + chatTextField.getText()  + "\n";
            if ( event.getCode() == KeyCode.ENTER )
            {
                textArea.appendText(message);
                chatTextField.setText(null);
            }
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
