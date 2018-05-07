import java.io.*;
import java.net.*;


public class EchoClient
{
	public static void main(String args[])
	{
		if(args.length != 1)
		{
			System.out.println("EchoClient MachineName");
		}
	
		InputStreamReader convert = new InputStreamReader(System.in);
		BufferedReader stdin = new BufferedReader(convert);
		
		try
		{
			Socket echoClient = new Socket(args[0], 13892);
			PrintStream outs = new PrintStream(echoClient.getOutputStream());
			BufferedReader ins = new BufferedReader(new InputStreamReader(echoClient.getInputStream()));
			
			System.out.print("My chat room client. Version One.\n>");
	
	
	String fromServer;
	String toServer = stdin.readLine();
	outs.println(toServer);
	
	while ((fromServer = ins.readLine()) != null) {
		
		System.out.println(fromServer);
		System.out.print(">"); 
		toServer = stdin.readLine();
		outs.println(toServer);
	
		}

		//echoClient.close();
		}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
}

