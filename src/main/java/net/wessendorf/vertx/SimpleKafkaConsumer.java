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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

import net.wessendorf.vertx.ios.NotificationSenderCallback;
import net.wessendorf.vertx.ios.PushyApnsSender;
import net.wessendorf.vertx.kafka.GenericDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import io.vertx.kafka.client.serialization.JsonObjectDeserializer;
import io.vertx.kafka.client.consumer.KafkaReadStream;
import io.vertx.kafka.client.serialization.JsonObjectDeserializer;
import net.wessendorf.vertx.helper.MessageHolderWithTokens;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

public class SimpleKafkaConsumer extends AbstractVerticle {

    private KafkaReadStream<String, String> consumer;
    PushyApnsSender sender = new PushyApnsSender();
    private ObjectMapper mapper = new ObjectMapper();

  @Override
  public void start(Future<Void> fut) {

      Properties config = new Properties();
      config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.17.0.9:9092");
      config.put(ConsumerConfig.GROUP_ID_CONFIG, "mygroup2");
      config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
      config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);


      consumer = KafkaReadStream.create(vertx, config);

      consumer.handler(record -> {

          try {
              MessageHolderWithTokens messageContainer = mapper.readValue(record.value(), MessageHolderWithTokens.class);
              sender.sendPushMessage(messageContainer.getVariant(), messageContainer.getDeviceTokens(), messageContainer.getUnifiedPushMessage(), messageContainer.getPushMessageInformation().getId(), new NotificationSenderCallback() {
                  @Override
                  public void onSuccess() {

                  }

                  @Override
                  public void onError(String reason) {

                  }
              });



          } catch (IOException e) {
              e.printStackTrace();
          }


      });

      consumer.subscribe(Collections.singleton("agpush_APNsTokenTopic"));

      // we are ready w/ deployment
      fut.complete();
  }
}



