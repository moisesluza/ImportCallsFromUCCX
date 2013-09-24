import java.util.*;

public class DateUtil
{
		
    public static Date addDays(Date date, int days)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTime();
    }
	
	public static Date addHours(Date date, int hours)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, hours); //minus number would decrement the days
        return cal.getTime();
    }
	/*
	public static String convertToString(String format){
		java.text.DateFormat df = new java.text.SimpleDateFormat(format);
		return df.format(fechaInicio) : "";
	}
	*/
}