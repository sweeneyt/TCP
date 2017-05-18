import java.util.*;

/**
 * Created by tim on 5/18/17.
 */
public class Room {
    private String name;
    private List<String> userNames;

    public Room(String name){
        this.name = name;
        this.userNames = new ArrayList<>();
    }

    private void addUser(String user){
        if(userNames.contains(user)) {
            userNames.add(user);
        }
        return;
    }

    private void removeUser(String user){
        if(userNames.contains(user))
           userNames.remove(user);
        return;
    }

    private List listUsers() {
        return userName;
    }
}
