import java.util.*;
import java.sql.*;

public class Llamada{
	String _id = null;
	DateExt _fechaInicio = null;
	DateExt _fechaFin = null;
	int _estado = 0;
	String _anexoOrigen = null;
	String _anexoDestino = null;
	String _nroMarcado = null;
	String _cola = null;
	int _tiempoCola = 0;
	String _agente = null;
	int _tiempoTimbrado = 0;
	int _tiempoHablado = 0;
	int _tiempoTrabajo = 0;
	String _proyecto = null;
	
	//--------------------------------------------- setters ------------------------------------------------
	public void setId(String id){
		_id = id;
	}
	
	public void setFechaInicio(DateExt fechaInicio){
		_fechaInicio=fechaInicio;
	}
	
	public void setEstado(int estado){
		_estado=estado;
	}
	
	public void setAnexoOrigen(String anexoOrigen){
		_anexoOrigen=anexoOrigen;
	}
	
	public void setAnexoDestino(String anexoDestino){
		_anexoDestino=anexoDestino;
	}
	
	public void setNroMarcado(String nroMarcado){
		_nroMarcado=nroMarcado;
	}
	
	public void setCola(String cola){
		_cola=cola;
	}
	
	public void setAgente(String agente){
		_agente=agente;
	}
	
	public void setTiempoTimbrado(int tiempoTimbrado){
		_tiempoTimbrado=tiempoTimbrado;
	}
	
	public void setTiempoHablado(int tiempoHablado){
		_tiempoHablado=tiempoHablado;
	}
	
	public void setTiempoTrabajo(int tiempoTrabajo){
		_tiempoTrabajo=tiempoTrabajo;
	}
	
	public void setTiempoCola(int tiempoCola){
		_tiempoCola=tiempoCola;
	}
	
	public void setFechaFin(DateExt fechaFin){
		_fechaFin=fechaFin;
	}
	
	public void setProyecto(String proyecto){
		_proyecto=proyecto;
	}
	//--------------------------------------------- getters ------------------------------------------------
	public String getId(){
		return _id;
	}
	
	public DateExt getFechaInicio(){
		return _fechaInicio;
	}
	
	public int getEstado(){
		return _estado;
	}
	
	public String getAnexoOrigen(){
		return _anexoOrigen;
	}
	
	public String getAnexoDestino(){
		return _anexoDestino;
	}
	
	public String getNroMarcado(){
		return _nroMarcado;
	}
	
	public String getCola(){
		return _cola;
	}
	
	public String getAgente(){
		return _agente;
	}
	
	public int getTiempoCola(){
		return _tiempoCola;
	}
	
	public int getTiempoTimbrado(){
		return _tiempoTimbrado;
	}
	
	public int getTiempoHablado(){
		return _tiempoHablado;
	}
	
	public int getTiempoTrabajo(){
		return _tiempoTrabajo;
	}
	
	public DateExt getFechaFin(){
		return _fechaFin;
	}
	
	public String getProyecto(){
		return _proyecto;
	}
	
