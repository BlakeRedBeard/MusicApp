import java.awt.BorderLayout;
import java.awt.Container;
import java.io.*;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class console {
	
	public console() {
		
	}

	public static void main(String[] Args) {
		/*Process p;
		try {
			p = Runtime.getRuntime().exec("cmd /c start cmd.exe");
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*
		Scanner scan = new Scanner(System.in);
		String s = scan.next();
		int i = scan.nextInt();*/
		
		
		JFrame frame = new JFrame("finestra bella");
		frame.setAutoRequestFocus(false);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		Container c = frame.getContentPane();
		
		JTextArea textArea = new JTextArea(50, 10);
		c.add(textArea);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textArea));
		System.setOut(printStream);
		System.setErr(printStream);
		
		
		Scanner scan = new Scanner(System.in);
		String s = scan.next();
		int i = scan.nextInt();

		System.out.println(s);
		System.out.println(i);
	}
	
	
}
