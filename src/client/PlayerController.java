package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerController implements ActionListener{
	private BasicPlayer player;
	
	public PlayerController() {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		switch(e.getActionCommand().toLowerCase())
		{
			case "play" : player.start();
						  break;
			case "pause": player.pause();
						  break;
			case "start": break;
		}
	}
}
