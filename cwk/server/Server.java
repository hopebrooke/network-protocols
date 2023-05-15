import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Server {

    private Map < String, Double > itemBids; // Map of items and bids
    private Map < String, String > itemClients; // Map of items and client
    private File log; // File to log to

    // Initialise Server
    public Server() {
        itemBids = new HashMap < > ();
        itemClients = new HashMap < > ();

        File log = new File("log.txt");
        // Delete current log.txt file if it exists
        boolean exists = log.delete();
        // Create file for logging
        try {
            log.createNewFile();
        } catch (IOException error) {
            error.printStackTrace();
        }
    }

    // Method to add item
    // Takes item name as parameter
    // Returns false if item already exists and is therefore not added
    // Returns true if item is successfully added
    public synchronized boolean addItem(String item) {
        if (itemBids.containsKey(item)) {
            return false;
        } else {
            // Set bid to 0, and set client's IP address to no bids
            itemBids.put(item, 0.0);
            itemClients.put(item, "<no bids>");
            return true;
        }
    }

    // Method to make bid
    // Takes item name, bid value and client's IP address as parameters
    // Returns 1 if bid was a valid value and accepted
    // Returns 2 if bid was a valid value but was rejected for not being high enough
    // Returns 3 if bid was not valid, either for being negative or for being for a non-existent item
    public synchronized int bid(String item, double bid, String clientIP) {
        if (!itemBids.containsKey(item) || bid <= 0) return 3;
        double currentBid = itemBids.get(item);
        if (bid > currentBid) {
            itemBids.put(item, bid);
            itemClients.put(item, clientIP);
            return 1;
        } else return 2;
    }

    // Method to show table
    // Returns string containing items, bids and IP's with new line characters after every entry
    public synchronized String show() {
        String show = "";
        for (String item: itemBids.keySet()) {
            show = show + item + " : " + itemBids.get(item) + " : " + itemClients.get(item) + "\n";
        }
        // Remove last new line char if not empty
        if (!show.isEmpty()) show = show.substring(0, show.length() - 1);
        // Display message if empty table
        else show = "There are currently no items in this auction.";
        return show;
    }

    // Method to log all valid requests
    // Takes the client's IP address and their request as parameters
    public synchronized void log(String clientIP, String request) {
        try {
            PrintWriter logging = new PrintWriter(new FileOutputStream("log.txt", true));
            // Format date and time appropriately
            String datePattern = "dd-MM-yyyy|HH:mm";
            SimpleDateFormat simpleDateTime = new SimpleDateFormat(datePattern);
            String dateTime = simpleDateTime.format(new Date());
            // Print to logging file
            logging.println(dateTime + "|" + clientIP + "|" + request);
            // Close logging file print writer
            logging.close();
        } catch (FileNotFoundException error) {
            System.out.println("There has been an error accessing the 'log.txt' file.");
            error.printStackTrace();
        }
    }

    // Method to start running the server
    // This initialises the executor with 30 threads 
    // and calls the client handler for each new client
    public void runServer() throws IOException {
        ServerSocket server = null;
        ExecutorService service = null;
        // Try to open up the listening port
        try {
            server = new ServerSocket(6001);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 6001.");
            System.exit(0);
        }

        // Initialise the executor.
        service = Executors.newFixedThreadPool(30);
        // For each new client, submit a new handler to the thread pool.
        while (true) {
            Socket client = server.accept();
            service.submit(new ClientHandler(client, this));
        }
    }

    public static void main(String[] args) throws IOException {
        // Initialise and run Server object
        Server server = new Server();
        server.runServer();
    }
}