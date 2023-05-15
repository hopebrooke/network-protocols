import java.net.*;
import java.io.*;
import java.util.*;

//
// Multicast client application
//
public class NumberClient {

    private InetAddress mcGroup = null;
    private int mcPort = -1;
    private MulticastSocket socket = null;
    private int timeout = 4000;

    //
    // Constructor
    //
    public NumberClient( String host, int port ) {
      try {
        mcGroup = InetAddress.getByName( host );  // IP address for the multicast
      }
      catch( UnknownHostException h ) {
        System.out.println( h );
      }

      mcPort = port;  // Set the port

      try {
        socket = new MulticastSocket( mcPort );   // Socket bound to the port
        socket.setSoTimeout( timeout );           // Set socket time out
        socket.joinGroup( mcGroup );              // Join multicast group
      }
      catch( IOException e ) {
        System.out.println( e );
      }
    }

    //
    // Run the client
    //
    public void runClient() {

        System.out.println("Client running\n");

        int num = 0;

        while( num < 5 ) {  // Read 5 datagrams

            byte[] data = new byte[256];
            DatagramPacket dp = new DatagramPacket(data, data.length);

            try {
              socket.receive( dp );     // Socket receives datagram from server
              System.out.println( new String(dp.getData()) );
            }
            catch( IOException e ) {
              System.out.println( e ); // Or times out
            }
            num = num + 1;
        }

        try {
	        socket.leaveGroup( mcGroup );  // Leave multicasst group
	    }
	    catch( IOException ex ) {
	    	System.out.println( ex );
	    }

        socket.close();                // Close socket
    }

    //
    // Main
    //
    public static void main( String[] args ) {
        NumberClient client = new NumberClient( "228.3.4.5", 4455 );
        client.runClient();
    }
}