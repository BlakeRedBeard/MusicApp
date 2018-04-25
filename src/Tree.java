import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//TODO CANCEL, WHEN FINISHED, THE UNUSED IMPORTS
public class Tree {
	
	private Node menu;

	
	//initialize the object with an empty node
	public Tree() {
		this.resetMenu();
	}
	
	//returns the node
	public Node getMenu() {
		return menu;
	}
	//clear the node'attributes (just set as null)
	public void resetMenu() {
		this.menu = new Node();
	}
	
	
	
	
}
