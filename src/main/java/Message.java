/**
 * Message Class
 * holds all msg from user and code to route server
 */

import java.net.Socket;
import java.io.Serializable;

public class Message implements Serializable{
    private String msg;
    private Header head;
    private String userFrom;
    private String roomToMsg;
    private Socket userSocket;

    public Message(){
        this.msg = null;
        this.head = null;
        this.userFrom = null;
        this.roomToMsg = null;
        this.userSocket = null;
    }

    public Message(int code){
        this.head = new Header(code);
    }

    public Message(int code, String msg){
        this(code);
        this.msg = msg;
    }

    public Message(int code, String msg, String userFrom){
        this(code, msg);
        this.userFrom = userFrom;
    }

    public Message(int code, String msg, String userFrom, String roomToMsg){
        this(code, msg, userFrom);
        this.roomToMsg = roomToMsg;
    }

    public Header getHeader()
    {
        return head;
    }

    public String getMsg(){
        return msg;
    }

    public int getCode(){
        return this.getHeader().getCode();
    }

    public String getUserFrom() {
        return userFrom;
    }

    public String getRoomToMsg() {
        return roomToMsg;
    }

}