	//--------------------------------------------- metodos ------------------------------------------------
	public static List<Llamada> obtenerLlamadasUCCX(DateExt fechaInicio, DateExt fechaFin, String nombreProyecto){
		List<Llamada> llamadas = new ArrayList<Llamada>();
		Llamada llamada = null;
		
		Connection conn = InformixConn.getJDBCConnection();
		/*
		String proc = "EXECUTE PROCEDURE informix.sp_call_csq_agent (" +
			"DATETIME (?) YEAR TO DAY , " +
			"DATETIME (?) YEAR TO DAY " +
			",0,'NULL','NULL','?','NULL','NULL','NULL','NULL','NULL',-1,-1)";
		*/
		String fecini = fechaInicio.toString("yyyy-MM-dd");
		String fecfin = fechaFin.toString("yyyy-MM-dd");
		
		String proc = "EXECUTE PROCEDURE informix.sp_call_csq_agent (" +
			"DATETIME (" + fecini + " 00:00:00) YEAR TO SECOND, " +
			"DATETIME (" + fecfin + " 06:59:59) YEAR TO SECOND " +
			",0,'NULL','NULL','" + nombreProyecto + "','NULL','NULL','NULL','NULL','NULL',-1,-1)";
		
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(proc);
			//pstmt.setString(0, fechaInicio.toString("yyyy-MM-dd"));
			//pstmt.setString(1, fechaFin.toString("yyyy-MM-dd"));
			//pstmt.setString(2, nombreProyecto);
			ResultSet r = pstmt.executeQuery();

			while (r.next())
			{
				llamada = new Llamada();
				String id = r.getString("session_id_seq");
				int est = r.getInt("contact_disposition");
				String anexoOrig = r.getString("originator_dn");
				String anexoDest = r.getString("destination_dn");
				String nroMarc = r.getString("called_number");
				String cola = r.getString("csq_names");
				String app = r.getString("application_name");
				String agente = r.getString("agent_name");
				DateExt start_time = r.getDate("start_time")!=null? new DateExt(r.getDate("start_time")) : null;
				DateExt end_time = r.getDate("start_time")!=null? new DateExt(r.getDate("end_time")) : null;
				int tcola = r.getInt("queue_time");
				int ttrab = r.getInt("work_time");
				int ttimb = r.getInt("ring_time");
				int thabl = r.getInt("talk_time");
				
				llamada.setId(id);
				llamada.setFechaInicio(start_time);
				llamada.setFechaFin(end_time);
				llamada.setCola(cola);
				llamada.setEstado(est);
				llamada.setAnexoOrigen(anexoOrig);
				llamada.setAnexoDestino(anexoDest);
				llamada.setNroMarcado(nroMarc);
				llamada.setProyecto(app);
				llamada.setTiempoHablado(thabl);
				llamada.setTiempoTrabajo(ttrab);
				llamada.setTiempoTimbrado(ttimb);
				llamada.setTiempoCola(tcola);
				llamada.setAgente(agente);
				
				llamadas.add(llamada);
			}
			
			r.close();
			pstmt.close();
		}
		catch (SQLException e)
		{
			System.out.println("ERROR: informix.sp_call_csq_agent failed :\n" + e.getMessage() + "\n" + e.getStackTrace());
		}
		return llamadas;
	}
	
	public static boolean guardarLlamadas(List<Llamada> llamadas){
		/*StringBuffer sql ="insert into detalle_llamadas(id,fecha_inicio,estado,anexo_orig,anexo_dest,nro_marcado,cola,t_cola,agente,t_ring,t_talk,t_work,fecha_fin,proy) " +
			"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";*/
		boolean result = false;
		Connection conn = SQLServerConn.getJDBCConnection();
		String sql = formarInsert(llamadas).toString();
		PreparedStatement pstmt = null;
		//System.out.println(sql);
		try
		{
			pstmt = conn.prepareStatement(sql);
			result = pstmt.execute();
			pstmt.close();		
		}
		catch (SQLException e)
		{
			System.out.println("ERROR: Llamada.GuardarLlamadas failed :\n " + e.getMessage());
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	
	private static StringBuffer formarInsert(List<Llamada> llamadas){
		String tabla = Configuration.getInstance().getProperty(Configuration.DEST_TABLE_NAME);
		String insert = "insert into " + tabla + "(id,fecha_inicio,estado,anexo_orig,anexo_dest,nro_marcado,cola,t_cola,agente,t_ring,t_talk,t_work,fecha_fin,proy) values";
		String values = "('%s','%s',%d,'%s','%s','%s','%s',%d,'%s',%d,%d,%d,'%s','%s')";
		StringBuffer sql = new StringBuffer(insert);		
		String fomattedVal = null;
		Llamada l = null;

		for(int i=0; i<llamadas.size(); i++){
			l = llamadas.get(i);
			
			String fecini = l.getFechaInicio().toString("yyyy-MM-dd HH:mm:ss");
			String fecfin = l.getFechaFin().toString("yyyy-MM-dd HH:mm:ss");
			
			fomattedVal = String.format(values,l.getId(),fecini,l.getEstado(),
				l.getAnexoOrigen(),l.getAnexoDestino(),l.getNroMarcado(),l.getCola(),
				l.getTiempoCola(),l.getAgente(),l.getTiempoTimbrado(),
				l.getTiempoHablado(),l.getTiempoTrabajo(),fecfin,l.getProyecto());
			
			sql.append(fomattedVal);
			
			//Solo se permiten 1000 inserts en una sola sentencia
			//cada mil inserts se vuelve a crear una nueva sentencia
			if((i+1)%1000==0)
				sql.append(insert);
			else if(i!=llamadas.size()-1)
				sql.append(",");
			
		}
		return sql;
	}
	
	public static boolean eliminarLlamadas(DateExt fechaInicio, DateExt fechaFin, String nombreProyecto){
		boolean result = false;
		Connection conn = SQLServerConn.getJDBCConnection();

		String tabla = Configuration.getInstance().getProperty(Configuration.DEST_TABLE_NAME);
		String fecini = fechaInicio.toString("yyyy-MM-dd");
		String fecfin = fechaFin.toString("yyyy-MM-dd");			
		String sql = "delete from " + tabla + " where fecha_inicio between '" + 
			fecini + " 00:00:00' and '" + 
			fecfin + " 00:00:00' and proy='" + 
			nombreProyecto + "'";
			
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(sql);
			//pstmt.setString(1, fechaInicio.toString("yyyy-MM-dd"));
			//pstmt.setString(2, fechaFin.toString("yyyy-MM-dd"));
			result = pstmt.execute();
			pstmt.close();
		}
		catch (SQLException e)
		{
			System.out.println("ERROR: Llamada.EliminarLlamadas failed :\n " + e.getMessage());
			result = false;
		}
		return result;
	}
	/*
	public static List<Llamada> findElementsById(List<Llamada> llamadas, String id){
		
	}
	*/
	public static Comparator<Llamada> COMPARE_BY_ID = new Comparator<Llamada>() {
        public int compare(Llamada one, Llamada other) {
            return one.getId().compareTo(other.getId());
        }
    };
}