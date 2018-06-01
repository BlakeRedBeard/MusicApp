package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.AbstractListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.List;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.ComponentOrientation;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
//TODO cancel the class from here and then replace it's calls into client
public class client {

	private JFrame frame;
	private Socket connection;
	private String server;
	private int port;
	private InputStreamReader 	receiver; // Stream di input dal server

	private BufferedReader  buffer; // buffer per lo stream del server

	private OutputStream out; // stream di connessione con il server
	private BasicPlayer player;	//lettore mp3
	private PrintWriter printer;	//stampa nello stream di output
	private static String path;		//percorso del file temporaneo
	private JTextField txtServerAddress;	//indirizzo logico o fisico del server
	private Thread time;
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
		initialize();
		
	}

	
	public client(int port) {
		
		//TODO add remote server
		this.server = "localhost";
		this.port = port;
		path = null;
		this.player = new BasicPlayer();
		initialize();
		
	}
	
	public client(int port, String server) {

		// TODO add remote server
		this.server = server;
		this.port = port;
		path = null;
		this.player = new BasicPlayer();
		initialize();

	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 500, 362);
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
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 336, 175);
		frame.getContentPane().add(scrollPane);
		JList list = new JList();
		scrollPane.setViewportView(list);
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
		
		JButton btnPlay = new JButton("\u25BA");
		btnPlay.setActionCommand("play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(arg0.getActionCommand().equals("play") && player.isReady())
					player.start();
				else 
					if(arg0.getActionCommand().equals("pause") && player.isReady())
						player.pause();
				
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
		btnPlay.setBounds(113, 264, 89, 23);
		frame.getContentPane().add(btnPlay);
		

		
		JButton btnStart = new JButton("Download Song");
		btnStart.setActionCommand("Start");
		btnStart.setBounds(10, 298, 174, 23);
		frame.getContentPane().add(btnStart);
		
		JButton btnPre = new JButton("<<");
		btnPre.setActionCommand("previous");
		btnPre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list.setSelectedIndex(list.getSelectedIndex()-1);
				
				btnStart.doClick();
				player.start();
			}
		});
		btnPre.setBounds(10, 264, 89, 23);
		frame.getContentPane().add(btnPre);
		
		JButton btnNext = new JButton(">>");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				list.setSelectedIndex(list.getSelectedIndex()+1);
				
				btnStart.doClick();
				player.start();
			}
		});
		btnNext.setActionCommand("next");
		btnNext.setBounds(212, 264, 89, 23);
		frame.getContentPane().add(btnNext);
		
		JButton btnBack = new JButton("back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		btnBack.setBounds(212, 298, 89, 23);
		frame.getContentPane().add(btnBack);
		
		txtServerAddress = new JTextField();
		txtServerAddress.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				if(txtServerAddress.getText().equals("Inserire l'indirizzo ip"))
					txtServerAddress.setText("");
			}
		});
		txtServerAddress.setText("Inserire l'indirizzo ip");
		txtServerAddress.setBounds(356, 9, 128, 23);
		frame.getContentPane().add(txtServerAddress);
		txtServerAddress.setColumns(10);
		
		JButton btnConnect = new JButton("connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				server = txtServerAddress.getText();
				if(server.equals("Inserire l'indirizzo ip") || server.equals(""))
					server = "localhost";
				if(StartClient())
					setConnectionsNeeded(list);
			}
		});
		btnConnect.setBounds(356, 43, 128, 23);
		frame.getContentPane().add(btnConnect);
		
		JSlider slider = new JSlider();
		slider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				player.setTime((double)slider.getValue()/100*player.getDuration());
			}
		});
		slider.setPaintTicks(true);
		slider.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		slider.setBounds(10, 197, 291, 56);
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(10);
		slider.setPaintLabels(true);
		slider.setLabelTable(null);
		frame.getContentPane().add(slider);
		
		JSlider slider_1 = new JSlider();
		slider_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if(player != null)
					player.setVolume(((double) slider_1.getValue())/100);
			}
		});
		slider_1.setPaintLabels(true);
		slider_1.setMinorTickSpacing(10);
		slider_1.setMajorTickSpacing(50);
		slider_1.setPaintTicks(true);
		slider_1.setOrientation(SwingConstants.VERTICAL);
		slider_1.setBounds(311, 197, 56, 124);
		frame.getContentPane().add(slider_1);
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				killTemp();
				path = requestSong(list.getSelectedValue().toString());
				if(path != null)
					player.changeTrack(path);
				if(time != null) {
					timeModifier.isAlive = false;
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
				
				time = new Thread(new timeModifier(player, slider));
				
				time.start();
			}
		});
	}
	
	
	private boolean StartClient() {

		try {
			if(this.connection != null)
				closeConnection();
			this.connection = new Socket(this.server, this.port);
			System.out.println("connessione stabilita...");
			receiver = new InputStreamReader(this.connection.getInputStream());
			buffer = new BufferedReader(receiver);

			out = this.connection.getOutputStream();
			printer = new PrintWriter(out);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			
		}
		
		return false;
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
			if(isTrack(songName))
			{
				printer.println("sendSong;"+songName);
				printer.flush();
				File song = new File(File.createTempFile(songName, ".tmp").getAbsolutePath());
				FileOutputStream file = new FileOutputStream(song);
				DataInputStream dis = new DataInputStream(connection.getInputStream());
				int len = dis.readInt();
				byte[] data = new byte[len];
				if(len > 0)
					dis.readFully(data);
				
				file.write(data);
				file.flush();
				String absolutePath = song.getAbsolutePath();
				file.close();
				return absolutePath;
			}
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
			killTemp();
			this.printer.print("die");
			this.printer.flush();
			this.printer.close();
			
			this.out.close();
			this.buffer.close();
			this.receiver.close();
			this.connection.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.exit(0);
		}
		
	}
}
