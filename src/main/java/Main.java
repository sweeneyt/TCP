import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException{
        Scanner console = new Scanner(System.in);
        String nickname = console.nextLine();
        String username = console.nextLine();
        String realName = console.nextLine();


       Socket socket= new Socket("chat.freenode.net", 6667);
       PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
       Scanner input = new Scanner(socket.getInputStream());

       writeout(nickname, out);
       writeout(username, out);
       writeout(realName, out);

       while(input.hasNext()) {
           String servermessage = input.nextLine();
           System.out.println(" <<<" + servermessage);
       }

       input.close();
       out.close();
       socket.close();
       System.out.println("Done");

    }

    public static void writeout(String name, PrintWriter out){
        System.out.println(" >>>" + name + "\r\n");
        out.flush();
    }
}
t