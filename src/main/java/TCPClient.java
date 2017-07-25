/**
*    Client Functions
*    Class holds all the functionality of the client
*    Gets all the input from the user, makes a message packet, and sends it to the server.
**/


import java.io.*;
import java.net.*;

public class TCPClient {

    private BufferedReader clientIn;
    private ObjectOutputStream toServer;
    private ObjectInputStream inFromServer;
    private String user;

    private TCPClient(){
        clientIn = new BufferedReader(new InputStreamReader(System.in));
    }

    // Function: Starts Client Thread, opens input/output streams, incoming client thread and gets username
    // In: INT - port to connect to
    // Out: None
    private void start(int port) throws Exception {
      try {
          InetAddress host = InetAddress.getLocalHost();
          // Create client, socket.  Connect to Server
          Socket clientSocket = new Socket(host, 1234);

          // Create output stream, attach to Socket
          toServer = new ObjectOutputStream(clientSocket.getOutputStream());
          inFromServer = new ObjectInputStream(clientSocket.getInputStream());

          getUniqueUsername();
          inClientThread display = new inClientThread(inFromServer);
          Thread thread = new Thread(display);
          thread.start();
          menu();
      }catch (Exception e) {System.out.println("ERROR: Can't get Client");}
    }

    // Function: Gets a unique username, Calls getUserName() until unique username is retrieved
    // In: None
    // Out: None
    private void getUniqueUsername() throws Exception {
        try {
            int returnedCode;
            do {
                returnedCode = getUsername();
            } while (returnedCode != 100);
        }catch(Exception e) { System.out.println("ERROR: Can't get username1");}

    }

    // Function: Gets username, queries user for a username, messages server with username to check its userList.
    // In: None
    // Out: INT - return code for username, 100 for a successful username, -100 if not
    private int getUsername() throws Exception {
        int returnedCode = 0;
        try {
            System.out.println("Username: ");
            String readFromClient = clientIn.readLine();
            int code = convertType("username", readFromClient);
            Message msgToServer = new Message(code, readFromClient);
            returnedCode = sendUserNameMessage(msgToServer);
        }catch (Exception e) { System.out.println("ERROR: Can't get username2");}
        return returnedCode;

    }

