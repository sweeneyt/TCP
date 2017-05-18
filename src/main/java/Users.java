import java.util.ArrayList;
import java.util.List;

/**
 * Created by tim on 5/18/17.
 */
public class User {
    private String name;
    private List<String> userRooms;

    public User(String name){
        this.name = name;
    }

    public void addRoom(String room){
        if(userRooms.contains(room))
            userRooms.add(room);
    }

    public void removeRoom(String room){
        if(userRooms.contains(room))
            userRooms.remove(room);
    }

    public List listRooms(){
        return userRooms;
    }

}
