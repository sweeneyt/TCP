import java.util.*;

/**
 * Created by tim on 5/18/17.
 */
public class Room {
    private String name;
    private List<String> usernames;

    public Room(String name){
        this.name = name;
        this.usernames = new ArrayList<>();
    }

    private addUser(String user){
        if(usernames.contains(user)) {
            usernames.add(user);
        }
        return;
    }

    private removeUser(String user){
        if(usernames.contains(user))
           usernames.remove(user);
        return;
    }

    private listUsers(){
        for(int i = 0; i < usernames.size(); i++){
            username.
        }
}
