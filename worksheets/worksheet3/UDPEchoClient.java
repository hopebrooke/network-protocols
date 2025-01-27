import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

public class UDPEchoClient {

  private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
  private static final int MAXTRIES = 5;     // Maximum retransmissions

  // Everything is main() for this demonstration; not normally regarded as good programming practice.
  public static void main(String[] args) throws IOException {

    if ((args.length < 2) || (args.length > 3))  // Test for correct # of args
      throw new IllegalArgumentException("Parameter(s): <Server> <Word> [<Port>]");

    InetAddress serverAddress = InetAddress.getByName(args[0]);  // Server address

    // Convert input String to bytes using the default character encoding
    byte[] bytesToSend = args[1].getBytes();

    int servPort = (args.length == 3) ? Integer.parseInt(args[2]) : 7777;

    DatagramSocket socket = new DatagramSocket();

    socket.setSoTimeout(TIMEOUT);  // Maximum receive blocking time (milliseconds)

    DatagramPacket sendPacket = new DatagramPacket(bytesToSend,  // Sending packet
        bytesToSend.length, serverAddress, servPort);

    DatagramPacket receivePacket =                              // Receiving packet
        new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

    int tries = 0;      // Packets may be lost, so we have to keep trying
    boolean receivedResponse = false;
    do {
      socket.send(sendPacket);          // Send the echo string
      try {
        socket.receive(receivePacket);  // Attempt echo reply reception

        if (!receivePacket.getAddress().equals(serverAddress))  // Check source
          throw new IOException("Received packet from an unknown source");

        receivedResponse = true;
      } catch (InterruptedIOException e) {  // We did not get anything
        tries += 1;
        System.out.println("Timed out, " + (MAXTRIES-tries) + " more tries...");
      }
    } while ((!receivedResponse) && (tries < MAXTRIES));

    if (receivedResponse)
      System.out.println("Received: " + new String(receivePacket.getData()));
    else
      System.out.println("No response -- giving up.");

    socket.close();
  }
}