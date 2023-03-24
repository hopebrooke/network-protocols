import java.io.*;
import java.net.*;
import java.util.*;


public class Client {

	private Socket socket = null;
	private PrintWriter socketOutput = null;
	private BufferedReader socketInput = null;

    // Initialise Client
    public Client() {
        try {
			// Try and create the socket.
			socket = new Socket("localhost", 6001);
			// Chain a writing and reading stream
			socketOutput = new PrintWriter(socket.getOutputStream(), true);
			socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		} catch (UnknownHostException e) {
			System.err.println("Don't know about host.\n");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to host.\n");
			System.exit(1);
		}
    }

    // Method to run auction service
    // Takes command line arguments as parameter
	public void runAuction(String[] args) {

		String fromServer;
        
		try { 
			// Send input to server as string
			String argsAsString = Arrays.toString(args);
			// Remove brackets
			socketOutput.println(argsAsString.substring(1, argsAsString.length()-1) );
            
            // Read output from server
			fromServer = socketInput.readLine();
            // Print to terminal for client
			System.out.println(fromServer);

            // If client has asked to show table then it will be multiple lines
            // so keep reading in from server while there are lines to read
			while((fromServer=socketInput.readLine())!=null) {
				// Echo server string.
				System.out.println(fromServer);
			}

            // Free up resources
			socketOutput.close();
			socketInput.close();
			socket.close();

		} catch (IOException e) {
			System.err.println("I/O exception during execution\n");
			System.exit(1);
		}
	}

	public static void main(String[] args) {
		// Check for correct num of arguments
		if( (args.length < 1) || (args.length > 3) ){
			System.out.println( "Arguments should be of the form: 'show', 'item <string>' or 'bid <item> <value>'.");
			return;
		}
		// Initialise and run client
		Client client = new Client();
		client.runAuction(args);
	}
}
