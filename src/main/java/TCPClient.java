import java.io.*;
import java.net.*;
import java.util.logging.SocketHandler;

/**
 * Created by tim on 5/13/17.
 */


public class TCPClient {


    public static void main(String argv[]) throws Exception
    {
        String sent;
        String mSent;
        InetAddress host = InetAddress.getLocalHost();

        // Create input stream
        BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));

        // Create client, socket.  Connect to Server
        Socket clientSocket = new Socket (host, 6789);

        // Create output stream, attach to Socket
        DataOutputStream toServer = new DataOutputStream(clientSocket.getOutputStream());

        // Create input stream attached to socket
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String username = getUsername();
        // Send query to Server to see if Username is available
        int menuChoice;
        do{
            menuChoice = showMenu();
            switch(menuChoice){
                case 1:
                    toServer.writeByte(menuChoice);
                    toServer.writeBytes("blank");
                    break;
                case 2:
                    //toServer.writeByte(menuChoice);
                    //toServer.writeBytes(getRoomToAdd());

            }
             mSent = inFromServer.readLine();
            System.out.println("received from server");
            System.out.println("From Server:" +mSent);
        }while(menuChoice != 5);

        // Send user input to Server
        toServer.writeBytes(username +'\n');

        // Read line from Server
        mSent = inFromServer.readLine();

        System.out.println("From Server:" +mSent);

        clientSocket.close();
    }

    public static String getUsername() throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Username: ");
        String user = in.readLine();
        return user;
    }

    public static int showMenu() throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1. List Available Rooms");
        System.out.println("2. Create a Room");
        System.out.println("3. List Rooms currently in");
        System.out.println("4. Leave Room");
        System.out.println("5. Leave Server");
        String menuChoiceString = in.readLine();
        int menuChoice = Integer.parseInt(menuChoiceString);
        return menuChoice;
    }

    public static String getRoomToAdd() throws Exception{
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter Room to Add: ");
        String roomTodAdd = in.readLine();
        return roomTodAdd;
    }
}
