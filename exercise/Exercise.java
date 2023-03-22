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
        
        System
    }


    public void displayCommon( String arg1, String arg2)
    {

    }
}