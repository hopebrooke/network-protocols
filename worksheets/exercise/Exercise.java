import java.io.*;
import java.net.*;
import java.util.*;

public class Exercise
{
	public Exercise( String[] args )
	{

        // If there are not one or two hostnames given then exit:
        if( args.length==0 || args.length>2 )
        {
            System.out.println("Please enter one or two hostnames.");
            System.exit(-1);
        }
        if( args.length == 1 ) reportAddress( args[0] );
        if( args.length == 2 ) displayCommon( args[0], args[1] );
	}


    // If one hostname is given, convert hostname to instance of InetAddress
    // and print hostname and IP address to terminal
    public void reportAddress( String arg )
    {
        InetAddress address = null;

        try {
            address = InetAddress.getByName(arg);

        } catch ( UnknownHostException e) {
            System.err.println(arg + " is not an IP address. Error: " + e);
            System.exit(-1);
        }

        System.out.println("Host name: " + address.getHostName());
        System.out.println("IP Address: " + address.getHostAddress());
        
        System.out.println( "IP version: " + (address.getAddress().length==4?"IPV4":"IPV6"));
    }


    public void displayCommon( String arg1, String arg2)
    {
        InetAddress address1 = null;
        InetAddress address2 = null;
        
        try
		{
			address1 = InetAddress.getByName( arg1 );
			address2 = InetAddress.getByName( arg2 );
		}
		catch( UnknownHostException err )
		{
			System.err.println( "Cannot convert both of the hostnames '" + arg1 + "' and '" + arg2 + "' to IP addresses; first error was " + err );
			System.exit(-1);
		}

        		// Need both to be IPv4 address for this coursework question.
		if( address1.getAddress().length!=4 || address2.getAddress().length!=4 )
		{
			System.out.println( "One or both hostnames correspond to IPv6 addresses; cannot determine common range." );
			System.exit(-1);
		}

        int commonLevels = 0;
		while( commonLevels<4 && address1.getAddress()[commonLevels]==address2.getAddress()[commonLevels] ) commonLevels++;

		// Display common parts of the address in a.b.*.* format
		System.out.print( "Common address: " );
		for( int i=0; i<4; i++ )
		{
			// Have to be careful not to output signed integers as signed.
			System.out.print( (i<commonLevels?(address1.getAddress()[i]&0xff):"*") );

			// Dot or end of line.
			if( i<3 )
				System.out.print(".");
			else
				System.out.println();
		}
    }

    public static void main( String[] args )
	{
		Exercise cwk1 = new Exercise( args );
	}

}