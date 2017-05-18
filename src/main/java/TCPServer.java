/**
 * Created by tim on 5/13/17.
 */

import javax.xml.crypto.Data;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.ArrayList;

public class TCPServer {

    private ArrayList rooms;
    private ArrayList userNames;
    private BufferedReader inFromClient ;
    private BufferedReader inFromClient2 ;
    private DataOutputStream outToClient;


    public TCPServer(int port) {

        String clientSent;
        int clientSentMenuChoice;
        this.rooms = new ArrayList();
        this.userNames = new ArrayList();

        try {
            start(port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(int port) throws Exception {


        while (true) {
            // wait on welcoming Socket
            Socket connectSocket = null;
            ServerSocket welcomeSocket = new ServerSocket(port);
            connectSocket = welcomeSocket.accept();


            // Create input stream, attached to socket
            this.inFromClient = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));
            //this.inFromClient2 = new BufferedReader(new InputStreamReader(connectSocket.getInputStream()));


            // Create output stream, attached to socket
            this.outToClient = new DataOutputStream(connectSocket.getOutputStream());


            // Read in line from Socket

            int clientSentMenuChoice = inFromClient.read();
            outToClient.writeBytes("You sent a " + clientSentMenuChoice);
            //String clientSentString = inFromClient.readLine();
            //String clientSentString = "blank";
            //System.out.print("Receiving data from client " + clientSentString);
            routeChoice(clientSentMenuChoice, "blank", outToClient);

            // clientSentMenuChoice = clientSent.toUpperCase() + '\n';


        }
    }


        public void routeChoice(int menuChoice, String clientString, DataOutputStream out) throws Exception{
            switch (menuChoice){
                case 1:
                    listRooms(out);
                    break;
                case 2:
                    addRoom(out, clientString);
                    break;
                case 3:
                    user(out);
                    break;
            }
        }

        public void listRooms(DataOutputStream out) throws Exception {
            if(rooms.isEmpty()) {
                out.writeBytes("No Rooms Available");
            }
            else{
                 for(int i = 0; i < rooms.size(); i++){
                     System.out.print(rooms.get(i));
                 }
            }
            return;
        }

        public void addRoom(DataOutputStream out, String roomToAdd) throws Exception {
            if(rooms.contains(roomToAdd)) {
                out.writeBytes("Room already exists");
                return;
            }
            else{
                rooms.add(roomToAdd);
                out.writeBytes("Room added!");
            }
        }

        public void user(DataOutputStream out) throws Exception{
            out.writeBytes("You are a user");
            return;
        }

        public static void main(String argv[]) throws Exception{
            new TCPServer(6789);
        }
}
