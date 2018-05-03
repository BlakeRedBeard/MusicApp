package testing;
import javax.media.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class AudioVideoPlayer extends JFrame
{
  private static final String FRAME_TITLE = "Un semplice riproduttore audio/video";
  private static final String CONTROL_PANEL_TITLE = "Pannello di Controllo";

  // Coordinate relative alla finestra dell’applicazione
  private static final int LOC_X = 100;
  private static final int LOC_Y = 100;
  private static final int HEIGHT = 400;
  private static final int WIDTH = 400;

  private Player player = null;


  // Il componente di tipo JTabbedPane utilizzato per la 
  // visualizzazione dei controlli associati al Player istanziato
  private JTabbedPane tabPane = null;

  public AudioVideoPlayer()
  {
    super(FRAME_TITLE);
    setLocation(LOC_X, LOC_Y);
    setSize(WIDTH, HEIGHT);

    tabPane = new JTabbedPane();
    this.getContentPane().add(tabPane);

    // Creazione di una classe anonima per la gestione
    // dell’evento windowCloing
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        closePlayer(); 
        System.exit(0);
      }
    });
  }

  private JPanel createMediaPanel()
  {
    JPanel mainPnl = new JPanel();
    GridBagLayout panelLayout = new GridBagLayout();
    GridBagConstraints constr = new GridBagConstraints();

    mainPnl.setLayout(panelLayout);

    // Aggiunta del pannello ricavato dal metodo
    // getVisualComponent()
    if (player.getVisualComponent() != null)
    {   
      constr.gridx = 0;
      constr.gridy = 0;
      constr.weightx = 1;
      constr.weighty = 1;
      constr.fill = GridBagConstraints.BOTH;
      mainPnl.add(player.getVisualComponent(), constr);
    }

    // Aggiunta del pannello ricavato dal metodo 
    // getGainControl().getControlComponent() (se esistente)
    if ((player.getGainControl() != null) &&
        (player.getGainControl().getControlComponent() != null))
    {
      constr.gridx = 1;
      constr.gridy = 0;
      constr.weightx = 0;
      constr.weighty = 1;
      constr.gridheight = 2;
      constr.fill = GridBagConstraints.VERTICAL;
      mainPnl.add(player.getGainControl().getControlComponent(), constr);
    }

    // Aggiunta del pannello ricavato dal metodo
    // getControlPanelComponent()
    if (player.getControlPanelComponent() != null)
    {
      constr.gridx = 0;
      constr.gridy = 1;
      constr.weightx = 1;
      constr.gridheight = 1;

      if (player.getVisualComponent() != null)
      {
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.weighty = 0;
      }
      else
      {
        constr.fill = GridBagConstraints.BOTH;
        constr.weighty = 1;
      }

      mainPnl.add(player.getControlPanelComponent(), constr);
    }

    return mainPnl;
  }

  public void setMediaLocator(MediaLocator locator) throws IOException, 
        NoPlayerException, CannotRealizeException
  {
    setPlayer(Manager.createRealizedPlayer(locator));
  }

  public void setPlayer(Player newPlayer)
  {
    // Qualora sia presente un Player già attivo, viene chiuso
    closePlayer();

    player = newPlayer;
    if (player == null) return;

    tabPane.removeAll();
    tabPane.add(CONTROL_PANEL_TITLE, createMediaPanel());
    Control[] controls = player.getControls();
    for (int i = 0; i < controls.length; i++)
    {
      if (controls[i].getControlComponent() != null)
      {
        tabPane.add(controls[i].getControlComponent());
      }
    }
  }

  private void closePlayer()
  {
    if (player != null)
    {
      player.stop();
      player.close();
    }
  }

  public static void main(String[] args)
  {
    try
    {
      if (args.length == 1)
      {
        AudioVideoPlayer mpf = new AudioVideoPlayer(); 
        
        mpf.setMediaLocator(new MediaLocator(new File("montavit-the-hero_1.mpg").toURI().toURL())); 
        mpf.setVisible(true);
      }
      else
      {
        System.out.println("E’ necessario fornire in input il nome del " + 
              "file da riprodurre");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}