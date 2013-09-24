
import java.util.*;
import java.text.*;

public class CopiaLlamadas {
	
	private List<Llamada> llamadas = null;
	private DateExt _fechaInicio = null;
	private DateExt _fechaFin = null;
	private String _proyecto = null;
	
	public CopiaLlamadas(String proy){
		_fechaInicio = new DateExt();
		try{
			//se le quita la hora a la fecha
			_fechaInicio = DateExt.parse("yyyy-MM-dd", _fechaInicio.toString("yyyy-MM-dd"));
		}catch(ParseException ex){}
		_fechaFin = _fechaInicio.addDays(1);		
		_proyecto = proy;
	}
	
	public CopiaLlamadas(DateExt fecha, String proy){
		_fechaInicio = fecha;
		_fechaFin = _fechaInicio.addDays(1);		
		_proyecto = proy;
	}
	
	public CopiaLlamadas(DateExt fechaInicio, DateExt fechaFin, String proyecto){
		_fechaInicio = fechaInicio;
		_fechaFin = fechaFin.addDays(1);
		_proyecto = proyecto;
	}
	
	public void CopiarLlamadas(){
		
		ObtenerLlamadasUCCX();
		EliminarLlamadas();
		Restar5Horas();
		EliminarLlamadasFueraRango();
		EliminarLlamadasDuplicadas();
		GuardarLlamadas();
		System.out.println("Cantidad de Registros copiados: " + llamadas.size());
	}
	
	private void ObtenerLlamadasUCCX(){
		llamadas = Llamada.obtenerLlamadasUCCX(_fechaInicio, _fechaFin, _proyecto);
		if(llamadas==null){
			System.out.println("No se pudieron obtener las llamadas desde el UCCX");
			System.exit(1);
		}
		//Se elimina el primer registro ya que es un indicador del SP para identificar el final de la extraccion de datos
		llamadas.remove(0);
		System.out.println(llamadas.size() + " llamadas obtenidas desde el UCCX");
	}
	
	private void EliminarLlamadas(){
		if(llamadas.size()==0) return;
		Llamada.eliminarLlamadas(_fechaInicio, _fechaFin, _proyecto);
	}
	
	private void GuardarLlamadas(){
		if(llamadas.size()==0){
			return;
		}
		Llamada.guardarLlamadas(llamadas);
		System.out.println(llamadas.size() + " llamadas guardadas");
	}
	
	private void Restar5Horas(){
		for(Llamada l : llamadas){
			l.setFechaInicio(l.getFechaInicio().addHours(-5));
			l.setFechaFin(l.getFechaFin().addHours(-5));
		}
	}
	
	private void EliminarLlamadasFueraRango(){
		int cant = 0;
		Iterator<Llamada> it = llamadas.iterator();
		while(it.hasNext()){
			Llamada l = it.next();
			if(l.getFechaInicio().before(_fechaInicio) || l.getFechaFin().after(_fechaFin)){
				it.remove();
				cant++;
			}
		}
		System.out.println(cant + " fuera de rango de fechas");
	}
	
	private void EliminarLlamadasDuplicadas(){
		List<Llamada> duplicates = ObtenerListaDuplicados();
		EliminarDuplicados(duplicates);
	}
	
	//Obtiene las llamadas duplicadas según el id
	private List<Llamada> ObtenerListaDuplicados(){
		Set<Llamada> duplicatesSet = new TreeSet<Llamada>(Llamada.COMPARE_BY_ID);
		Set<Llamada> llamadaSet = new TreeSet<Llamada>(Llamada.COMPARE_BY_ID);
		for(Llamada ll : llamadas)
		{
		    if(!llamadaSet.add(ll))
		    {
		        duplicatesSet.add(ll);
		    }
		}
		return new ArrayList<Llamada>(duplicatesSet);
	}
	
	//Elimina las llamadas duplicadas con tiempo de hablado cero
	//La lista de llamadas duplicadas debe estar ordenada por id
	private void EliminarDuplicados(List<Llamada> duplicates){
		Collections.sort(llamadas, Llamada.COMPARE_BY_ID);
		int idxDuplList = 0;
		Iterator<Llamada> it = llamadas.iterator();
		Llamada ll = it.next();
		while(it.hasNext()){
			if(ll.getId().equals(duplicates.get(idxDuplList).getId())){
				do{
					if(ll.getTiempoHablado()==0){
						it.remove();
					}
					if(it.hasNext()){
						ll = it.next();
					}else{
						break;
					}
				}while(ll.getId().equals(duplicates.get(idxDuplList).getId()));
				idxDuplList = idxDuplList != duplicates.size()-1? idxDuplList + 1 : idxDuplList;
			}else{
				ll = it.next();
			}
		}
	}
	
	public static void main(String[] args){
		CopiaLlamadas obj = null;
		String sfecini, sfecfin, sproy;
		DateExt dfecini, dfecfin;
		switch(args.length){
			//1 parametro: se ejecuta el proceso para la fecha actual y el proyecto especificado
			case 1:
				obj = new CopiaLlamadas(args[0]);
				break;
			//2 parametros: se ejecuta el proceso para la fecha y proyecto especificados
			case 2:
				sfecini = args[0];
				sproy = args[1];
				
				dfecini = null;
				try{
					dfecini=DateExt.parse("yyyy-MM-dd",sfecini);
				}
				catch(ParseException ex){
					System.out.println("La fecha de ingresada debe ser ingresada en el formato YYYY-MM-DD");
					return;
				}
				
				obj = new CopiaLlamadas(dfecini,sproy);
				break;
			//3 parametros: se ejecuta el proceso para un rango de fechas y proyecto especificado
			case 3:
				sfecini = args[0];
				sfecfin = args[1];
				sproy = args[2];
				
				dfecini = null;
				dfecfin = null;
				
				try{
					dfecini=DateExt.parse("yyyy-MM-dd",sfecini);
				}
				catch(ParseException ex){
					System.out.println("La fecha de inicio debe ser ingresada en el formato YYYY-MM-DD");
					return;
				}
				
				try{
					dfecfin=DateExt.parse("yyyy-MM-dd",sfecfin);
				}
				catch(ParseException ex){
					System.out.println("La fecha de fin debe ser ingresada en el formato YYYY-MM-DD");
					return;
				}
				
				if(!dfecini.before(dfecfin)){
					System.out.println("El rango de fechas ingresado no es correcto");
					return;
				}
				
				obj = new CopiaLlamadas(dfecini,dfecfin,sproy);
				break;
			default:
				System.out.println("Cantidad incorrecta de parametros");
				return;
		}
				
		obj.CopiarLlamadas();
		obj=null;
    }
}