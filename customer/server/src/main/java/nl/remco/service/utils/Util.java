package nl.remco.service.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import nl.remco.service.common.model.Identifiable;

public class Util {
	@SuppressWarnings("rawtypes")
	public static boolean isDefined(List l) {
		return l!= null && !l.isEmpty();
	}

	public static boolean isDefined(Identifiable identifiable) {
		return identifiable!= null && isDefined(identifiable.getId());
	}

	public static boolean isDefined(String s) {
		return (s!= null && !s.isEmpty());
	}

	public static List<Long> toLongList( String string)
	{
		String[] stringarray= string.split(",");
		List<Long> list= new ArrayList<Long>();  
		for (int i = 0; i < stringarray.length; i++) {  
			list.add( Long.valueOf(stringarray[i]));  
		}
		return list;
	}
	
	public static List<String> toStringList( String string)
	{
		if (string== null || string.isEmpty()) {
			return null;
		}
		String[] stringarray= string.split(",");
		return Arrays.asList(stringarray);
	}
	
	public static Date toDate( int year, int month, int day_of_month)
	{

		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month -1);
		c.set(Calendar.DAY_OF_MONTH, day_of_month);
		Date date = c.getTime();
		return date;
	}
	
	@SuppressWarnings("unused")
	public static InputStream dumpInputStream( InputStream is) throws IOException
	{
		if (false) {
			StringWriter writer = new StringWriter();
			//IOUtils.copy(is, writer, "UTF-8");
			String theString = writer.toString();
			System.err.println( "Data:"+ theString);
			is= new ByteArrayInputStream(theString.getBytes("UTF-8"));
		}
		return is;
	}

}
