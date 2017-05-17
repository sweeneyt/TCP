/**
 * Created by tim on 5/13/17.
 */

import java.sql.*;

public class dB {
    public static void main(String argv[]){
        try(
                Connection con = DriverManager.getConnection("" +
                        "jdbc:mysql://localhost:3306/")
                )
    }
}
