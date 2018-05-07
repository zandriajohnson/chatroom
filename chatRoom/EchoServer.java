import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class EchoServer
{
	public static void main(String args[])
	{
		try
		{
			ServerSocket echoServer = new ServerSocket(13892);
			//Try not to use port number < 2000. 
			
			while (true)
			{
				Socket s = echoServer.accept();
			
				System.out.print("My chat room client. Version One.");
				
				BufferedReader ins = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintStream outs = new PrintStream(s.getOutputStream());

				File file = new File("userpwd.txt");
 
  				//BufferedReader br = new BufferedReader(new FileReader(file));
				
				Boolean loggedIn = false;
				Boolean userFound = false;
				String username = null;
				int i = 0;
				int count = 1;
		
					String inputLine, outputLine = null;

					while ((inputLine = ins.readLine()) != null) {
						//System.out.println(inputLine);
						if(inputLine.startsWith("login")){
							//Boolean userFound = false;
							//System.out.println("login");
							if(loggedIn == true){
								outs.println("A user is signed in. Please logout first.");
							}
							String st;
							String text = inputLine.substring(6);
							BufferedReader br = new BufferedReader(new FileReader(file));
							while ((st = br.readLine()) != null){
								//System.out.println(st);
								Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(st);
								 while(m.find()) {
									//System.out.println("hmm");	
								//System.out.println(m.group(1));   
									String[] words = m.group(1).split(", ");
									String[] splitByWords = text.split(" ");
									username = splitByWords[0];
									//System.out.println(username);
									i = 0;
									count = 1;
									for(String w:words){  
										
										//System.out.println(w); 
										//System.out.println(splitByWords[i]); 
										if (w.matches(splitByWords[i]) && count == 1)
										{
											//System.out.println("checking password now"); 
										//continue;	 
										} else if (w.matches(splitByWords[i]) && count == 2)  {
											outputLine = "Server: " + username + " joins.";
											System.out.println(username + " login.");
											loggedIn = true;
											outs.println(outputLine);
											userFound = true;
											
										}
										
									i++;
									count++;
									}
									
								
								}
							}

						
					} else if (inputLine.startsWith("send")){
							if(loggedIn == true){
							outputLine = sendToClient(username, loggedIn, inputLine);
							outs.println(outputLine);
							} else {
								outputLine = "Denied. Please login first.";
								outs.println(outputLine);
							}
						} else if(inputLine.startsWith("logout")){
							outputLine = logOut(username, loggedIn);
							outs.println(outputLine);
							loggedIn = false;
							userFound = false;
							//echoServer.accept();
							//br.close();
							//s.close();
							//System.out.println("\nClient Closed.");
						} else if(inputLine.startsWith("newuser")){
							System.out.println("\n--- New User ---");
							outs.println("Enter a username: ");
							BufferedWriter bw = null;
							try{
								String user = ins.readLine();
								System.out.println("Username entered: " + user);

								if(user.length() <= 32 && user != null){
									outs.println("Enter a password: ");
									String pwd = ins.readLine();
									if(pwd.length() <= 8 && pwd.length() >= 4 ){
										System.out.println("Password entered: " + pwd);
										bw = new BufferedWriter(new FileWriter(file, true));
										bw.write("(" + user + ", " + pwd + ")");
										bw.newLine();	
									} else {
										outs.println("Pwd should be between 4 and 8 characters: ");
										pwd = ins.readLine();
										System.out.println("Password entered: " + pwd);
										bw = new BufferedWriter(new FileWriter(file, true));
										bw.write("(" + user + ", " + pwd + ")");
										bw.newLine();	
										//outs.println(ob);
									}
								} else {
									outs.println("Username/Password is invalid. Start over.");
								}
							} catch (IOException e) {
								//exception handling left as an exercise for the reader
								System.out.println("This isnt working");
							} finally {                       // always close the file
								if (bw != null) {
									bw.flush();
									bw.close();	
								}
							} 
							outs.println("Server: New User created.");
							System.out.println("New User created.");
						} else {
							outputLine = "Invalid command.";
							outs.println(outputLine);
						}
					}
		
		}
	}
		catch (IOException e)
		{
			System.out.println(e);
		}
	}
	
	public static String logOut(String username, Boolean loggedIn){
		if(loggedIn != true){
			//outs.println("Server: You are not logged in.");
			System.out.println("You are not logged in.");
			return "Server: You are not logged in.";
		} else {
			//outs.println("Server: "+ username + " left.");
			System.out.println(username + " logout.");
			return "Server: "+ username + " left.";
		}
	}

	public static String sendToClient(String username, Boolean loggedIn, String line){
		if(loggedIn != true){
			//outs.println("Server: Denied. Please login first. ");
			return "Server: Denied. Please login first. ";
		} else {
			String text = line.substring(5);
			//outs.println(username + ": " + text);//add user
			System.out.println(username + ": " + text);
			return username + ": " + text;
		}
	}
}

