package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//TODO cancel the class from here and then replace it's calls into client
public class client {

	private JFrame frame;
	private Socket connection;
	private String server;
	private int port;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					client window = new client();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public client() {
		//TODO add remote server
		this.server = "localhost";
		this.port = 2345;
		try {
			this.connection = new Socket(this.server, this.port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initialize();
		
	}

	
	public client(int port) {
		
		//TODO add remote server
		this.server = "localhost";
		this.port = port;
		try {
			this.connection = new Socket(this.server, this.port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initialize();
		
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JList list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2 && !e.isConsumed())
				{
					e.consume();
					if(isArtist(list.getSelectedValue().toString()))
						list.setModel(new AbstractListModel() {
							String[] values = getAlbums(list.getSelectedValue().toString());
							public int getSize() {
								return values.length;
							}
							public Object getElementAt(int index) {
								return values[index];
							}
						});
					else if(isAlbum(list.getSelectedValue().toString()))
						list.setModel(new AbstractListModel() {
							String[] values = getTracks(list.getSelectedValue().toString());
							public int getSize() {
								return values.length;
							}
							public Object getElementAt(int index) {
								return values[index];
							}
						});
					
				}
			}
		});
		list.setModel(new AbstractListModel() {
			String[] values = getArtists();
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.setBounds(10, 11, 338, 148);
		frame.getContentPane().add(list);
		
		JButton btnPre = new JButton("<<");
		btnPre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnPre.setBounds(10, 170, 89, 23);
		frame.getContentPane().add(btnPre);
		
		JButton btnPlay = new JButton("\u25BA");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnPlay.getText() == "\u25BA")
					btnPlay.setText("\u2590 \u258C");
				else
				if(btnPlay.getText() == "\u2590 \u258C")
					btnPlay.setText("\u25BA");
			}
		});
		btnPlay.setBounds(109, 170, 89, 23);
		frame.getContentPane().add(btnPlay);
		
		JButton btnNext = new JButton(">>");
		btnNext.setBounds(208, 170, 89, 23);
		frame.getContentPane().add(btnNext);
		
		JButton btnStart = new JButton("Start Reproduction");
		btnStart.setBounds(55, 204, 197, 23);
		frame.getContentPane().add(btnStart);
		
		JButton btnBack = new JButton("back");
		btnBack.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(isAlbum(list.getModel().getElementAt(0).toString()))
					list.setModel(new AbstractListModel() {
						String[] values = getArtists();
						public int getSize() {
							return values.length;
						}
						public Object getElementAt(int index) {
							return values[index];
						}
					});
				else if(isTrack(list.getModel().getElementAt(0).toString()))
					list.setModel(new AbstractListModel() {
						String[] values = backAlbums(list.getModel().getElementAt(0).toString());
						public int getSize() {
							return values.length;
						}
						public Object getElementAt(int index) {
							return values[index];
						}
					});
			}
		});
		btnBack.setBounds(298, 204, 89, 23);
		frame.getContentPane().add(btnBack);
	}
	
	
	private void StartClient() {

		try {
			this.connection = new Socket(this.server, this.port);
			System.out.println("connessione stabilita...");

		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1); // termina il programma restituendo un errore
		}

	}
	
	private boolean isArtist(String artist) {
		InputStreamReader sender, // Stream di input da tastiera
				receiver; // Stream di input dal server

		BufferedReader keyboard, // buffer per l'input da tastiera
				server; // buffer per lo stream del server

		OutputStream out; // stream di connessione con il server

		PrintWriter printer;
		String message;

		try {
			receiver = new InputStreamReader(this.connection.getInputStream());
			server = new BufferedReader(receiver);

			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);

			printer.println("isArtist;"+artist);
			printer.flush();
			message = server.readLine();
			if(message.toLowerCase().equals("true"))
				return true;
			else if(message.toLowerCase().equals("false"))
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean isAlbum(String album) {
		InputStreamReader sender, // Stream di input da tastiera
				receiver; // Stream di input dal server

		BufferedReader keyboard, // buffer per l'input da tastiera
				server; // buffer per lo stream del server

		OutputStream out; // stream di connessione con il server

		PrintWriter printer;
		String message;

		try {
			receiver = new InputStreamReader(this.connection.getInputStream());
			server = new BufferedReader(receiver);

			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);

			printer.println("isAlbum;"+album);
			printer.flush();
			message = server.readLine();
			if(message.toLowerCase().equals("true"))
				return true;
			else if(message.toLowerCase().equals("false"))
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean isTrack(String track) {
		InputStreamReader sender, // Stream di input da tastiera
				receiver; // Stream di input dal server

		BufferedReader keyboard, // buffer per l'input da tastiera
				server; // buffer per lo stream del server

		OutputStream out; // stream di connessione con il server

		PrintWriter printer;
		String message;

		try {
			receiver = new InputStreamReader(this.connection.getInputStream());
			server = new BufferedReader(receiver);

			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);

			printer.println("isTrack;"+track);
			printer.flush();
			message = server.readLine();
			if(message.toLowerCase().equals("true"))
				return true;
			else if(message.toLowerCase().equals("false"))
				return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private String[] getArtists() {
		InputStreamReader sender, // Stream di input da tastiera
				receiver; // Stream di input dal server

		BufferedReader keyboard, // buffer per l'input da tastiera
				server; // buffer per lo stream del server

		OutputStream out; // stream di connessione con il server

		PrintWriter printer;
		String message;
		
		try {
			receiver = new InputStreamReader(this.connection.getInputStream());
			server = new BufferedReader(receiver);
			
			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);
			
			printer.println("getArtists;");
			printer.flush();
			message = server.readLine();
			StringTokenizer stkn = new StringTokenizer(message, ";");
			String[] result = new String[stkn.countTokens()];
			
			for(int i=0; stkn.hasMoreTokens(); i++)
				result[i] = stkn.nextToken();
			
			return result;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String[] getAlbums(String artist) {
		InputStreamReader sender, // Stream di input da tastiera
				receiver; // Stream di input dal server

		BufferedReader keyboard, // buffer per l'input da tastiera
				server; // buffer per lo stream del server

		OutputStream out; // stream di connessione con il server

		PrintWriter printer;
		String message;
		
		try {
			receiver = new InputStreamReader(this.connection.getInputStream());
			server = new BufferedReader(receiver);
			
			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);
			
			printer.println("getAlbums;"+artist);
			printer.flush();
			message = server.readLine();
			StringTokenizer stkn = new StringTokenizer(message, ";");
			String[] result = new String[stkn.countTokens()];
			
			for(int i=0; stkn.hasMoreTokens(); i++)
				result[i] = stkn.nextToken();
			
			return result;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String[] getTracks(String album) {
		InputStreamReader sender, // Stream di input da tastiera
				receiver; // Stream di input dal server

		BufferedReader keyboard, // buffer per l'input da tastiera
				server; // buffer per lo stream del server

		OutputStream out; // stream di connessione con il server

		PrintWriter printer;
		String message;
		
		try {
			receiver = new InputStreamReader(this.connection.getInputStream());
			server = new BufferedReader(receiver);
			
			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);
			
			printer.println("getTracks;"+album);
			printer.flush();
			message = server.readLine();
			StringTokenizer stkn = new StringTokenizer(message, ";");
			String[] result = new String[stkn.countTokens()];
			
			for(int i=0; stkn.hasMoreTokens(); i++)
				result[i] = stkn.nextToken();
			
			return result;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String[] backAlbums(String track) {
		InputStreamReader sender, // Stream di input da tastiera
				receiver; // Stream di input dal server

		BufferedReader keyboard, // buffer per l'input da tastiera
				server; // buffer per lo stream del server

		OutputStream out; // stream di connessione con il server

		PrintWriter printer;
		String message;
		
		try {
			receiver = new InputStreamReader(this.connection.getInputStream());
			server = new BufferedReader(receiver);
			
			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);
			
			printer.println("backAlbums;"+track);
			printer.flush();
			message = server.readLine();
			StringTokenizer stkn = new StringTokenizer(message, ";");
			String[] result = new String[stkn.countTokens()];
			
			for(int i=0; stkn.hasMoreTokens(); i++)
				result[i] = stkn.nextToken();
			
			return result;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
