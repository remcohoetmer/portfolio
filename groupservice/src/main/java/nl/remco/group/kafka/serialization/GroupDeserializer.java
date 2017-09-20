package nl.remco.group.kafka.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.remco.group.service.domain.Group;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GroupDeserializer implements Deserializer<Group> {
  @Override
  public void close() {
  }

  @Override
  public void configure(Map<String, ?> arg0, boolean arg1) {
  }

  @Override
  public Group deserialize(String arg0, byte[] arg1) {
    ObjectMapper mapper = new ObjectMapper();
    Group group = null;
    try {
      group = mapper.readValue(arg1, Group.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return group;
  }
}
