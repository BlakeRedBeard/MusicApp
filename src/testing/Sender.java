package testing;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class Sender {

	Socket connection;
	ServerSocket serverSocket;
	int port;
	boolean isAlive;
	
	public static void main(String[] Args) {
		Sender sender = new Sender(); 
		sender.port = 2345;
		sender.connection = null;
		sender.isAlive = true;
		InputStreamReader inputStream;  //stream per il trasferimento di dati con il server
	    BufferedReader buffer;          //buffer per il trasferimento di dati con il server

	    OutputStream output;            //output per invio dati al server
	    PrintWriter printer;            //contiene i metodi necessari per inviare i messaggi

	    String message;                 //stringa per invio e ricezione messaggi
	    
	    try {
	    File file = new File("res/ilSolitoSesso.mp3");
		byte[] song = new byte[(int) file.length()];
		FileInputStream input = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(input);
		bis.read(song, 0, song.length);
		BufferedOutputStream bos; //= new BufferedOutputStream(sender.connection.getOutputStream());
		bos = new BufferedOutputStream(System.out);
		
		bos.write(song, 0, song.length);
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
		try
		{
			sender.serverSocket = new ServerSocket(sender.port);
			
			while(true)
			{
				sender.connection = sender.serverSocket.accept();	//accept the connection request
				sender.isAlive = true;
				inputStream = new InputStreamReader(sender.connection.getInputStream());	//input stream connected with client
				buffer = new BufferedReader(inputStream);
				
				output = sender.connection.getOutputStream();	//output stream connected with client
				printer = new PrintWriter(output);	//permits communication
				while(sender.isAlive)
				{
					message = buffer.readLine();	//waits for the command to be performed
					StringTokenizer stkn = new StringTokenizer(message, ";");
					if(stkn.hasMoreTokens())
					{
						switch (stkn.nextToken().toLowerCase()) {
						case "die":
							sender.isAlive = false;
							printer.println("I'm dead right now");
							printer.flush();
							break;
	/*
						case "send":
							File file = new File("res/ilSolitoSesso.mp3");
							byte[] song = new byte[(int) file.length()];
							FileInputStream input = new FileInputStream(file);
							BufferedInputStream bis = new BufferedInputStream(input);
							bis.read(song, 0, song.length);
							BufferedOutputStream bos = new BufferedOutputStream(sender.connection.getOutputStream());
							bos = new BufferedOutputStream(System.out);
							
							bos.write(song, 0, song.length);
							break;
						*/
						}
					}
				
				}
				
			}
		}
		catch(Exception e)
		{
			
		}
	}
	
	
}
