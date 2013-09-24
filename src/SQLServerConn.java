
import java.sql.*;

public class SQLServerConn extends DBConn{
	
	//private static final String url = "jdbc:sqlserver://172.31.101.217:1433;databaseName=GMD_MDB_Resumen;user=report_user;password=report_user";
	//private static final String className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	public static Connection getJDBCConnection(){
		String url = obtenerUrl();
		String className = Configuration.getInstance().getProperty(Configuration.SQL_CLASS_NAME);
		return getJDBCConnection(url, className);
	}
	
	private static String obtenerUrl(){
		String serverName = Configuration.getInstance().getProperty(Configuration.SQL_SERVER_NAME);
		String port = Configuration.getInstance().getProperty(Configuration.SQL_DATABASE_PORT);
		String database = Configuration.getInstance().getProperty(Configuration.SQL_DATABASE_NAME);
		String user = Configuration.getInstance().getProperty(Configuration.SQL_DATABASE_USER);
		String pwd = Configuration.getInstance().getProperty(Configuration.SQL_DATABASE_PASSWORD);
		
		String url = "jdbc:sqlserver://<server_name>:<port_number>;databaseName=<db_name>;user=<user>;password=<password>";
		
		url= url.replace("<server_name>",serverName);
		url= url.replace("<port_number>",port);
		url= url.replace("<db_name>",database);
		url= url.replace("<user>",user);
		url= url.replace("<password>",pwd);
		
		return url;		
	}
}