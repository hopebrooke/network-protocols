// Minimal client to test the non-blocking date server.

import java.net.*;
import java.io.*;

public class Client
{
	public static void main( String[] args )
	{
		try 
		{ 
			Socket connection = new Socket( "localhost", 9898 );

			// Just need to read; no need to write.
			BufferedReader r = new BufferedReader(
								new InputStreamReader( connection.getInputStream() ) );

			// Read one line from the server; display and quit.
			System.out.println(" Received: " + r.readLine()) ;

			connection.close();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}
}
