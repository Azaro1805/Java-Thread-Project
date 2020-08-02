import java.sql.*;
public class Database {

	private Connection conn;
	private static Statement stmt;

	public Database(){//constructor
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/test";
			conn=DriverManager.getConnection(url, "root", "root");
			stmt=conn.createStatement();
		} 
		catch (ClassNotFoundException e) {
		}
		catch (SQLException e) {
		}

		createTables("votes");
	} 

	//Create Table
	public void createTables(String table1Name) {
		try{
			// if table already exists, drop it
			// if table already exists, SQL can't override it
			stmt.executeUpdate("DROP TABLE IF EXISTS " + table1Name);
			// create new empty table with given name
			stmt.executeUpdate("CREATE TABLE "+table1Name+" (sum int, id int, Age int, Mayor varchar(50), List varchar(50))");

			//stmt.executeUpdate(table1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	void insert(String table, VoteTicket v) {
		try{
			stmt.executeUpdate("INSERT INTO "+table+" VALUES("+v.index+", "+ v.idVoter+", "+ v.getVoterAge()+" , '"+ v.mayorSelection+"' , '"+ v.listSelection+"')");
		}
		catch(SQLException ex){
			ex.printStackTrace();
		}
	}
}