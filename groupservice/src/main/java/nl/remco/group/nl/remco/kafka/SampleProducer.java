/*
 * Copyright (c) 2016-2017 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.remco.group.nl.remco.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SampleProducer {

  private static final Logger log = LoggerFactory.getLogger(SampleProducer.class.getName());
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS z dd MMM yyyy");

  private KafkaSender<Integer, String> sender;

  @PostConstruct
  public void init() {
    init(BOOTSTRAP_SERVERS);
  }

  public void init(String bootstrapServers) {
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "sample-producer");
    props.put(ProducerConfig.ACKS_CONFIG, "all");
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    SenderOptions<Integer, String> senderOptions = SenderOptions.create(props);
    sender = KafkaSender.create(senderOptions);
  }

  @PreDestroy
  public void close() {
    sender.close();
  }

  public Flux<SenderResult<Integer>> sendMessages(String topic, String message) throws InterruptedException {
    return sender.<Integer>send(Flux.range(1, 1)
      .map(i -> SenderRecord.create(new ProducerRecord<Integer, String>(topic, 1, null, i, message, null), i)))
      .doOnError(e -> log.error("Send failed", e))
      .doOnNext(r -> {
        RecordMetadata metadata = r.recordMetadata();
        System.out.printf("Message %d sent successfully, topic-partition=%s-%d offset=%d timestamp=%s\n",
          r.correlationMetadata(),
          metadata.topic(),
          metadata.partition(),
          metadata.offset(),
          dateFormat.format(new Date(metadata.timestamp())));
      });
  }


}
