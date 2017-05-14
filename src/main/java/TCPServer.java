/**
 * Created by tim on 5/13/17.
 */

import java.io.*;
import java.net.*;
import java.sql.*;

public class TCPServer {
    public static void main(String argv[]) throws Exception{

        String clientSent;
        String capSent;

        // Create Welcoming socket at port
        ServerSocket welcomeSocket = new ServerSocket(6789);

        while(true)
        {
            // wait on welcoming Socket
            Socket connectSocket = welcomeSocket.accept();

            // Create input stream, attached to socket
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));

            // Create output stream, attached to socket
            DataOutputStream outToClient = new DataOutputStream(connectSocket.getOutputStream());

            // Read in line from Socket
            System.out.print("Receiving data from client");
            clientSent = inFromClient.readLine();

            capSent = clientSent.toUpperCase() + '\n';

            // Write out to client
            outToClient.writeBytes(capSent);

        }


    }


}
