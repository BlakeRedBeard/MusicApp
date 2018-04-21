import java.sql.*;

public class ProvaJDBC {
	
	public static void main(String args[]) {

		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver);
			String url = "jdbc:mysql://localhost/ricettario?serverTimezone=GMT";
			Connection con = DriverManager.getConnection(url, "root", "");
			Statement cmd = con.createStatement();
			String query = "SELECT codice, nome FROM ricetta";
			ResultSet res = cmd.executeQuery(query);
			while (res.next()) {
				System.out.print(res.getString("codice")+"; ");
				System.out.println(res.getString("nome"));
			}
			res.close(); // chiudere le risorse DB è obbligatorio
			cmd.close();
			con.close();
		}

		catch (SQLException e) {
			e.printStackTrace();
		}

		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
