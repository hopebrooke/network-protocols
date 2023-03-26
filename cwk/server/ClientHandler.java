import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler extends Thread {

    private Socket socket = null;
    private Server server = null;

    // Initialises Client Handler
    // Takes the socket of the client and the sever calling it as parameters
    public ClientHandler(Socket socket, Server server) {
        super("ClientHandler");
        this.socket = socket;
        this.server = server;
    }

    // Method to run the client handler
    public void run() {
        try {
            // Input and output streams to/from the client.
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Get client IP for logging
            InetAddress inet = socket.getInetAddress();
            String clientIP = inet.getHostAddress();

            // Input args is string of arguments
            String inputArgs = in.readLine();
            // Convert string to array of string arguments
            String[] arrayArgs = inputArgs.split(",");

            // Define output string
            String output;

            // First check for show
            if (arrayArgs.length == 1 && arrayArgs[0].equals("show")) {
                output = server.show();
                server.log(clientIP, inputArgs);
            }
            // Check for item adding 
            else if (arrayArgs.length == 2 && arrayArgs[0].equals("item")) {
                // Check for failure/success
                if (server.addItem(arrayArgs[1])) output = "Success.";
                else output = "Failure.";
                server.log(clientIP, inputArgs);
            }
            // Check for item bidding
            else if (arrayArgs.length == 3 && arrayArgs[0].equals("bid")) {
                // Make sure string bid is 2dp
                double bid = Double.parseDouble(arrayArgs[2]);
                String roundedString = String.format("%.2f", bid);
                // Convert back to double
                double roundedBid = Double.parseDouble(roundedString);
                if (server.bid(arrayArgs[1], roundedBid, clientIP) == 1) output = "Accepted.";
                else if (server.bid(arrayArgs[1], roundedBid, clientIP) == 2) output = "Rejected.";
                else output = "Failure.";
                server.log(clientIP, inputArgs);
            } else {
                output = "Arguments should be of the form: 'show', 'item <string>' or 'bid <item> <value>'.";
            }

            // Send output back to client
            out.println(output);
            // Free up resources for this connection.
            out.close();
            in.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}