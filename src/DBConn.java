
import java.sql.*;

public abstract class DBConn {
	
	public static Connection getJDBCConnection(String url, String className)
	{
		Connection conn = null;
		
		try{
			Class.forName(className);
		} 
		catch (Exception e){
			System.out.println("DBConn.getJDBCConnection >> ERROR: failed to load JDBC driver.");
		}

		try{
			conn = DriverManager.getConnection(url);
		} 
		catch (SQLException e){
			System.out.println("DBConn.getJDBCConnection >> ERROR: failed to connect!");
		}
		
		return conn;
	}
}