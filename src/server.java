import java.io.*;
import java.net.*;


public class server {

	private Socket connection;
	private ServerSocket serverSocket;
	private int port;
	
	public server() {
		this.connection = null;
		this.port = 2440;
		
		startServer();
	}
	
	
	
	public server(int port) {
		this.connection = null;
		this.port = port;
		
		startServer();
	}
	
	
	private void startServer() {
		
		
		InputStreamReader inputStream;  //stream per il trasferimento di dati con il server
	    BufferedReader buffer;          //buffer per il trasferimento di dati con il server

	    OutputStream output;            //output per invio dati al server
	    PrintWriter printer;            //contiene i metodi necessari per inviare i messaggi

	    String message;                 //stringa per invio e ricezione messaggi
		
		try
		{
			this.serverSocket = new ServerSocket(this.port);
			
			
			this.connection = this.serverSocket.accept();
			
			
			
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	
	
}
