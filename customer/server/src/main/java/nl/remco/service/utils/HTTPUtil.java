package nl.remco.service.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import nl.remco.service.common.web.BadRequestException;

public class HTTPUtil {
	public final static SimpleDateFormat HTTP_TIMESTAMPFORMAT;
	static {
		HTTP_TIMESTAMPFORMAT= new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
		HTTP_TIMESTAMPFORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public static Date parse(String modified) {
		if (modified==null) {
			throw new BadRequestException( "HTTP header param If-Unmodified-Since is required");
		}
		
		try {
			Date date= HTTP_TIMESTAMPFORMAT.parse( modified);
			return date;
		} catch (ParseException e) {
			throw new BadRequestException( "Datum onjuist geformatteerd");
		}

	}

	public static String format(Date date) {
		return HTTP_TIMESTAMPFORMAT.format(date);
	}

}
