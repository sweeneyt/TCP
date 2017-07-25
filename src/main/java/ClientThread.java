/**
  *  Client Connection
  *  Class called when new client joins
     sets up all input and output streams

 **/
import java.io.*;
import java.net.*;

public class ClientThread implements Runnable{

    private Socket connectSocket;
    private ServerFunctions server;

    public ClientThread(Socket connectSocket, ServerFunctions server){
        this.connectSocket = connectSocket;
        this.server = server;
    }

    @Override
    public void run() {
       try {
                // Create input and output streams, attached to socket
                ObjectInputStream inFromClient = new ObjectInputStream(connectSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(connectSocket.getOutputStream());
                connectSocket.setKeepAlive(true);

                // Read in line from Socket
                Message msgReceived;
                Message clientSent = new Message();

               do {
                   try {
                       clientSent = (Message) inFromClient.readObject();
                   } catch (ClassNotFoundException e) {System.out.println("ERROR: Reading in Message from user");}

                   try {
                       if (clientSent.getCode() == 100)
                           msgReceived = server.decipherMessage(clientSent, outToClient);
                       else
                           msgReceived = server.decipherMessage(clientSent);

                       outToClient.writeObject(msgReceived);
                       outToClient.flush();
                       outToClient.reset();
                   }catch(Exception e) {System.out.println("ERROR: error receiving message");}
               } while (true) ;

            } catch (Exception e) {
                System.out.println("Error making Connection");
            }
            try {
                connectSocket.close();
            }catch(Exception e){System.out.println("Can't close client socket");}
        }



}
