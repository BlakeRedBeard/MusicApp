package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
	private InputStreamReader 	sender, // Stream di input da tastiera
								receiver; // Stream di input dal server

	private BufferedReader  buffer; // buffer per lo stream del server

	private OutputStream out; // stream di connessione con il server

	private PrintWriter printer;
	
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
		this.StartClient();		
		initialize();
		
	}

	
	public client(int port) {
		
		//TODO add remote server
		this.server = "localhost";
		this.port = port;
		this.StartClient();
		initialize();
		
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener exitListener = new WindowAdapter() {
			private client frame;
			
			public WindowAdapter setFrame(client frame) {
				this.frame = frame;
				return this;
			}
			
		    @Override
		    public void windowClosing(WindowEvent e) {
		        int confirm = JOptionPane.showOptionDialog(
		             null, "Are You Sure to Close Application?", 
		             "Exit Confirmation", JOptionPane.YES_NO_OPTION, 
		             JOptionPane.QUESTION_MESSAGE, null, null, null);
		        if (confirm == 0) {
		           this.frame.closeConnection();
		           System.exit(0);
		        }
		    }
		}.setFrame(this);
		frame.addWindowListener(exitListener);
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
			receiver = new InputStreamReader(this.connection.getInputStream());
			buffer = new BufferedReader(receiver);

			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);

		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1); // termina il programma restituendo un errore
		}

	}
	
	private boolean isArtist(String artist) {

		String message;

		try {
			printer.println("isArtist;"+artist);
			printer.flush();
			
			message = buffer.readLine();
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
		String message;

		try {
			printer.println("isAlbum;"+album);
			printer.flush();
			message = buffer.readLine();
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
		String message;

		try {
			printer.println("isTrack;"+track);
			printer.flush();
			message = buffer.readLine();
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
		String message;
		
		try {
			printer.println("getArtists;");
			printer.flush();
			message = buffer.readLine();
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
		String message;
		
		try {
			printer.println("getAlbums;"+artist);
			printer.flush();
			message = buffer.readLine();
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
		String message;
		
		try {
			printer.println("getTracks;"+album);
			printer.flush();
			message = buffer.readLine();
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
		String message;
		
		try {
			printer.println("backAlbums;"+track);
			printer.flush();
			message = buffer.readLine();
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
	
	private void closeConnection() {
		this.printer.print("die");
		this.printer.flush();
		this.printer.close();
		try {
			this.out.close();
			this.buffer.close();
			this.receiver.close();
			this.connection.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
