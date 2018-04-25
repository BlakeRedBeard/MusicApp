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
			ResultSet autori = this.database.select("SELECT codice, nome FROM autore");
			
			while(autori.next())
			{
				this.menu.addSon(null, autori.getString("nome"), autori.getString("codice"));
				System.out.println("artista aggiunto: "+autori.getString("nome"));
				ResultSet album = this.database.select("SELECT codice, nome FROM album WHERE album.codAutore = "+autori.getString("codice"));
				ArrayList<Node> artisti = this.menu.getSons();
				for(int i=0; i<artisti.size(); i++)
				{
					if(artisti.get(i).getPrimaryKey().equals(autori.getString("codice")))
					{
						while(album.next())
						{
							artisti.get(i).addSon(null, album.getString("nome"), album.getString("codice"));
							System.out.println("\talbum aggiunto: "+album.getString("nome"));
							ResultSet brani = this.database.select("SELECT codice, nome FROM brano WHERE brano.codAlbum = "+album.getString("codice"));
							ArrayList<Node> albums = artisti.get(i).getSons();
							for(int j=0; j<albums.size(); j++)
							{
								if(albums.get(j).getPrimaryKey().equals(album.getString("codice")))
								{
									while(brani.next())
									{
										albums.get(j).addSon(null, brani.getString("nome"), brani.getString("codice"));
										System.out.println("\t\tbrano aggiunto: "+brani.getString("nome"));
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] Args) {
		Tree albero = new Tree("mmdb", "root", "");
		albero.database.connect();
		albero.createHierarchy();
		try {
			albero.database.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
