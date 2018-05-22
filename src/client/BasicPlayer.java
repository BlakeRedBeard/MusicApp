package client;

import java.io.File;
import java.util.Scanner;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class BasicPlayer {
	private MediaPlayer mediaPlayer;
	private String path;
	private JFXPanel starter;
	
	public BasicPlayer() {
		JFXPanel starter = new JFXPanel();
	}
	
	public BasicPlayer(String path) {
		JFXPanel starter = new JFXPanel();
		changeTrack(path);
	}
	
	public void changeTrack(String path) {
		this.path = path;
		Media hit = new Media(new File(path).toURI().toString());
		this.mediaPlayer = new MediaPlayer(hit);
	}
	
	public void start() {
		this.mediaPlayer.play();
	}
	
	public void stop() {
		this.mediaPlayer.setStartTime(Duration.minutes(this.getCurrentTime()));
		this.mediaPlayer.stop();
	}
	
	public double getDuration() {
		return this.mediaPlayer.getTotalDuration().toMinutes();
	}
	
	public double getCurrentTime() {
		return this.mediaPlayer.getCurrentTime().toMinutes();
	}
	
	public void setVolume(double volume) {
		this.mediaPlayer.setVolume(volume);
	}
	
	public void pause() {
		this.mediaPlayer.pause();
	}
	
	public String getStatus() {
		return this.mediaPlayer.getStatus().toString();
	}
	
	public static void main(String[] Args) throws InterruptedException {
		BasicPlayer player = new BasicPlayer("res/ilSolitoSesso.mp3");
		player.start();

		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		player.stop();
		
	}
}