    // Function: Gets room to add to roomList from client.  calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void getRoomToAdd() throws Exception {
        try {
            System.out.println("Enter Room to Add: ");
            String readIn = clientIn.readLine();
            Message msgToServerAddRoom = makeMessage("addRoom", readIn);
            sendMessage(msgToServerAddRoom);
            Message msgToServerJoinRoom = makeMessage("joinRoom", readIn, user);
            sendMessage(msgToServerJoinRoom);
        }catch (Exception e) { System.out.println("ERROR: Can't get room to add");}
    }

    // Function: List all rooms in the Server, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void listAllRooms() throws Exception {
        try {
            Message msgToServer = makeMessage("listAllRooms");
            sendMessage(msgToServer);
        }catch (Exception e) {System.out.println("ERROR: Can't list rooms");}
    }

    // Function: List all users in the Server, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void listAllUsers() throws Exception {
        try {
            Message msgToServer = makeMessage("listAllUsers");
            sendMessage(msgToServer);
        }catch(Exception e){System.out.println("ERROR: Can't list users");}
    }

    // Function: List all room current user belongs to, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void listUsersRooms() throws Exception {
        try {
            Message msgToServer = makeMessage("listUsersRooms", null, user);
            System.out.println("Rooms " + user + " is in:");
            sendMessage(msgToServer);
        }catch (Exception e){System.out.println("ERROR: Can't list user's rooms");}
    }

    // Function: List all users in a room, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void listUsersInARooms() throws Exception {
        try {
            System.out.println("Enter Room to list users: ");
            String readIn = clientIn.readLine();
            Message msgToServer = makeMessage("listUsersinaRoom", readIn);
            sendMessage(msgToServer);
        }catch (Exception e){System.out.println("ERROR: Can't list users in a room");}
    }

    // Function: Gets room user wishes to join, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void joinRoom() throws Exception {
        try {
            System.out.println("Enter Room to Join: ");
            String readIn = clientIn.readLine();
            Message msgToServer = makeMessage("joinRoom", readIn, user);
            sendMessage(msgToServer);
          }catch(Exception e){ System.out.println("ERROR: Can't join room");}
    }

    // Function: Gets room user wishes to leave, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void leaveRoom() throws Exception{
        try {
            System.out.println("Enter Room to Leave: ");
            String readIn = clientIn.readLine();
            Message msgToServer = makeMessage("leaveRoom", readIn, user);
            sendMessage(msgToServer);
        }catch(Exception e){ System.out.println("ERROR: Can't leave room");}
    }

    // Function: Gets room and message to send from user, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void messageRoom() throws Exception{
        try {
            System.out.println("Enter Room to Message: ");
            String roomToMsg = clientIn.readLine();
            System.out.println("Enter your message: ");
            String readIn = clientIn.readLine();
            Message msgToServer = makeMessage("messageRoom", readIn, user, roomToMsg);
            sendMessage(msgToServer);
        }catch(Exception e){ System.out.println("ERROR: Can't message room");}
    }

    // Function: Gets user and message to send from user, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void messageUser() throws Exception {
        try {
            System.out.println("Enter User to Message: ");
            String userToMsg = clientIn.readLine();
            System.out.println("Enter your message: ");
            String msg = clientIn.readLine();
            Message msgToServer = makeMessage("messageUser", msg, user, userToMsg);
            sendMessage(msgToServer);
        }catch(Exception e){ System.out.println("ERROR: Can't message room");}
    }

    // Function: Gets message user wishes to send to all users, calls makeMessage and sendMessage functions
    // In: None
    // Out: None
    private void messageAllUsers() throws Exception{
        try{
            System.out.println("Enter your message: ");
            String msg = clientIn.readLine();
            Message msgToServer = makeMessage("messageAllUsers", msg, user);
            sendMessage(msgToServer);
        }catch(Exception e){ System.out.println("ERROR: Can't message room");}
    }

    // Function: calls convertType, and makes message
    // In: String - type of message that is being sent
    // Out: Message
    private Message makeMessage(String type) throws Exception{
        Message msgToServer = null;
        try {
            int code = convertType(type, null);
            msgToServer = new Message(code);
        }catch(Exception e) {System.out.println("ERROR: Making message to Server");}
        return msgToServer;
    }

    // Function: calls convertType, and makes message
    // In: String type - type of message that is being sent, String msg - msg from user
    // Out: Message
    private Message makeMessage(String type, String msg) throws Exception{
        Message msgToServer = null;
        try {
            int code = convertType(type, msg);
            msgToServer = new Message(code, msg);
        }catch(Exception e){System.out.println("ERROR: Making message to Server");}
        return msgToServer;
    }

    // Function: calls convertType, and makes message
    // In: String - type of message that is being sent, String msg - msg from user,
    //     String currentUser - user sending message
    // Out: Message
    private Message makeMessage(String type, String msg, String currentUser) throws Exception{
        Message msgToServer = null;
        try {
            int code = convertType(type, msg);
            msgToServer = new Message(code, msg, currentUser);
        }catch (Exception e){System.out.println("ERROR: Making message to Server");}
        return msgToServer;
    }

    // Function: calls convertType, and makes message
    // In: String - type of message that is being sent, String msg - msg from user,
    //     String currentUser - user sending message, String toRoom - room msg is being sent
    // Out: Message
    private Message makeMessage(String type, String msg, String currentUser, String toRoom) throws Exception{
        Message msgToServer = null;
        try {
            int code = convertType(type, msg);
            msgToServer = new Message(code, msg, currentUser, toRoom);
        }catch (Exception e){System.out.println("ERROR: Making message to Server");}
        return msgToServer;
    }

    // Function: sends message to Server, calls flushOutput
    // In: String msgToServer - message to send to Server
    // Out: None
    private void sendMessage(Message msgToServer) throws Exception{
        toServer.writeObject(msgToServer);
        flushOutput();
    }

    // Function: sends username message to Server, calls flushOutput (different from sendMessage because
    //      this function is called before the incoming Client thread is created so it also retrieves message
    //      from server
    // In: String msgToServer - message to send to Server
    // Out: success code, 100 is success, -100 if not
    private int sendUserNameMessage(Message msgToServer) throws Exception{
        toServer.writeObject(msgToServer);
        flushOutput();
        Message msgFromServer = (Message) inFromServer.readObject();
        System.out.println(msgFromServer.getMsg());
        return msgFromServer.getCode();
    }

    // Function: converts the type of message being sent to an optcode so the server can decipher the type of message
    // In: String type - type of message being sent, String msg - only used when username is being called
    // Out: INT - optcode that corresponds to type of message being sent
    private int convertType(String type, String msg){
        int code;
        switch(type) {
            case "username":
                code = 100;
                user = msg;
                break;
            case "listAllRooms":
                code = 1;
                break;
            case "addRoom":
                code = 2;
                break;
            case "listUsersRooms":
                code = 3;
                break;
            case "joinRoom":
                code = 4;
                break;
            case "leaveRoom":
                code = 5;
                break;
            case "listAllUsers":
                code = 6;
                break;
            case "messageRoom":
                code = 7;
                break;
            case "messageUser":
                code = 8;
                break;
            case "messageAllUsers":
                code = 9;
                break;
            case "listUsersinaRoom":
                code = 10;
                break;
            case "removeuser":
                code = 11;
                break;
            default:
                code = 0;
                break;
        }
        return code;
    }

    // Function: takes in user input and calls routeUser to route input accordingly
    // In: None
    // Out: None
    private void menu() throws Exception{
        String menuChoice = "";
        do{
            try{
                menuChoice = clientIn.readLine();
                routeUser(menuChoice);
            }catch(Exception e){ System.out.println("ERROR: Can't read in from user");}
        }while(!menuChoice.equalsIgnoreCase("exit"));
        inFromServer.close();
        toServer.close();
        System.out.println("Bye Bye");
        System.exit(0);
    }

    // Function: calls removeUser
    // In: None
    // Out: None
    private void exit(){
        removeUser();
        return;
    }

    // Function: creates message to Server to exit, calls makeMessage and sendMessage
    // In: None
    // Out: None
    private void removeUser(){
        try {
            Message msgToServer = makeMessage("removeuser", user);
            sendMessage(msgToServer);
        }catch(Exception e) {System.out.println("ERROR: Removing user from Server");}
    }

    // Function: from user input, routes to corresponding function
    // In: String menuchoice - user command for functionality
    // Out: None
    private void routeUser(String menuChoice) throws Exception {
        try {
            switch (menuChoice) {
                case "listusers":
                    listAllUsers();
                    break;
                case "listrooms":
                    listAllRooms();
                    break;
                case "addroom":
                    getRoomToAdd();
                    break;
                case "joinroom":
                    joinRoom();
                    break;
                case "leaveroom":
                    leaveRoom();
                    break;
                case "listmyrooms":
                    listUsersRooms();
                    break;
                case "listroomusers":
                    listUsersInARooms();
                    break;
                case "messageroom":
                    messageRoom();
                    break;
                case "messageuser":
                    messageUser();
                    break;
                case "messageallusers":
                    messageAllUsers();
                    break;
                case "exit":
                    exit();
                    break;
                default:
                    System.out.println("Do not recognize that command");
                    return;

            }
        }catch(Exception e){ System.out.println("ERROR: Can't route message");}
    }

    // Function: flushes the ObjectOutputStream
    // In: None
    // Out: None
    private void flushOutput() throws Exception{
        try{
            toServer.flush();
            toServer.reset();
        }catch(Exception e){ System.out.println("ERROR: Can't flush output");}
    }

    public static void main(String argv[]) throws Exception{
        TCPClient go = new TCPClient();
        go.start(1234);
    }

}
