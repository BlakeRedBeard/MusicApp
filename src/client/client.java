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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
//TODO cancel the class from here and then replace it's calls into client
public class client {

	private JFrame frame;
	private Socket connection;
	private String server;
	private int port;
	private InputStreamReader 	receiver; // Stream di input dal server

	private BufferedReader  buffer; // buffer per lo stream del server

	private OutputStream out; // stream di connessione con il server
	private BasicPlayer player;
	private PrintWriter printer;
	private static String path;
	private JTextField txtServerAddress;
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
		path = null;
		this.player = new BasicPlayer();
		this.StartClient();		
		initialize();
		
	}

	
	public client(int port) {
		
		//TODO add remote server
		this.server = "localhost";
		this.port = port;
		path = null;
		this.StartClient();
		initialize();
		
	}
	
	public client(int port, String server) {

		// TODO add remote server
		this.server = server;
		this.port = port;
		path = null;
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
		
		list.setBounds(10, 11, 286, 148);
		frame.getContentPane().add(list);
		
		JButton btnPre = new JButton("<<");
		btnPre.setActionCommand("previous");
		btnPre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnPre.setBounds(10, 170, 89, 23);
		frame.getContentPane().add(btnPre);
		
		JButton btnPlay = new JButton("\u25BA");
		btnPlay.setActionCommand("play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getActionCommand().equals("play"))
					player.start();
				else if(arg0.getActionCommand().equals("pause"))
					player.stop();
			}
		});
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(btnPlay.getText() == "\u25BA")
				{
					btnPlay.setText("\u2590 \u258C");
					btnPlay.setActionCommand("pause");
				}
				else
				if(btnPlay.getText() == "\u2590 \u258C")
				{
					btnPlay.setText("\u25BA");
					btnPlay.setActionCommand("play");
				}
			}
		});
		btnPlay.setBounds(109, 170, 89, 23);
		frame.getContentPane().add(btnPlay);
		
		JButton btnNext = new JButton(">>");
		btnNext.setActionCommand("next");
		btnNext.setBounds(208, 170, 89, 23);
		frame.getContentPane().add(btnNext);
		
		JButton btnStart = new JButton("Start Reproduction");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				killTemp();
				path = requestSong(list.getSelectedValue().toString());
				player.changeTrack(path);
			}
		});
		btnStart.setActionCommand("Start");
		btnStart.setBounds(10, 204, 174, 23);
		frame.getContentPane().add(btnStart);
		
		JButton btnBack = new JButton("back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
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
		btnBack.setBounds(208, 204, 89, 23);
		frame.getContentPane().add(btnBack);
		
		txtServerAddress = new JTextField();
		txtServerAddress.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(txtServerAddress.getText().equals("inserire l'indirizzo ip"))
					txtServerAddress.setText("");
			}
		});
		txtServerAddress.setText("Inserire l'indirizzo ip");
		txtServerAddress.setBounds(306, 11, 128, 23);
		frame.getContentPane().add(txtServerAddress);
		txtServerAddress.setColumns(10);
		
		JButton btnConnect = new JButton("connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				server = txtServerAddress.getText();
				if(server.equals("Inserire l'indirizzo ip"))
					server = "localhost";
				StartClient();
				setConnectionsNeeded(list);
			}
		});
		btnConnect.setBounds(306, 45, 128, 23);
		frame.getContentPane().add(btnConnect);
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
	
	private String requestSong(String songName) {
		try
		{
			printer.println("sendSong;"+songName);
			printer.flush();
			File song = new File(File.createTempFile(songName, ".tmp").getAbsolutePath());
			byte[] bytes = new byte[4096];
			FileOutputStream file = new FileOutputStream(song);
			
			while(connection.getInputStream().available() != 0) {
				connection.getInputStream().read(bytes);
				file.write(bytes);
			}
			file.close();
			return song.getAbsolutePath();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void killTemp() {
		
		if(path != null)
		{
			File f = new File(path);
			if(f.exists())
				f.delete();
			path = null;
		}
	}
	
	private void setConnectionsNeeded(JList list) {
		
		list.setModel(new AbstractListModel() {
			String[] values = getArtists();
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		
	}
	
	private void closeConnection() {
		try {
			this.printer.print("die");
			this.printer.flush();
			this.printer.close();
		
			this.out.close();
			this.buffer.close();
			this.receiver.close();
			this.connection.close();
			killTemp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
}
