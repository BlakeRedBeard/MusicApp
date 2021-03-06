import java.io.*;
import java.net.*;
import java.util.StringTokenizer;


public class Server implements Runnable{

	private Socket connection;
	private ServerSocket serverSocket;
	private Tree tree;
	private int port;
	private boolean isAlive, clientIsAlive;
	
	public Server() {
		this.connection = null;
		this.port = 2345;
		this.isAlive = true;
		this.clientIsAlive = true;
		this.tree = new Tree("mmdb");
		this.tree.createHierarchy();
		
		//startServer();
	}
	
	public Server(PrintStream out) {
		this.connection = null;
		this.port = 2345;
		System.setOut(out);
		this.isAlive = true;
		this.clientIsAlive = true;
		this.tree = new Tree("mmdb");
		this.tree.createHierarchy();
		//startServer();
	}
	
	public Server(PrintStream out, boolean isAlive, boolean clientIsAlive) {
		this.connection = null;
		this.port = 2345;
		System.setOut(out);
		this.isAlive = isAlive;
		this.clientIsAlive = clientIsAlive;
		this.tree = new Tree("mmdb");
		this.tree.createHierarchy();
		//startServer();
	}
	
	public Server(int port) {
		this.connection = null;
		this.port = port;
		this.isAlive = true;
		this.clientIsAlive = true;
		this.tree = new Tree("mmdb");
		this.tree.createHierarchy();
		//startServer();
	}
	
/**** DEPRECATED****	
	private void startServer() {
		
		
		InputStreamReader inputStream;  //stream per il trasferimento di dati con il server
	    BufferedReader buffer;          //buffer per il trasferimento di dati con il server

	    OutputStream output;            //output per invio dati al server
	    PrintWriter printer;            //contiene i metodi necessari per inviare i messaggi

	    String message;                 //stringa per invio e ricezione messaggi
	    
		try
		{
			this.serverSocket = new ServerSocket(this.port);
			
			while(true)
			{
				this.connection = this.serverSocket.accept();	//accept the connection request
				this.isAlive = true;
				inputStream = new InputStreamReader(this.connection.getInputStream());	//input stream connected with client
				buffer = new BufferedReader(inputStream);
				
				output = this.connection.getOutputStream();	//output stream connected with client
				printer = new PrintWriter(output);	//permits communication
				while(this.isAlive)
				{
					message = buffer.readLine();	//waits for the command to be performed
					StringTokenizer stkn = new StringTokenizer(message, ";");
					if(stkn.hasMoreTokens())
					{
						switch (stkn.nextToken().toLowerCase()) {
						case "die":
							this.isAlive = false;
							printer.println("I'm dead right now");
							printer.flush();
							break;
	
						case "isartist":
							printer.println(this.isArtist(stkn.nextToken()));
							printer.flush();
							break;
	
						case "isalbum":
							printer.println(this.isAlbum(stkn.nextToken()));
							printer.flush();
							break;
							
						case "istrack":
							printer.println(this.isTrack(stkn.nextToken()));
							printer.flush();
							break;
							
						case "getartists":
							printer.println(this.getArtists());
							printer.flush();
							break;
							
						case "getalbums":
							printer.println(this.getAlbums(stkn.nextToken()));
							printer.flush();
							break;
							
						case "gettracks":
							printer.println(this.getTracks(stkn.nextToken()));
							printer.flush();
							break;
							
						case "backalbums":
							printer.println(this.backAlbums(stkn.nextToken()));
							printer.flush();
							break;
							
						case "sendsong": 
							String nome = stkn.nextToken();
							String absolutePath = getTrackPath(nome);
							if(absolutePath != null || absolutePath != "" || absolutePath != "null")
							{
								System.out.println("a client downloaded : "+nome+": from : "+ absolutePath);
								File file = new File(absolutePath);
								byte[] song = new byte[(int) file.length()];
								FileInputStream input = new FileInputStream(file);
								BufferedInputStream bis = new BufferedInputStream(input);
                                bis.read(song, 0, song.length);
                                DataOutputStream dos = new DataOutputStream(output);
                                dos.writeInt(song.length);
                                if(song.length > 0)
                                	dos.write(song);
							}
							break;
						}
					}
				
				}
				
			}
		}
		catch(SocketException e) {
			try {
				this.serverSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			startServer();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			startServer();
		}
		
	}
	
	*/
	private String getArtists() {
		return this.tree.getArtists();
	}
	
