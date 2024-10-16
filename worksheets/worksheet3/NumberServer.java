import java.net.*;
import java.io.*;

//
// Multicast server application.
//
public class NumberServer {

    private int ttl = 1;
    private InetAddress mcGroup = null;
    private int mcPort = -1;
    private DatagramSocket socket = null;
    private int counter = 0;

    // Constructor. Specify host and port for packets, not sockets.
    public NumberServer( String host, int port ) {
        try {
            mcGroup = InetAddress.getByName( host );		// Host in a multicast IP address.
        }
            catch( UnknownHostException ex ) {
            System.out.println( ex );
        }

        mcPort = port;

        try {
            socket = new DatagramSocket(); // Port set at run time
            // socket.setSoTimeout( ttl );    // TTL for all packets
        }
        catch( IOException ex )
        {
            System.out.println( ex );
        }
    }

    // For this example send consecutive numbers every 2 secs
    public void runServer()
    {
        while( true )
        {
            byte[] data = String.valueOf(counter).getBytes();

            DatagramPacket dp = new DatagramPacket( data, data.length, mcGroup, mcPort );

            try
            {
                socket.send( dp );
                System.out.println( "Sent message" );
            }
            catch( IOException ex )
            {
                System.out.println( ex );
            }

            try
            {
                Thread.sleep( 2000 );        // Pause for 2 secs
            }
            catch( InterruptedException ex )
            {
                System.out.println( ex );
            }

            counter++;
        }
    }

    //
    // Main
    //
    public static void main( String[] args ) {
        NumberServer server = new NumberServer( "228.3.4.5", 4455 );
        server.runServer();
    }
}
