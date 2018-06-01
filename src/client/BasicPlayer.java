package client;

import java.io.File;
import java.util.Scanner;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class BasicPlayer {
	private MediaPlayer mediaPlayer;
	private String path;
	private JFXPanel starter;
	
	public BasicPlayer() {
		JFXPanel starter = new JFXPanel();
		this.mediaPlayer = null;
	}
	
	public BasicPlayer(String path) {
		JFXPanel starter = new JFXPanel();
		changeTrack(path);
	}
	
	public void changeTrack(String path) {
		this.path = path;
		Media hit = new Media(new File(path).toURI().toString());
		if(this.mediaPlayer != null)
			if(this.mediaPlayer.getStatus() == Status.PLAYING)
				this.mediaPlayer.stop();
		
		this.mediaPlayer = new MediaPlayer(hit);
		this.mediaPlayer.setVolume(50);
	}
	
	public void start() {
		if(this.mediaPlayer != null)
			this.mediaPlayer.play();
	}
	
	public void stop() {
		if(this.mediaPlayer != null)
			this.mediaPlayer.stop();
	}
	
	public double getDuration() {
		if(this.mediaPlayer != null)
			return this.mediaPlayer.getTotalDuration().toMinutes();
		return 0;
	}
	
	public double getCurrentTime() {
		if(this.mediaPlayer != null)
			return this.mediaPlayer.getCurrentTime().toMinutes();
		return 0;
	}
	
	public void setVolume(double volume) {
		if(this.mediaPlayer != null)
			this.mediaPlayer.setVolume(volume);
	}
	
	public void pause() {
		if(this.mediaPlayer != null)
			this.mediaPlayer.pause();
	}
	
	public String getStatus() {
		if(this.mediaPlayer != null)
			return this.mediaPlayer.getStatus().toString();
		return null;
	}
	
	public boolean isReady() {
		if(this.mediaPlayer != null)
			return true;
		else return false;
	}
	
	public void setTime(double time) {
		if(this.mediaPlayer != null)
		{
			stop();
			this.mediaPlayer.setStartTime(Duration.minutes(time));
			start();
		}
	}
	
	/* JUST TESTING
	public static void main(String[] Args) throws InterruptedException {
		BasicPlayer player = new BasicPlayer("res/ilSolitoSesso.mp3");
		player.start();

		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		player.stop();
		
	}*/
}