	private String getAlbums(String artist) {
		return this.tree.getAlbums(artist);
	}
	
	private String getTracks(String album) {
		return this.tree.getTracks(album);
	}
	
	private String backAlbums(String track) {
		return this.tree.backAlbums(track);
	}
	
	private String isArtist(String artist) {
		if(this.tree.isArtist(artist))
			return "true";
		
		return "false";
	}
	
	private String isAlbum(String album) {
		if(this.tree.isAlbum(album))
			return "true";
		return "false";
	}
	
	private String isTrack(String track) {
		if(this.tree.isTrack(track))
			return "true";
		return "false";
	}
	
	private String getTrackPath(String songName) {
		String absolutePath;
		
		if((absolutePath = this.tree.getTrackPath(songName.toLowerCase())) != null)
		{
			return absolutePath;
		}
		return null;
	}
	
	public void kill() {
		try {
			this.isAlive = false;
			this.clientIsAlive = false;
			if(this.serverSocket != null)
				this.serverSocket.close();
			if(this.connection != null)
				this.connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		InputStreamReader inputStream;  //stream per il trasferimento di dati con il server
	    BufferedReader buffer;          //buffer per il trasferimento di dati con il server

	    OutputStream output;            //output per invio dati al server
	    PrintWriter printer;            //contiene i metodi necessari per inviare i messaggi

	    String message;                 //stringa per invio e ricezione messaggi
	    
		try
		{
			this.serverSocket = new ServerSocket(this.port);
			
			while(this.isAlive)
			{
				this.connection = this.serverSocket.accept();	//accept the connection request
				
				this.clientIsAlive = true;
				inputStream = new InputStreamReader(this.connection.getInputStream());	//input stream connected with client
				buffer = new BufferedReader(inputStream);
				
				output = this.connection.getOutputStream();	//output stream connected with client
				printer = new PrintWriter(output);	//permits communication
				while(this.clientIsAlive)
				{
					message = buffer.readLine();	//waits for the command to be performed
					StringTokenizer stkn = null;
					System.out.println("messaggio - "+message);
					if(message != null)
						stkn = new StringTokenizer(message, ";");
					if(stkn != null && stkn.hasMoreTokens())
					{
						switch (stkn.nextToken().toLowerCase()) {
						case "die":
							this.clientIsAlive = false;
							break;
	
						case "isartist":
							printer.println(this.isArtist(stkn.nextToken()));
							printer.flush();
							break;
	
						case "isalbum":
							printer.println(this.isAlbum(stkn.nextToken()));
							printer.flush();
							break;
							
						case "istrack":
							printer.println(this.isTrack(stkn.nextToken()));
							printer.flush();
							break;
							
						case "getartists":
							printer.println(this.getArtists());
							printer.flush();
							break;
							
						case "getalbums":
							printer.println(this.getAlbums(stkn.nextToken()));
							printer.flush();
							break;
							
						case "gettracks":
							printer.println(this.getTracks(stkn.nextToken()));
							printer.flush();
							break;
							
						case "backalbums":
							printer.println(this.backAlbums(stkn.nextToken()));
							printer.flush();
							break;
							
						case "sendsong": 
							String nome = stkn.nextToken();
							String absolutePath = getTrackPath(nome);
							if(absolutePath != null || absolutePath != "" || absolutePath != "null")
							{
								System.out.println("a client downloaded : "+nome+": from : "+ absolutePath);
								File file = new File(absolutePath);
								byte[] song = new byte[(int) file.length()];
								FileInputStream input = new FileInputStream(file);
								BufferedInputStream bis = new BufferedInputStream(input);
                                bis.read(song, 0, song.length);
                                DataOutputStream dos = new DataOutputStream(output);
                                dos.writeInt(song.length);
                                if(song.length > 0)
                                	dos.write(song);
                                
                                input.close();
                                bis.close();
                                dos.close();
							}
							break;
						}
					}
				
				}
				
				inputStream.close();
				buffer.close();
				output.close();
				printer.close();
				
			}

			
		}
		catch(SocketException e) {
			try {
				e.printStackTrace();
				if(this.serverSocket != null)
				this.serverSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
				e1.printStackTrace();
				/*Thread t1 = new Thread(new Server(System.out, this.isAlive, this.clientIsAlive));
				t1.start();*/
			}
			
		}		
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			e.printStackTrace();
			/*Thread t1 = new Thread(new Server(System.out, this.isAlive, this.clientIsAlive));
			t1.start();*/
		}
		
	}
	
	
	
	
}
