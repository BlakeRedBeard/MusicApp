package testing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;

public class Receiver {

	private JFrame frame;
	private Socket connection;
	private String server;
	private int port = 2345;
	private InputStreamReader 	sender, // Stream di input da tastiera
								mahamf; // Stream di input dal server

	private BufferedReader  buffer; // buffer per lo stream del server

	private OutputStream out; // stream di connessione con il server

	private PrintWriter printer;
	

	public static void main(String[] Args) {
		Receiver receiver = new Receiver();
		try {
			receiver.connection = new Socket(receiver.server, receiver.port);
			System.out.println("connessione stabilita...");
			receiver.mahamf = new InputStreamReader(receiver.connection.getInputStream());
			receiver.buffer = new BufferedReader(receiver.mahamf);

			receiver.out = receiver.connection.getOutputStream();
			receiver.printer = new PrintWriter(receiver.out);
			
			receiver.printer.println("send");
			receiver.printer.flush();
			
			while(receiver.buffer.ready())
			{
				System.out.println(receiver.buffer.readLine());
			}

			
			receiver.printer.println("die");
			receiver.printer.flush();
			
			receiver.printer.close();
			receiver.out.close();
			receiver.buffer.close();
			receiver.mahamf.close();
			receiver.connection.close();
			
			
		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1); // termina il programma restituendo un errore
		}
	}
}
