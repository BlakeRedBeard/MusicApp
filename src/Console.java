import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.TextArea;
import javax.swing.JTextField;
import java.awt.Button;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;

public class Console {

	private JFrame frame;
	private JTextField txtCommandline;
	private Thread t1;
	private Server server;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Console window = new Console();
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
	public Console() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.server = null;
		frame = new JFrame("MusicApp console manager");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 10, 414, 167);
		frame.getContentPane().add(textArea);
		
		PrintStream printer = new PrintStream(System.out) {
			private TextArea textarea = textArea;
			@Override
			public void println(String string) {
				textarea.append(string+'\n');
			}
			
			@Override
			public void print(String string) {
				textarea.append(string);
			}
		};
		
		txtCommandline = new JTextField();
		txtCommandline.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyCode() == 10 || arg0.getKeyChar() == '\n')
				{
					//TODO IMPLEMENTARE COMANDI DA COMMANDLINE
					if(txtCommandline.getText().toLowerCase().equals("clear") || txtCommandline.getText().toLowerCase().equals("cls"))
					{
						txtCommandline.setText("");
						textArea.setText("");
					}
					
					if(txtCommandline.getText().toLowerCase().equals("start") || txtCommandline.getText().toLowerCase().equals("startserver"))
						if(server == null)
						{
							server = new Server(printer);
							t1 = new Thread(server);
							t1.start();
							textArea.append("il server e' stato avviato\n");
						}
						else
							textArea.append("il server e' gia' in esecuzione\n");
					
					if(txtCommandline.getText().toLowerCase().equals("stop") || txtCommandline.getText().toLowerCase().equals("stopserver"))
						if(server != null)
						{
							server.kill();
							
							t1 = null;
							server = null;
							textArea.append("il server non e' piu' in esecuzione\n");
						}
						else
							textArea.append("il server e' gia' stato fermato\n");

				
				}
			}
		});
		txtCommandline.setBounds(10, 183, 270, 20);
		frame.getContentPane().add(txtCommandline);
		txtCommandline.setColumns(10);
		
		JButton btnStopserver = new JButton("stopServer");
		btnStopserver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO STOP_SERVER
				if(server != null)
				{
					
					server.kill();
					
					t1 = null;
					server = null;
					textArea.append("il server non e' piu' in esecuzione\n");
				}
				else
					textArea.append("il server e' gia' stato fermato\n");
			}
		});
		btnStopserver.setBounds(300, 217, 114, 23);
		frame.getContentPane().add(btnStopserver);
		
		JButton btnStartserver = new JButton("startServer");
		btnStartserver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TODO START_SERVER
				if(server == null)
				{
					server = new Server(printer);
					t1 = new Thread(server);
					t1.start();
					textArea.append("il server e' stato avviato\n");
				}
				else
					textArea.append("il server e' gia' in esecuzione\n");
			}
		});
		btnStartserver.setBounds(300, 183, 114, 23);
		frame.getContentPane().add(btnStartserver);
		
		
	}
}
