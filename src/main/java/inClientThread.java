/**
 *	inClient Thread
 *	Created by client to handle all incoming messages
 *
 **/

import java.io.IOException;
import java.io.ObjectInputStream;


public class inClientThread implements Runnable {

	private ObjectInputStream inStream;

	public inClientThread(ObjectInputStream inStreamIn) {
		inStream = inStreamIn;
	}

	@Override
	public void run() {
		while (true) {
			if (Thread.currentThread().isInterrupted()) {
				System.out.println("Its being interrupted");
				return;
			}

			Message msgFromServer;
			//try to get incoming packet
			try {
				msgFromServer = (Message) inStream.readObject();
			} catch (ClassNotFoundException e) {
				System.out.println("ERROR: Class Not Found during runtime");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			routeServerMsg(msgFromServer);
		}
	}

	public void routeServerMsg(Message msgFromServer) {
		System.out.println(msgFromServer.getMsg());
	}
}
