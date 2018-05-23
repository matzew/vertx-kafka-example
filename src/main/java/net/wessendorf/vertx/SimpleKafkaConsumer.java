/*
 * Copyright (C) 201 Matthias Weßendorf.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.wessendorf.vertx;

import io.reactivex.Flowable;
import io.vertx.core.Future;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.kafka.client.consumer.KafkaConsumer;
import io.vertx.reactivex.kafka.client.consumer.KafkaConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Map;
import java.util.Properties;

public class SimpleKafkaConsumer extends AbstractVerticle {

    private KafkaReadStream<String, String> consumer;

  @Override
  public void start(Future<Void> fut) {

      Map config = new Properties();
      config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.0.7:9092");
      config.put(ConsumerConfig.GROUP_ID_CONFIG, "my_group");
      config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
      config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

      Flowable<KafkaConsumerRecord> stream = KafkaConsumer.<String, String>create(vertx, config)
              .subscribe("testy")
              .toFlowable();

      stream
              .subscribe(data -> {
                  System.out.println("DA.......... " + data.value());
              });

      // we are ready w/ deployment
      fut.complete();
  }
}



