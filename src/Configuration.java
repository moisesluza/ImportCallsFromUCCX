import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
 

public class Configuration {
 
    Properties properties = null;
	private static Configuration INSTANCE = null;
 
	public final static String CONFIG_FILE_NAME="conf/Configuration.properties";
 
	// DataBase Server name or ip
	public final static String SQL_SERVER_NAME="SQL_SERVER_NAME";

	// Port
	public final static String SQL_DATABASE_PORT="SQL_DATABASE_PORT";

	// DataBase name
	public final static String SQL_DATABASE_NAME="SQL_DATABASE_NAME";

	// DataBase User
	public final static String SQL_DATABASE_USER="SQL_DATABASE_USER";

	// DataBase Password
	public final static String SQL_DATABASE_PASSWORD="SQL_DATABASE_PASSWORD";

	// Class name
	public final static String SQL_CLASS_NAME="SQL_CLASS_NAME";

	// Nombre Tabla destino
	public final static String DEST_TABLE_NAME="DEST_TABLE_NAME";
	
	// host name or ip
	public final static String IFX_HOST_NAME="IFX_HOST_NAME";

	// Port
	public final static String IFX_DATABASE_PORT="IFX_DATABASE_PORT";
	 
	// DataBase name
	public final static String IFX_DATABASE_NAME="IFX_DATABASE_NAME";

	//Informix Server name
	public final static String IFX_SERVER_NAME="IFX_SERVER_NAME";
	 
	// DataBase User
	public final static String IFX_DATABASE_USER="IFX_DATABASE_USER";
	 
	// DataBase Password
	public final static String IFX_DATABASE_PASSWORD="IFX_DATABASE_PASSWORD";

	// Class Name
	public final static String IFX_CLASS_NAME="IFX_CLASS_NAME";
 
    private Configuration() {
        this.properties = new Properties();
        try 
		{
            //properties.load(Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
			FileInputStream in = new FileInputStream(CONFIG_FILE_NAME);
			properties.load(in);
			in.close();
			//properties.load(getClass().getResourceAsStream(CONFIG_FILE_NAME));
			
        }
		catch (IOException ex) 
		{
			System.out.println("No se pudo cargar el archivo de configuración");
			ex.printStackTrace();
        }
    }//Configuration
 
	/**
	* Implementando Singleton
	*
	* @return
	*/
    /*public static Configuration getInstance() {
        return ConfigurationHolder.INSTANCE;
    }*/
	public static Configuration getInstance() {
        if(INSTANCE == null){
			INSTANCE=new Configuration();
		}
		return INSTANCE;
    }
 
    /*private static class ConfigurationHolder {
 
        private static final Configuration INSTANCE = new Configuration();
    }*/
 
    /**
     * Retorna la propiedad de configuración solicitada
     *
     * @param key
     * @return
     */
    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }//getProperty
}