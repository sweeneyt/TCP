/**
 * Created by tim on 5/18/17.
 */

import java.io.Serializable;

public class Header implements Serializable{
    private int code;

    public Header(int code){
        this.code = code;
    }

    public int getCode(){
        return this.code;
    }

}
