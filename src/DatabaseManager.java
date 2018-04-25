import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class DatabaseManager {

	private String database;
	private String user;
	private String password;
	private Connection connection;
	
		//initialize the object with the specified database (user and password default values are "root" & "")
		public DatabaseManager(String database) {
			this.database = database;
			this.user = "root";
			this.password = "";
		}
		
		//initialize the object with the specified database and user (password default value is "")
		public DatabaseManager(String database, String user) {
			this.database = database;
			this.user = user;
			this.password = "";
		}
		
		//initialize the object with the specified database, user and password
		public DatabaseManager(String database, String user, String password) {
			this.database = database;
			this.user = user;
			this.password = password;
		}

		public Connection getConnection() {
			return this.connection;
		}
		//establish the connection to the specified, in the attributes, 
		//database and saves the object, still in the attributes of the class's object
		//returns true, if the connection is set, or returns false if there was an error
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
		
		//establish connection to the specified database
		//returns the object who represents the connection or else returns null
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
	
		//execute 'SELECT' kind queries and returns the result
		public ResultSet select(String query) throws SQLException {
			Statement statement = this.connection.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			return result;
		}
		
		//execute 'SELECT' kind queries (with the specified connection) and returns the result
		public ResultSet select(Connection conn, String query) throws SQLException {
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(query);
			
			return result;
		}
				
		//execute the update, delete or insert query with the specified values
		//example of query:"UPDATE table SET field = ? WHERE field = ?"
		/*the "?" symbol is where we want to put the specified value,
		 *we can replace them with the various "set" methods of the PreparedStatement's object,
		 *the parameters of the method are 2: the first is the ordinary position of the value (defined by "?");
		 *the second parameter is the actual value we want to put in.
		 */
		/*this method contains 2 parameters: the UPDATE query we want to execute, and the value to put in;
		 *the values are specified in an array of strings composed in this way:
		 *{type;value, int;45, string;hello, float;45.68, ...}
		 */
		public boolean update(String query, String[] values) throws SQLException {
			PreparedStatement prStatement = this.connection.prepareStatement(query);
			String parametro, tipo, valore;
			for(int i=0; i<values.length; i++)
			{
				parametro = values[i];
				tipo = parametro.substring(0, parametro.indexOf(";")).toLowerCase();
				valore = parametro.substring(parametro.indexOf(";")+1);
				
				switch(tipo)
				{
					case "int": 		prStatement.setInt(i, Integer.parseInt(valore));
										break;
							
					case "float": 		prStatement.setFloat(i, Float.parseFloat(valore));
										break;
								  
					case "double": 		prStatement.setDouble(i, Double.parseDouble(valore));
								   		break;
					
					case "String": 		prStatement.setString(i, valore);
								   		break;
								   
					case "timestamp": 	prStatement.setTimestamp(i, Timestamp.valueOf(valore));;
										break;
					
					case "date": 		prStatement.setDate(i, Date.valueOf(valore));
										break;
					
					case "boolean": 	prStatement.setBoolean(i, Boolean.parseBoolean(valore));
										break;
				}
			}
			
			prStatement.executeUpdate();
			prStatement.close();
			return true;
		}
		
		//same as the one above but with the specified database in the connection
		public boolean update(Connection conn, String query, String[] values) throws SQLException {
			PreparedStatement prStatement = conn.prepareStatement(query);
			String parametro, tipo, valore;
			for(int i=0; i<values.length; i++)
			{
				parametro = values[i];
				tipo = parametro.substring(0, parametro.indexOf(";")).toLowerCase();
				valore = parametro.substring(parametro.indexOf(";")+1);
				
				switch(tipo)
				{
					case "int": 		prStatement.setInt(i, Integer.parseInt(valore));
										break;
							
					case "float": 		prStatement.setFloat(i, Float.parseFloat(valore));
										break;
								  
					case "double": 		prStatement.setDouble(i, Double.parseDouble(valore));
								   		break;
					
					case "String": 		prStatement.setString(i, valore);
								   		break;
								   
					case "timestamp": 	prStatement.setTimestamp(i, Timestamp.valueOf(valore));;
										break;
					
					case "date": 		prStatement.setDate(i, Date.valueOf(valore));
										break;
					
					case "boolean": 	prStatement.setBoolean(i, Boolean.parseBoolean(valore));
										break;
				}
			}
			
			prStatement.executeUpdate();
			prStatement.close();
			return true;
		}
		
}
