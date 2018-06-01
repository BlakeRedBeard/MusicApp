package client;

import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;

public class timeModifier implements Runnable {

	private BasicPlayer player;
	private JSlider slider;
	public static boolean isAlive;
	private double duration;
	private JLabel currentTime;
	
	public timeModifier(BasicPlayer player, JSlider slider) {
		this.player = player;
		this.slider = slider;
		this.isAlive = true;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		duration = this.player.getDuration();
		int minutes = (int) duration;
		int seconds = (int)((duration - minutes)*60);
		Hashtable positions = new Hashtable();
		currentTime = new JLabel("0:00");
		positions.put(0, currentTime);
			positions.put(100, new JLabel(Integer.toString(minutes)+":" +((seconds >= 10) ? seconds : "0"+seconds)));
		slider.setLabelTable(positions);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		double current;
		int minutes, seconds;
		while(isAlive)
		{
			current = this.player.getCurrentTime();
			minutes = (int) current;
			seconds = (int)((current - minutes)*60);
			currentTime.setText(minutes + ":" +((seconds >= 10) ? seconds : "0"+seconds));
			//setting the current position of the song
			slider.setValue((int)(this.player.getCurrentTime()/duration*100));
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.run();
			}
		}
		
	}


}
