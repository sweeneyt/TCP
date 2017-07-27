/**
 * User class
 * holds username, list of rooms user is in, and the users objectoutputstream
 * so it can receive messages
 */

import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


public class User {
    private String name;
    private List<String> userRooms;
    private ObjectOutputStream userOut;

    public User(String name){
        this.name = name;
        this.userRooms = new ArrayList<>();
        this.userOut = null;
    }

    public User(String name, ObjectOutputStream userOut){
        this(name);
        this.userOut = userOut;
    }

    public boolean addRoom(String room){
        if(userRooms.contains(room)) {
            return false;
        }
        else{
            userRooms.add(room);
            return true;
        }
    }

    public void removeRoom(String room){
        if(userRooms.contains(room))
            userRooms.remove(room);
    }

    public boolean isRoomInRoomList(String room){
        if(userRooms.contains(room))
            return true;
        else
            return false;
    }

    public List getUserRooms(){
        return userRooms;
    }

    public String getName() { return name; }

    public ObjectOutputStream getUserOut(){
        return userOut;
    }

}
