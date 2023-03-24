
import java.net.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Server {

  private Map<String, Double> itemBids;
  private Map<String, String> itemClients;
  private File log;

  public Server() {
    itemBids = new HashMap<>();
    itemClients = new HashMap<>();

    File log = new File("log.txt");
    boolean exists = log.delete();
    try{
      log.createNewFile();
    } catch( IOException error) {
      error.printStackTrace();
    }
  }

  // Create getters and setters:
  public synchronized boolean addItem(String item){
    if( itemBids.containsKey(item)){
      return false;
    } else {
      // Set bid to 0
      itemBids.put(item, 0.0);
      itemClients.put(item, "<no bids>");
      return true;
    }
  }

  public synchronized int bid(String item, double bid, String clientIP) {
    if( !itemBids.containsKey(item) || bid <= 0 ) return 3;
    double currentBid = itemBids.get(item);
    if( bid > currentBid) {
      itemBids.put(item, bid);
      itemClients.put(item, clientIP);
      return 1;
    } else {
      return 2;
    }
  }

  public synchronized String show() {
    // return string of all items, bids, clients
    String show = "";
    for(String item : itemBids.keySet()) {
      show = show + item + " : " + itemBids.get(item) + " : " + itemClients.get(item) + "\n";
    }
    // Remove last new line char if not empty
    if( !show.isEmpty()) show = show.substring(0, show.length() -1);
    else show = "There are currently no items in the table";
    return show;
  }

  public synchronized void log(String clientIP, String request ) {
    try {
      PrintWriter logging = new PrintWriter(new FileOutputStream("log.txt", true));
      String datePattern = "dd-MM-yyyy|HH:mm";
      SimpleDateFormat simpleDateTime = new SimpleDateFormat(datePattern);
      String dateTime = simpleDateTime.format(new Date());
      logging.println( dateTime + "|" + clientIP + "|" + request);
      System.out.println( dateTime + "|" + clientIP + "|" + request);
      logging.close();
    } catch ( FileNotFoundException error ){
      System.out.println("problemo");
      error.printStackTrace();
    }
  }

  public void runServer(/**String[] args**/) throws IOException {

    ServerSocket server = null;
    ExecutorService service = null;

    // Try to open up the listening port
    try {
      server = new ServerSocket(6001);
    } catch (IOException e) {
      System.err.println("Could not listen on port: 6001.");
      System.exit(-1);
    }

    // Initialise the executor.
    service = Executors.newFixedThreadPool(30);

    // For each new client, submit a new handler to the thread pool.
    while( true )
    {
      Socket client = server.accept();
      service.submit( new ClientHandler(client, this) );
    }
  }

  public static void main(String[] args) throws IOException{

    // Initialise and run Server object
    Server server = new Server();
    server.runServer();
  }
}