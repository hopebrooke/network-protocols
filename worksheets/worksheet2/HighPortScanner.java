
// Recommended to only run this on a School machine, as it can be very slow if
// attempted over the WiFi network.
//

import java.net.*;			 // For Socket, UnknownHostException
import java.io.*;					// For IOException

public class HighPortScanner {

  public static void main( String[] args ) {

    // If called with a hostname as the first command line argument, use that instead.
    String hostname = "localhost";
    if( args.length > 0 ) hostname = args[0];

    // Loop over all of the reserved ports, try to open a socket to each in turn.
    for( int i = 1025; i < 65536; i++ ) {
    try {
      Socket s = new Socket(hostname,i);
      System.out.println("There is a server on port " + i + " of " + hostname);
    }
    catch( UnknownHostException ex ) {
      System.err.println(ex);
      break;
    }
    catch( IOException ex ) {
      // Must not be a server on this port; don't report anything.
    }
    } // End of the port loop.

  }  // End of main()

}  
