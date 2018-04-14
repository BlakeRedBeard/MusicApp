import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.TextArea;
import javax.swing.JTextField;
import java.awt.Button;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class console {

	private JFrame frame;
	private JTextField txtCommandline;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					console window = new console();
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
	public console() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("MusicApp console manager");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		TextArea textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 10, 414, 167);
		frame.getContentPane().add(textArea);
		
		txtCommandline = new JTextField();
		txtCommandline.setBounds(10, 183, 270, 20);
		frame.getContentPane().add(txtCommandline);
		txtCommandline.setColumns(10);
		
		JButton btnStopserver = new JButton("stopServer");
		btnStopserver.setBounds(300, 217, 114, 23);
		frame.getContentPane().add(btnStopserver);
		
		JButton btnStartserver = new JButton("startServer");
		btnStartserver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.append("that's the number: " + Math.random() + "\n");
			}
		});
		btnStartserver.setBounds(300, 183, 114, 23);
		frame.getContentPane().add(btnStartserver);
	}
}
