package nl.remco.service.utils;

import nl.remco.service.common.model.Identifiable;

public class HTTPServerUtil {

	public static void check( int aantal_updates, Identifiable identifiable)
	{
		if (aantal_updates!= 1) {
			throw new RuntimeException( "Update van object "+ identifiable.toString() + " mislukt omdat het tussentijds is gewijzigd");
		}
	}
}
