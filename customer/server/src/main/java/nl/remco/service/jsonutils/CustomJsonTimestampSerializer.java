package nl.remco.service.jsonutils;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import nl.remco.service.utils.HTTPUtil;

public class CustomJsonTimestampSerializer extends JsonSerializer<Date>{


	@Override
	public void serialize(Date date, JsonGenerator jsonGenerator,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {

		jsonGenerator.writeString( HTTPUtil.HTTP_TIMESTAMPFORMAT.format(date));

	}
}
