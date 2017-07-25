/**
*    SERVER CONNECTION:
*    makes the server
*    when a new client joins, makes a new client thread
**/

import java.net.*;

public class TCPServer {

    public static void main(String argv[]) throws Exception {

        // gets instance of the Server's Functions
        ServerFunctions serverHelp = new ServerFunctions();
        int port = 1234;
        Socket connectSocket;
        ServerSocket welcomeSocket = new ServerSocket(port);

        while (true) {
            // wait on welcoming Socket
            connectSocket = welcomeSocket.accept();

            // makes new client thread when a new user joins
            ClientThread newClient = new ClientThread(connectSocket, serverHelp);
            Thread newThread = new Thread(newClient);
            System.out.println("New User");
            newThread.start();
        }
    }
}