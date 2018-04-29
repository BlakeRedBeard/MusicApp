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

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//TODO cancel the class from here and then replace it's calls into client
public class client {

	private JFrame frame;
	private Socket connection;
	private String server;
	private int port;
	private Tree tree;
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
		this.connection = null;
		//TODO add remote server
		this.server = "localhost";
		this.port = 2440;
		tree = new Tree("mmdb");
		
		tree.createHierarchy();
		
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
					if(tree.isArtist(list.getSelectedValue().toString()))
						list.setModel(new AbstractListModel() {
							String[] values = tree.getAlbums(list.getSelectedValue().toString());
							public int getSize() {
								return values.length;
							}
							public Object getElementAt(int index) {
								return values[index];
							}
						});
					else if(tree.isAlbum(list.getSelectedValue().toString()))
						list.setModel(new AbstractListModel() {
							String[] values = tree.getTracks(list.getSelectedValue().toString());
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
			String[] values = tree.getArtists();
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
				if(tree.isAlbum(list.getModel().getElementAt(0).toString()))
					list.setModel(new AbstractListModel() {
						String[] values = tree.getArtists();
						public int getSize() {
							return values.length;
						}
						public Object getElementAt(int index) {
							return values[index];
						}
					});
				else if(tree.isTrack(list.getModel().getElementAt(0).toString()))
					list.setModel(new AbstractListModel() {
						String[] values = tree.backAlbums(list.getModel().getElementAt(0).toString());
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
		InputStreamReader sender, // Stream di input da tastiera
				receiver; // Stream di input dal server

		BufferedReader keyboard, // buffer per l'input da tastiera
				server; // buffer per lo stream del server

		OutputStream out; // stream di connessione con il server

		PrintWriter printer;
		String msgDaInviare;
		String msgRicevuto;
		
		try
		{
			this.connection = new Socket(this.server, this.port);
			System.out.println("connessione stabilita, scrivere il nome da cercare nella rubrica...");
			
		}
		catch(IOException e)
		{
			System.out.println(e);
			System.exit(-1);		//termina il programma restituendo un errore
		}
		
		
	}
}
