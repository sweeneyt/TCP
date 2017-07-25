/**
    Room Class
    Holds all the room name and List of its users
    Room class can add, remove, and access its list
 **/


import java.util.*;


public class Room {
    private String name;
    private List<String> userNames;
    private int size;

    public Room(String name){
        this.name = name;
        this.userNames = new ArrayList<>();
        size = userNames.size();
    }

    public String getName() {
        return name;
    }

    public boolean addUser(String user){
        if(!userNames.contains(user)) {
            userNames.add(user);
            return true;
        }
        return false;
    }

    public boolean removeUser(String user){
        if(userNames.contains(user)) {
            userNames.remove(user);
            return true;
        }
        else
            return false;
    }

    public List getUserNames() {
        return userNames;
    }

    public String getUserNamesAsaString(){
        String users = "Room " + name +"'s user list:";
        for(int i = 0; i < userNames.size(); i++){
            users = users + "\n" + userNames.get(i);
        }
        return users;
    }

    public int getSize(){
        return userNames.size();
    }


}
