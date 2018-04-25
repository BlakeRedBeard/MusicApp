import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
				ResultSet album = this.database.select("SELECT codice, nome FROM album WHERE album.codAutore = "+autori.getString("codice"));
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
