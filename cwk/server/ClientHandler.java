/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler extends Thread {
	
	private Socket socket = null;
	private Server server = null;

	public ClientHandler(Socket socket, Server server) {
		super("ClientHandler");
		this.socket = socket;
		this.server = server;
	}

	public void run() {
		try {
			// Input and output streams to/from the client.
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
						new InputStreamReader(
						socket.getInputStream()));


			//Date date = new Date();
			//System.out.println("\nDate " + date.toString() );
			//System.out.println("Connection made from " + inet.getHostName() );

			// Initialise a protocol object for this client.
			// String inputLine, outputLine;
			// Protocol protocol = new Protocol();
			// outputLine = protocol.processInput(null);
			// out.println(outputLine);

			String inputArgs = in.readLine();
			// Input args is string of arguments
			// Convert to array
			String[] arrayArgs = inputArgs.split(",");

			String output;

			// Logging.
			InetAddress inet = socket.getInetAddress();
			String clientIP = inet.getHostAddress();
			
			// First check for show
			if( arrayArgs.length == 1 && arrayArgs[0].equals("show")){
				output = server.show();
				server.log(clientIP, inputArgs);
				//if( output.isEmpty()) output = "There are currently no items in this auction.";
			}
			// Check for item adding 
			else if (arrayArgs.length == 2 && arrayArgs[0].equals("item")) {
				// Check for failure/success
				if( server.addItem(arrayArgs[1]) ) output = "Success";
				else output = "Failure";
				server.log(clientIP, inputArgs);
			} 
			// Check for item bidding
			else if (arrayArgs.length == 3 && arrayArgs[0].equals("bid")) {
				// Get bid as double
				double bid = Double.parseDouble(arrayArgs[2]);
				// Get client IP
				if( server.bid( arrayArgs[1], bid, clientIP ) == 1 ) output = "Accepted";
				else if (server.bid( arrayArgs[1], bid, clientIP ) == 2) output = "Rejected";
				else output = "Failure";
				server.log(clientIP, inputArgs);

			} else {
				output = "Error, wrong format";
			}


			// try{
			// 	Thread.sleep(5000);
			// } catch (InterruptedException e){
			// 	System.out.println("problemo");
			// }
			
			out.println(output);

			// Free up resources for this connection.
			out.close();
			in.close();
			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}