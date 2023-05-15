//
// This is a commented version of the stripped-down code that was provided.
//


import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class DateServer
{
    public DateServer() throws IOException
    {
        // Create the sole selector object that will poll all channels.
        Selector selector = Selector.open();

        // Create a channel for the server socket, which will be polled to check for new client connections.
        ServerSocketChannel svrChann = ServerSocketChannel.open();
        svrChann.configureBlocking( false );        // Non-blocking IO.

        // We want to act on this channel when it is ready to accept a new client connection,
        // so the selection key is "OP_ACCEPT".
        svrChann.register( selector, SelectionKey.OP_ACCEPT );

        // Bind the socket (channel) to the listening port, 9898 in this example. This is the same as a for a normal ServerSocket.
        svrChann.socket().bind( new InetSocketAddress("localhost",9898) );

        // Infinite server loop as normal.
        while( true )
        {
            // Blocking call to select() - will only return when the selector thinks we have at least one event to act upon.
            selector.select();

            // Iterate over all keys (events) that the selector has identified as actionable.
            Iterator keys = selector.selectedKeys().iterator();
            while( keys.hasNext() )
            {
                // Get the selection key from the iterator, and remove from the list so we don't action it twice.
                SelectionKey key = (SelectionKey) keys.next();
                keys.remove();

                // Possible to have invalid keys; just skip them.
				if( !key.isValid() ) continue;

                // For this application, only one selection key is ever used - "OP_ACCEPT". Check this is the case.
                if( key.isAcceptable() )
                {
                    // Get the channel from the key; we know this is a ServerSocketChannel for this application.
					svrChann = (ServerSocketChannel) key.channel();

                    // Just as ServerSocketChannel is the non-blocking IO version of a ServerSocket, so is
                    // SocketChannel the non-blocking IO version of a Socket. Get this from the accept() method.
					SocketChannel channel = svrChann.accept();
					channel.configureBlocking(false);       // Non-blocking IO.

                    // Send the date to the client. Note we have to convert to a byte array ourselves; we can't use Streams for non-blocking I/O.
					String message = (new Date()).toString() + "\r\n";
					channel.write( ByteBuffer.wrap(message.getBytes()) );
                }
            }
        }
    }
    
    public static void main( String[] args ) throws IOException
    {
        DateServer server = new DateServer();
    }
}