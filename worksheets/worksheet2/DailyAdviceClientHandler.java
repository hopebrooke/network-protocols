

import java.net.*;
import java.io.*;
import java.util.*;

public class DailyAdviceClientHandler extends Thread {
    private Socket socket = null;

    public DailyAdviceClientHandler(Socket socket) {
		super("DailyAdviceClientHandler");
		this.socket = socket;
    }

    // Feel free to add any 'hilarious' one-line advice strings here.
    private String[] adviceList = {
        "Take smaller bites",
        "Go for the tight jeans. No they do NOT make you look fat",
        "One word: inappropriate",
        "Just for today, be honest. Tell your boss what you *really* think",
        "You might want to rethink that haircut"
    };

    // The advice sent to the client is just randomly selected from the above list.
    private String getAdvice() {
        int random = (int) (Math.random() * adviceList.length);
        return adviceList[random];
    }

    public void run() {

		try {

			// Input and output streams to/from the client.
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

			// Logging.
			InetAddress inet = socket.getInetAddress();
			Date date = new Date();
			System.out.println("\nDate " + date.toString() );
			System.out.println("Connection made from " + inet.getHostName() );


            String advice = getAdvice();
            out.println(advice);    		 // Write to client
            System.out.println(advice);		 // Local server echo
            
            //out.flush();

            try{
                Thread.sleep(5000);
            } catch (InterruptedException e){
                System.out.println("problemo");
            }

            System.out.println( "Closing client socket" ); 

			// Free up resources for this connection.
			out.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}