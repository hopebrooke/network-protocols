
/*
 * Original source code from
 * http://stilius.net/java/java_ssl.php
 */

import javax.net.ssl.*;
import java.io.*;

public class EchoClient {

    // state
    private SSLSocket sslsocket;

    // constructor
    public EchoClient( int port ) {
      try {
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        sslsocket = (SSLSocket) sslsocketfactory.createSocket("localhost", port);
      }
      catch (Exception exception) {
        exception.printStackTrace();
      }
    }

    // run
    private void goClient() {
      try {
        BufferedReader bread = new BufferedReader(
                                  new InputStreamReader(
                                     System.in ) );
        BufferedWriter bwrite = new BufferedWriter(
                                   new OutputStreamWriter(
                                      sslsocket.getOutputStream() ) );

        String string = null;
        while ((string = bread.readLine()) != null) {
          bwrite.write(string + '\n');
          bwrite.flush();
        }
        sslsocket.close();
      }
      catch (Exception exception) {
        exception.printStackTrace();
      }
    }

    // main
    public static void main( String[] args ) {
      EchoClient e = new EchoClient( 9999 );
      e.goClient();
    }
}
