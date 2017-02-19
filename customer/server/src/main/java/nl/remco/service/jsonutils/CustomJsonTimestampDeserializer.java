package nl.remco.service.jsonutils;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

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
