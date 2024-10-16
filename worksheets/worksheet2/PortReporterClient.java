

import java.io.*;
import java.net.*;

public class PortReporterClient
{
    public void connect() {
        try {
            // Try to open up a connection with this host ('localhost'), port number 4242.
            // You must first run the server 'DailyAdviceServer' on the same host (i.e. in another shell).
            Socket s = new Socket( "localhost", 5555 );

            // Buffer the input stream for performance.
            // BufferedReader reader = new BufferedReader(
                                    //    new InputStreamReader(
                                    //       s.getInputStream() ) );


            
            // String ports = reader.readLine();
            String ports = ("Source port : " + s.getPort() + ", Destination port: " + s.getLocalPort());
            System.out.println(ports);

            // Close the reader and the connection.
            // reader.close();
            s.close();
        }
        catch( IOException e )
        {
            System.out.println( e );
        }
    }

    public static void main(String[] args)
    {
        PortReporterClient client = new PortReporterClient();
        client.connect();
    }
}