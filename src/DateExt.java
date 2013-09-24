import java.util.*;
import java.text.*;

public class DateExt extends Date
{
	
	public DateExt(){
		super();
	}
	
	public DateExt(long ml){
		super(ml);
	}
	
	public DateExt(Date d){
		super(d.getTime());			
	}

	public DateExt addDays(int days)
    {
		Calendar cal = convertToCalendar(this);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
		return convertToDateExt(cal);
    }
	
	public DateExt addHours(int hours)
    {
		Calendar cal = convertToCalendar(this);
        cal.add(Calendar.HOUR_OF_DAY, hours); //minus number would decrement the hours
        return convertToDateExt(cal);
    }
	
	public String toString(String format){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(this);
	}
	
	public int getDayOfMonth(){
		Calendar cal = convertToCalendar(this);
		return cal.get(Calendar.DATE);
	}
	
	public static DateExt parse(String format, String date) throws ParseException{
		DateFormat df = new SimpleDateFormat(format);
		Date result = null;
		result = df.parse(date);
		return new DateExt(result);
	}
	
	private Calendar convertToCalendar(DateExt d){
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return cal;
	}
	
	private DateExt convertToDateExt(Calendar cal){
		Date d = cal.getTime();
		DateExt de = new DateExt(d.getTime());
		return de;
	}
	
	
}