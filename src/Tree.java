import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//TODO CANCEL, WHEN FINISHED, THE UNUSED IMPORTS
public class Tree {
	
	private Node menu;
	private String database;
	private String user;
	private String password;
	private Connection connection;
	
	//initialize the object with an empty node
	public Tree() {
		this.resetMenu();
	}
	//initialize the object with an empty node and the specified database (user and password default values are "root" & "")
	public Tree(String database) {
		this.resetMenu();
		this.database = database;
		this.user = "root";
		this.password = "";
	}
	//initialize the object with an empty node and the specified database and user (password default value is "")
	public Tree(String database, String user) {
		this.resetMenu();
		this.database = database;
		this.user = user;
		this.password = "";
	}
	//initialize the object with an empty node and the specified database, user and password
	public Tree(String database, String user, String password) {
		this.resetMenu();
		this.database = database;
		this.user = user;
		this.password = password;
	}

	
	//returns the node
	public Node getMenu() {
		return menu;
	}
	//clear the node'attributes (just set as null)
	public void resetMenu() {
		this.menu = new Node();
	}
	
	//enstablish the connection to the specified, in the attributes, 
	//database and saves the object, still in the attributes of the class's object
	public boolean connect() {

		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver);
			String url = "jdbc:mysql://localhost/"+this.database+"?serverTimezone=GMT";
			this.connection = DriverManager.getConnection(url, this.user, this.password);
			
		}

		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;

	}
	
	//enstablish connection to the specified database and returns the object who represents the connection
	public Connection connect(String url, String user, String password) {
		Connection conn = null;
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
			
		}

		catch (SQLException e) {
			e.printStackTrace();
			return conn;
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
			return conn;
		}
		
		return conn;
	}
	
	
	
}
