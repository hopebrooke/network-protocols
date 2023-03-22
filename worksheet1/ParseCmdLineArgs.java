//
// Simple Java code intended as a refresher for students who need to 'brush up' on their Java.
// There are no network featires in this example.
//
// Compile with: javac ParseCmdLineArgs.java
// Execute with: java ParseCmdLineArgs arg1 arg2 arg3 ...
//

public class ParseCmdLineArgs {

    private String[] clArgs;
	// The sole constructor, which expects the command line arguments to be provided as a String array.
	public ParseCmdLineArgs( String[] args )
	{
    
	    //(i) - if no CL args, print message then quit
        if ( args.length == 0 ) 
        {
            System.out.println( "No command line arguments entered..." );
            System.exit(1);
        }
    
        //(ii) - if there are CL args, store in private string array
        else 
        {
            clArgs = args;
            for ( int i=0; i<clArgs.length; i++ )
            {

                //(iii) - print out each CL arg on a separate line
                System.out.print( i + " : " + clArgs[i] );

                //(iv) - check if CL arg contains a "."
                if ( clArgs[i].contains(".") ) System.out.print( " (may be a hostname)" );

                //(v) - check if CL arg contains 3 "."s
                if ( clArgs[i].split("[.]").length == 4 ) System.out.print( " (may be an IPv4 address)" );
                
                System.out.print("\n");

            }
        }
	}

    // main(): This is the function that is called after executing with 'java ParseCmdLineArgs'.
	// Any command line arguments are passed as the string array 'String[] args', i.e. if you execute the code with
	//   java ParseCmdLineArgs arg1 arg2 arg3
	// then String[] args will be an array of length 3, containing the strings 'arg1', arg2', and arg3.'
    public static void main( String[] args )
	{
		ParseCmdLineArgs parser = new ParseCmdLineArgs(args);
	}
}