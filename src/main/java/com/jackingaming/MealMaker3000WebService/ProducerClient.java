package com.jackingaming.MealMaker3000WebService;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerClient {
    private final String TOPIC = "meal-to-prepare-topic";
    private final String BOOTSTRAP_SERVERS =
            "localhost:9092,localhost:9093,localhost:9094";

    private Long numberOfMealServed;
    private KafkaProducer<Long, String> kafkaProducer;

    public ProducerClient() {
        numberOfMealServed = 0L;
        Properties settings = createConfigurationSettingsForKafkaProducer();
        kafkaProducer = new KafkaProducer<Long, String>(settings);
        attachShutdownBehavior();
    }

    private Properties createConfigurationSettingsForKafkaProducer() {
        Properties settings = new Properties();
        settings.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-client-v0.1");
        settings.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        settings.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.LongSerializer");
        settings.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        return settings;
    }

    private void attachShutdownBehavior() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    System.out.println("### Stopping KafkaProducer ###");
                    kafkaProducer.close();
                }
        ));
    }

    public void sendData(String mealToPostAsJSONString) {
        Long keyNumberOfMealServed = numberOfMealServed;
        String valueMealToPostAsJSONString = mealToPostAsJSONString;

        ProducerRecord<Long, String> record = new ProducerRecord<>(TOPIC,
                keyNumberOfMealServed, valueMealToPostAsJSONString);
        kafkaProducer.send(record);

        numberOfMealServed++;
        System.out.println("numberOfMealServed: " + numberOfMealServed);
    }
}