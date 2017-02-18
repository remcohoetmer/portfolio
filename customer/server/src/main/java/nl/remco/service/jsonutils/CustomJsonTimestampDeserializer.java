package nl.remco.service.jsonutils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import nl.remco.service.utils.HTTPUtil;

public class CustomJsonTimestampDeserializer extends JsonDeserializer<Date>{
    @Override
    public Date deserialize(JsonParser jsonparser,
            DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {

        String date = jsonparser.getText();
        try {
            return HTTPUtil.HTTP_TIMESTAMPFORMAT.parse(date);
        } catch (ParseException e) {
            throw new JsonParseException(e.getMessage(), jsonparser.getCurrentLocation());
        }

    }
}
