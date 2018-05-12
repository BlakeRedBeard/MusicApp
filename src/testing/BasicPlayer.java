package testing;

import java.io.File;
import java.util.Scanner;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class BasicPlayer {
	private MediaPlayer mediaPlayer;
	public BasicPlayer() {
		
	}
	
	
	public void startPlay(String location) {
		Media hit = new Media(new File(location).toURI().toString());
		mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}
	
	public void stop() {
		mediaPlayer.stop();
	}
	
	
	public static void main(String[] Args) {
		BasicPlayer player = new BasicPlayer();
		player.startPlay("Radioactive.mp3");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
		player.stop();
	}
}
