import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


//TODO CANCEL, WHEN FINISHED, THE UNUSED IMPORTS
public class Tree {
	
	private Node menu;
	private DatabaseManager database;
	
	//initialize the object with an empty node
	public Tree(String dbName) {
		this.resetMenu();
		this.database = new DatabaseManager(dbName);
	}
	
	public Tree(String dbName, String user) {
		this.resetMenu();
		this.database = new DatabaseManager(dbName, user);
	}
	
	public Tree(String dbName, String user, String password) {
		this.resetMenu();
		this.database = new DatabaseManager(dbName, user, password);
	}
	
	//returns the node
	public Node getMenu() {
		return menu;
	}
	//clear the node'attributes (just set as null)
	public void resetMenu() {
		this.menu = new Node();
	}
	
	public void createHierarchy() {
		try {
			this.database.connect();
			ResultSet autori = this.database.select("SELECT codice, nome FROM autore");
			
			while(autori.next())
			{
				this.menu.addSon(null, autori.getString("nome"), autori.getString("codice"));
				//System.out.println("artista aggiunto: "+autori.getString("nome"));
				ResultSet album = this.database.select("SELECT codice, nome FROM album WHERE album.codAutore = "+autori.getString("codice"));
				ArrayList<Node> artisti = this.menu.getSons();
				for(int i=0; i<artisti.size(); i++)
				{
					if(artisti.get(i).getPrimaryKey().equals(autori.getString("codice")))
					{
						while(album.next())
						{
							artisti.get(i).addSon(null, album.getString("nome"), album.getString("codice"));
							//System.out.println("\talbum aggiunto: "+album.getString("nome"));
							ResultSet brani = this.database.select("SELECT codice, nome, locazione FROM brano WHERE brano.codAlbum = "+album.getString("codice"));
							ArrayList<Node> albums = artisti.get(i).getSons();
							for(int j=0; j<albums.size(); j++)
							{
								if(albums.get(j).getPrimaryKey().equals(album.getString("codice")))
								{
									while(brani.next())
									{
										albums.get(j).addSon(brani.getString("locazione"), brani.getString("nome"), brani.getString("codice"));
										//System.out.println("\t\tbrano aggiunto: "+brani.getString("nome"));
									}
								}
							}
							brani.close();
						}
					}
				}
				album.close();
			}
			autori.close();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	//returns a string array of the artists names
	public String getArtists() {

		if (!this.menu.getSons().isEmpty() || this.menu.getSons() != null) {
			ArrayList<Node> artists = this.menu.getSons();
			String artisti = "";

			for (int i = 0; i < artists.size(); i++)
				artisti += artists.get(i).getName()+";";

			return artisti;
		}

		return null;
	}

	// returns a string array of the albums names of the specified artist
	public String getAlbums(String artist) {

		if (!this.menu.getSons().isEmpty() || this.menu.getSons() != null) {
			for (int i = 0; i < this.menu.getSons().size(); i++) {
				if (artist.equals(this.menu.getSons().get(i).getName())) {
					String albums = "";
					for (int j = 0; j < this.menu.getSons().get(i).getSons().size(); j++)
						albums += this.menu.getSons().get(i).getSons().get(j).getName()+";";
					return albums;
				}

			}
		}

		return null;
	}

	// returns a string array of the tracks of the specified album
	public String getTracks(String album) {
		if (!this.menu.getSons().isEmpty() || this.menu.getSons() != null) {
			for (int i = 0; i < this.menu.getSons().size(); i++) {
				for (int j = 0; j < this.menu.getSons().get(i).getSons().size(); j++) {
					if (album.equals(this.menu.getSons().get(i).getSons().get(j).getName())) {
						String tracks = "";
						for (int z = 0; z < this.menu.getSons().get(i).getSons().get(j).getSons().size(); z++) {
							tracks += this.menu.getSons().get(i).getSons().get(j).getSons().get(z).getName()+";";
						}
						return tracks;
					}
				}
			}
		}

		return null;
	}
	
	// returns a string array of the tracks of the specified album
		public String backAlbums(String track) {
			if (!this.menu.getSons().isEmpty() || this.menu.getSons() != null) {
				for (int i = 0; i < this.menu.getSons().size(); i++) {
					for (int j = 0; j < this.menu.getSons().get(i).getSons().size(); j++) {
						for (int z=0; z<this.menu.getSons().get(i).getSons().get(j).getSons().size(); z++) {
							if(track.equals(this.menu.getSons().get(i).getSons().get(j).getSons().get(z).getName()))
							{
								String albums = "";
								for(int y=0; y<this.menu.getSons().get(i).getSons().size(); y++)
								{
									albums += this.menu.getSons().get(i).getSons().get(y).getName()+";";
								}
								return albums;
							}
						}
					}
				}
			}

			return null;
		}
	// returns true if the specified string is an artist, else returns false
	public boolean isArtist(String artist) {
		if (!this.menu.getSons().isEmpty() || this.menu.getSons() != null) {
			for (int i = 0; i < this.menu.getSons().size(); i++) {
				if (artist.equals(this.menu.getSons().get(i).getName()))
					return true;
			}
		}
		return false;
	}
	
	// returns true if the specified string is an album, else returns false
	public boolean isAlbum(String album) {
		if (!this.menu.getSons().isEmpty() || this.menu.getSons() != null) {
			for (int i = 0; i < this.menu.getSons().size(); i++) {
				for (int j = 0; j < this.menu.getSons().get(i).getSons().size(); j++) {
					if (album.equals(this.menu.getSons().get(i).getSons().get(j).getName()))
						return true;
				}
			}
		}

		return false;
	}
	
	// returns true if the specified string is an album, else returns false
	public boolean isTrack(String track) {
		if (!this.menu.getSons().isEmpty() || this.menu.getSons() != null) {
			for (int i = 0; i < this.menu.getSons().size(); i++) {
				for (int j = 0; j < this.menu.getSons().get(i).getSons().size(); j++) {
					for (int z = 0; z < this.menu.getSons().get(i).getSons().get(j).getSons().size(); z++) {
						if (track.equals(this.menu.getSons().get(i).getSons().get(j).getSons().get(z).getName()))
							return true;
					}
				}
			}
		}

		return false;
	}
	
	//returns the absolutePath of the specified track
	public String getTrackPath(String track) {
		if (!this.menu.getSons().isEmpty() || this.menu.getSons() != null) {
			for (int i = 0; i < this.menu.getSons().size(); i++) {
				for (int j = 0; j < this.menu.getSons().get(i).getSons().size(); j++) {
					for (int z = 0; z < this.menu.getSons().get(i).getSons().get(j).getSons().size(); z++) {
						if (track.equals(this.menu.getSons().get(i).getSons().get(j).getSons().get(z).getName().toLowerCase()))
							return this.menu.getSons().get(i).getSons().get(j).getSons().get(z).getPath();
					}
				}
			}
		}
		return null;
	}
	/* DEPRECATED only used to visualize the correct creation of the structure
	public void visualizeHierarchy() {
		ArrayList<Node> artists = this.menu.getSons();
		
		for(int i=0; i<artists.size(); i++)
		{
			System.out.println("artista: "+artists.get(i).getName());
			ArrayList<Node> albums = artists.get(i).getSons();
			for(int j=0; j<albums.size(); j++)
			{
				System.out.println("\talbum:"+albums.get(j).getName());
				ArrayList<Node> tracks = albums.get(j).getSons();
				for(int z=0; z<tracks.size(); z++)
				{
					System.out.println("\t\tbrano:"+tracks.get(z).getName());
				}
			}
		}
	}*/
	
	public static void main(String[] Args) {
		Tree albero = new Tree("mmdb", "root", "");
		albero.database.connect();
		albero.createHierarchy();
		//albero.visualizeHierarchy();
		System.out.println(albero.getTrackPath("radioactive"));
		try {
			albero.database.getConnection().close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
}
