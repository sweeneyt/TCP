/**
 *  Server's Helping Functions
 *  After message from Client are retrieved this class is called
 *  Routes all messages based on optcode
 *  provides functionality the user wants
 *
 */

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ServerFunctions {

    private List<User> userList;
    private List<Room> roomList;
    private Message msgFromClient;
    private Message msgToClient;

    private final String ERRORUserName = "ERROR: Name Already in use";
    private final String ERRORRoomName = "ERROR: Room Name Already in use";
    private final String ERRORNoRoom = "ERROR: Room does not exist";
    private final String ERRORAlreadyMember = "ERROR: You are Already in that room";
    private final String ERRORNotinRoom = "ERROR: You are not in Room";
    private final String ERRORUserNotMember = "ERROR: User not allowed to message a room not currently a member of";
    private final String ERRORNoUser = "ERROR: User not on Server";
    private final String ERROROnlyUser = "ERROR: Your message was not sent to anyone else, " +
            "since there are not other users in the system";

    public ServerFunctions(){
        this.roomList = new ArrayList();
        this.userList = new ArrayList();
    }

    // Function: reads in optcode code from user's message and routes user accordingly
    // In: Message msgFromClient - user's message
    // Out: Message msgToClient - message back to client
    public Message decipherMessage(Message msgFromClient) throws IOException{
        try {
            this.msgFromClient = msgFromClient;
            System.out.println(msgFromClient.getCode());
            int code = msgFromClient.getCode();
            switch (code) {
                case 100:
                    addUser();
                    break;
                case 1:
                    listAllRooms();
                    break;
                case 2:
                    System.out.println("Got a new Room!");
                    addRoom();
                    break;
                case 3:
                    listUsersRooms();
                    break;
                case 4:
                    joinRoom();
                    break;
                case 5:
                    leaveRoom();
                    break;
                case 6:
                    listAllUsers();
                    break;
                case 7:
                    messageRoom();
                    break;
                case 8:
                    messageUser();
                    break;
                case 9:
                    messageAllUsers();
                    break;
                case 10:
                    listUsersinARoom();
                    break;
                case 11:
                    removeUser();
                    break;
                default:
                    break;
            }
        }catch(Exception e){System.out.println("Error: routing packets");}
        return msgToClient;
    }

    // Function: reads in optcode code from user's message and routes user accordingly
    // In: Message msgFromClient - user's message
    // Out: Message msgToClient - message back to client
    public Message decipherMessage(Message msgFromClient, ObjectOutputStream userOut) throws IOException {
        try {
            this.msgFromClient = msgFromClient;
            System.out.println(msgFromClient.getCode());
            int code = msgFromClient.getCode();
            switch (code) {
                case 100:
                    addUser(userOut);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {System.out.println("ERROR: adding user sending user socket");}
        return msgToClient;
    }

    // Function: calls checkUserAvail to see if user name is unique, sends message back confirming/denying success
    // In: ObjectOutputSteam userOut - gets users Outputstream to store in user class
    // Out: None
    private void addUser(ObjectOutputStream userOut){
        boolean success;
        String userToAdd = msgFromClient.getMsg();
        success = checkUserAvail(userToAdd, userOut);
        if(success) {
            makeMessage(100, "Name added", userToAdd);
            System.out.println(userToAdd + " joined!");
        }
        else
            makeMessage(-100, ERRORUserName);
        return;
    }

    // Function: calls checkUserAvail to see if user name is unique, sends message back confirming/denying success
    // In: None
    // Out: None
    private void addUser(){
        boolean success;
        String userToAdd = msgFromClient.getMsg();
        success = checkUserAvail(userToAdd);
        if(success) {
            makeMessage(100, "Name added", userToAdd);
            System.out.println(userToAdd + " joined!");
        }
        else
            makeMessage(-100, ERRORUserName);
        return;
    }

    // Function: searches userList to make sure username is unique, and if true creates new user
    // In: String Username, ObjectOutputSteam userOut - gets users Outputstream to store in user class
    // Out: Boolean, true if username is unique (does not appear in userList, false otherwise
    private boolean checkUserAvail(String userToAdd, ObjectOutputStream userOut){
        for(int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equalsIgnoreCase(userToAdd))
                return false;
        }
        User newUser = new User(userToAdd, userOut);
        userList.add(newUser);
        return true;
    }

    // Function: searches userList to make sure username is unique, and if true creates new user
    // In: String Username
    // Out: Boolean, true if username is unique (does not appear in userList, false otherwise
    private boolean checkUserAvail(String userToAdd){
        for(int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getName().equalsIgnoreCase(userToAdd))
                return false;
        }
        User newUser = new User(userToAdd);
        userList.add(newUser);
        return true;
    }

    // Function: calls findRoom, if room doesn't exist adds room to roomList, create message to user
    // In: None
    // Out: None
    private void addRoom(){
        int success;
        String roomToAdd = msgFromClient.getMsg();
        success = findRoom(roomToAdd);
        if(success < 0) {
            Room newRoom = new Room(roomToAdd);
            roomList.add(newRoom);
            makeMessage(2, "Room added");
        }
        else {
            makeMessage(-2, ERRORRoomName);
            return;
        }
    }

    // Function: searches roomList for room
    // In: String roomToCheck
    // Out: Int, location in the roomList List the room is located, -1 if not in list
    private int findRoom(String roomToCheck) {
        //System.out.println("Does " +roomToCheck +" exits?");
        for(int i = 0; i < roomList.size(); i++) {
            if (roomList.get(i).getName().equalsIgnoreCase(roomToCheck)) {
          //      System.out.println("YEP!");
                return i;
            }
        }
        return -1;
    }

    // Function: traverses through roomList and creates a String with all the rooms, creates message with info
    // In: None
    // Out: None
    private void listAllRooms() {
        String roomsString = "Rooms:";
        for(int i = 0; i < roomList.size(); i++) {
            roomsString = roomsString + "\n" + roomList.get(i).getName();
        }
        makeMessage(3, roomsString);
        return;
    }

    // Function: traverses through userList and creates a String with all the rooms, creates message with info
    // In: None
    // Out: None
    private void listAllUsers(){
        String usersString = "Users online:";
        for(int i = 0; i < userList.size(); i++){
            usersString = usersString + "\n" + userList.get(i).getName();
        }
        System.out.println(usersString);
        makeMessage(6, usersString);
        return;
    }

     // Function: checks user isn't already in room, if not add user to roomList's userlist and room to the user's
     //             roomlist
     // In: None
     // Out: None
     private void joinRoom() {
        System.out.println("Server join room");
        String roomToJoin = msgFromClient.getMsg();
        int i = findRoom(roomToJoin);
        if(i >= 0)
        {
            boolean success = roomList.get(i).addUser(msgFromClient.getUserFrom());
            if(success){
                System.out.println(msgFromClient.getUserFrom() + " just entered " +roomToJoin);
                addRoomToUserRooms(roomToJoin);
                makeMessage(4, "You joined "+roomToJoin);
            }
            else {
                makeMessage(-4, ERRORAlreadyMember);
            }
        }
        else
            makeMessage(-4, ERRORNoRoom);
    }

    // Function: finds room, checks if user is in room, removes user, if room occupancy == 0, deletes room
    // In: None
    // Out: None
    private void leaveRoom() {
        String roomToLeave = msgFromClient.getMsg();
        int i = findRoom(roomToLeave);
        if(i >= 0) {
            if (roomList.get(i).removeUser(msgFromClient.getUserFrom())) {
                System.out.println(msgFromClient.getUserFrom() + " just left " + roomToLeave);
                makeMessage(5, "You left " + roomToLeave);
                removeRoomToUserRooms();
                if(roomList.get(i).getSize() <= 0)
                {
                    System.out.println( "room size = _"+roomList.get(i).getSize());
                    roomList.remove(i);
                }
            } else
                makeMessage(-5, ERRORNotinRoom);
        }
        else
            makeMessage(-5, ERRORNoRoom);
    }

    // Function: finds room, checks if user is in room, finds all users in room, sends them the user's message
    // In: None
    // Out: None
    private void messageRoom() throws IOException{
        String roomToMsg = msgFromClient.getRoomToMsg();
        String userFrom = msgFromClient.getUserFrom();
        String msgToRoom = msgFromClient.getMsg();
        int i = findRoom(roomToMsg);
        if(i >= 0) {
            if (isUserInRoom(userFrom, roomToMsg)) {
                Room toMsg = roomList.get(i);
                String msgToBroadCast = "NEW ROOM MESSAGE: Room: " + roomToMsg + " From: " +
                        userFrom + " :: " + msgToRoom;
                System.out.println("broadcasting" + msgToBroadCast);
                List<String> roomUsers = toMsg.getUserNames();
                for (int j = 0; j < userList.size(); j++) {
                    for (int k = 0; k < roomUsers.size(); k++) {
                        if (userList.get(j).getName().equals(roomUsers.get(k))) {
                            Message toCast = new Message(2, msgToBroadCast);
                            ObjectOutputStream out = userList.get(j).getUserOut();
                            out.writeObject(toCast);
                        }

                    }
                }
                msgToClient = new Message(2, "message sent");
            }
            else
                    msgToClient = new Message(-2, ERRORUserNotMember);
        }
        else
            msgToClient = new Message(-2, ERRORNoRoom);

    }

    // Function: finds user to message, checks if user is online, sends user a message
    // In: None
    // Out: None
    private void messageUser() throws Exception{
        String userToMsg = msgFromClient.getRoomToMsg();
        String userFrom = msgFromClient.getUserFrom();
        String msgToUser = "NEW PRIVATE MESSAGE: From: " + userFrom + " :: " +msgFromClient.getMsg();
        int i = findUser(userToMsg);
        if(i >= 0){
            User toMsg = userList.get(i);
            Message toCast = new Message(2, msgToUser);
            ObjectOutputStream out = toMsg.getUserOut();
            out.writeObject(toCast);
            msgToClient = new Message(2, "message sent");
        }
        else{
            msgToClient = new Message(2, ERRORNoUser);
        }

    }

    // Function: finds all users, sends all users a message
    // In: None
    // Out: None
    private void messageAllUsers() throws Exception{
        String userFrom = msgFromClient.getUserFrom();
        String msgToUsers = "NEW BROADCAST MESSAGE: From: " + userFrom + " :: " +msgFromClient.getMsg();
        if(userList.size() > 1) {
            for (int i = 0; i < userList.size(); i++) {
                User toMsg = userList.get(i);
                Message toCast = new Message(2, msgToUsers);
                ObjectOutputStream out = toMsg.getUserOut();
                out.writeObject(toCast);
            }
            msgToClient = new Message(2, "message sent");
        }
        else{
            msgToClient = new Message(-1, ERROROnlyUser);
        }

    }

    // Function: search a user's roomList to see if user is in room
    // In: String user, String room
    // Out: Boolean, true if user is in room, false, otherwise
    private boolean isUserInRoom(String user, String room){
        int i = findUser(user);
        boolean isRoom = userList.get(i).isRoomInRoomList(room);
        return isRoom;
    }

    // Function: finds user in userList, add room to user's roomList
    // In: String roomToJoin
    // Out: None
    private void addRoomToUserRooms(String roomToJoin){
        String user = msgFromClient.getUserFrom();
        int location = findUser(user);
        userList.get(location).addRoom(roomToJoin);
        return;
    }

    // Function: finds user in userList, removes room in roomList
    // In: None
    // Out: None
    private void removeRoomToUserRooms(){
        String user = msgFromClient.getUserFrom();
        for(int i= 0; i < userList.size(); i++){
            if(userList.get(i).getName().equalsIgnoreCase(user)){
                userList.get(i).removeRoom(msgFromClient.getMsg());
            }
        }
        return;
    }

    // Function: finds user, gets user's roomList and return String to user in message
    // In: None
    // Out: None
    private void listUsersRooms(){
        String user = msgFromClient.getUserFrom();
        int i = findUser(user);
        List usersRooms = userList.get(i).getUserRooms();
        String msgBack = printList(usersRooms);
        makeMessage(3, msgBack);
    }

    // Function: finds room in roomList and lists all users in that room
    // In: None
    // Out: None
    private void listUsersinARoom(){
        String room = msgFromClient.getMsg();
        int i = findRoom(room);
        String users;
        if(i >= 0) {
            users = roomList.get(i).getUserNamesAsaString();
            System.out.println(users);
        }
        else
            users = ERRORNoRoom;
        makeMessage(2, users);
    }

    // Function: finds user in userList, removes user, searchs roomList's userlist and removes user from those list
    // In: None
    // Out: None
    private void removeUser(){
        String user = msgFromClient.getMsg();
        int i = findUser(user);
        String userName = userList.get(i).getName();
        List userRoomList = userList.get(i).getUserRooms();
        for(int j = 0; j < userRoomList.size(); j++){
            for(int k = 0; k < roomList.size(); k++){
                if(roomList.get(k).getName().equalsIgnoreCase((String) userRoomList.get(j))){
                    roomList.get(k).removeUser(userName);
                }
            }
        }
        userList.remove(i);
        makeMessage(99, "You have left the chat");
    }

    // Function: makes a List into a String
    // In: List listToPrint
    // Out: String - the list as a String
    private String printList(List listToPrint){
        String roomsString = "";
        for(int i = 0; i < listToPrint.size(); i++) {
            roomsString = roomsString + listToPrint.get(i) + "\n";
        }
        return roomsString;
    }

    // Function: searches userList to find user
    // In: String user, user being searched for
    // Out: INT - location in userList, user is, -1 if not in list
    private int findUser(String user){
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getName().equalsIgnoreCase(user))
                return i;
         }
         return -1;
    }

    // Function: creates new message
    // In: INT code, optcode for client, String msg
    // Out: None
    private void makeMessage(int code, String msg){
        msgToClient = new Message(code, msg);
    }
    // Function: creates new message
    // In: INT code, optcode for client, String msg, String userSending msg
    // Out: None
    private void makeMessage(int code, String msg, String userName) {
        msgToClient = new Message(code, msg, userName);
    }

}
