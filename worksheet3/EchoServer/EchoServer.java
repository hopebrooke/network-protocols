
/*
 * Original source code from
 * http://stilius.net/java/java_ssl.php
 */

import javax.net.ssl.*;
import java.io.*;

public class EchoServer {

    // state
    private SSLServerSocket sslserversocket = (SSLServerSocket)null;
    private SSLSocket sslsocket = (SSLSocket)null;

    // constructor
    public EchoServer( int port ) {
      try {
        SSLServerSocketFactory sslserversocketfactory =
          (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        sslserversocket =
          (SSLServerSocket) sslserversocketfactory.createServerSocket(port);
      }
      catch (Exception exception) {
        exception.printStackTrace();
      }
    }

    // run
    private void goServer() {
      try {
        sslsocket = (SSLSocket) sslserversocket.accept();

        BufferedReader bufferedreader = new BufferedReader(
                                           new InputStreamReader(
                                              sslsocket.getInputStream() ) );

        String string = null;
        while ((string = bufferedreader.readLine()) != null) {
          System.out.println(string);
          System.out.flush();
        }
        sslsocket.close();
        sslserversocket.close();
      } 
      catch (Exception exception) {
        exception.printStackTrace();
      }
    }

    // main
    public static void main( String[] args ) {
      EchoServer e = new EchoServer( 9999 );
      e.goServer();
    }

}
