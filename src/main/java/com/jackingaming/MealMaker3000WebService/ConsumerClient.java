package com.jackingaming.MealMaker3000WebService;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerClient {
    private final static String TOPIC = "meal-to-prepare-topic";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092,localhost:9093,localhost:9094";

    private KafkaConsumer<Long, String> kafkaConsumer;

    public ConsumerClient() {
        Properties settings = createConfigurationSettingsForConsumer();
        kafkaConsumer = createAndSubscribeTheConsumer(settings);
        attachShutdownBehavior();
    }

    private void attachShutdownBehavior() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    System.out.println("### Stopping KafkaConsumer ###");
                    kafkaConsumer.close();
                }
        ));
    }

    public ConsumerRecords<Long, String> pollData() {
        ConsumerRecords<Long, String> consumerRecords = null;

        try {
            // TODO: pollingData
            consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return consumerRecords;
    }

    private static Properties createConfigurationSettingsForConsumer() {
        Properties settings = new Properties();
        settings.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-client-v0.1");
        settings.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        settings.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongDeserializer");
        settings.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return settings;
    }

    private static KafkaConsumer<Long, String> createAndSubscribeTheConsumer(Properties settings) {
        KafkaConsumer<Long, String> consumer = new KafkaConsumer<Long, String>(settings);
        consumer.subscribe(Arrays.asList(TOPIC));
        return consumer;
    }
}