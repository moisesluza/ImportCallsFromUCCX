
import java.sql.*;

public class InformixConn extends DBConn{
	
	//private static final String url = "jdbc:informix-sqli://172.29.0.12:1504/db_cra:INFORMIXSERVER=mdacca_uccx;user=uccxhruser;password=c1sc0";
	//private static final String className = "com.informix.jdbc.IfxDriver";
	
	public static Connection getJDBCConnection(){
		String url = obtenerUrl();
		String className = Configuration.getInstance().getProperty(Configuration.IFX_CLASS_NAME);
		//System.out.println("Informix URL: " + url);
		//System.out.println("Informix ClassName: " + className);
		return getJDBCConnection(url, className);
	}
	
	private static String obtenerUrl(){
		String hostName = Configuration.getInstance().getProperty(Configuration.IFX_HOST_NAME);
		String port = Configuration.getInstance().getProperty(Configuration.IFX_DATABASE_PORT);
		String database = Configuration.getInstance().getProperty(Configuration.IFX_DATABASE_NAME);
		String ifxServerName = Configuration.getInstance().getProperty(Configuration.IFX_SERVER_NAME);
		String user = Configuration.getInstance().getProperty(Configuration.IFX_DATABASE_USER);
		String pwd = Configuration.getInstance().getProperty(Configuration.IFX_DATABASE_PASSWORD);
	
		String url = "jdbc:informix-sqli://<host_name>:<port_number>/<database_name>:INFORMIXSERVER=<server_name>;user=<user>;password=<password>";
		
		url= url.replace("<host_name>",hostName);
		url= url.replace("<port_number>",port);
		url= url.replace("<database_name>",database);
		url= url.replace("<server_name>",ifxServerName);
		url= url.replace("<user>",user);
		url= url.replace("<password>",pwd);
		return url;
	}
}