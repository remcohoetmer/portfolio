package nl.remco.service.jsonutils;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import nl.remco.service.utils.HTTPUtil;

public class CustomJsonTimestampSerializer extends JsonSerializer<Date>{


	@Override
	public void serialize(Date date, JsonGenerator jsonGenerator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		jsonGenerator.writeString( HTTPUtil.HTTP_TIMESTAMPFORMAT.format(date));

	}
}
